package com.launchersoft.cnabparser.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Set;

import com.launchersoft.cnabparser.model.CNABFile;
import com.launchersoft.cnabparser.model.TransactionType;

import lombok.Getter;

@Getter
/**
 * Class responsible for parse a file in CNAB format.
 * @since 0.1
 * @implNote Uses java @{@link BufferedReader} to read and parse file.
 * @author Romulo S.
 */
public class FileCNABParser {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.US);
    private static final int BEGIN_INDEX_TYPE_TRANSACTION = 0;
    private static final int END_INDEX_TYPE_TRANSACTION = 1;
    private static final int BEGIN_INDEX_YEAR_DATA_TRANSACTION = 1;
    private static final int END_INDEX_YEAR_DATA_TRANSACTION = 5;
    private static final int BEGIN_INDEX_MONTH_DATA_TRANSACTION = END_INDEX_YEAR_DATA_TRANSACTION;
    private static final int END_INDEX_MONTH_DATA_TRANSACTION = 7;
    private static final int BEGIN_INDEX_DAY_DATA_TRANSACTION = END_INDEX_MONTH_DATA_TRANSACTION;
    private static final int END_INDEX_DAY_DATA_TRANSACTION = 9;
    private static final int BEGIN_INDEX_VALUE_TRANSACTION = 10;
    private static final int END_INDEX_VALUE_TRANSACTION = 19;
    private static final int BEGIN_INDEX_CPF = END_INDEX_VALUE_TRANSACTION;
    private static final int END_INDEX_CPF = 30;
    private static final int BEGIN_INDEX_CARD_NUMBER = END_INDEX_CPF;
    private static final int END_INDEX_CARD_NUMBER = 42;
    private static final int BEGIN_INDEX_TRANSACTION_TIME_HOUR = 42;
    private static final int END_INDEX_TRANSACTION_TIME_HOUR = 44;
    private static final int BEGIN_INDEX_TRANSACTION_TIME_MINUTE = END_INDEX_TRANSACTION_TIME_HOUR;
    private static final int END_INDEX_TRANSACTION_TIME_MINUTE = 46;
    private static final int BEGIN_INDEX_TRANSACTION_TIME_SECOND = END_INDEX_TRANSACTION_TIME_MINUTE;
    private static final int END_INDEX_TRANSACTION_TIME_SECOND = 48;
    private static final int BEGIN_INDEX_STORE_OWNER = END_INDEX_TRANSACTION_TIME_SECOND;
    private static final int END_INDEX_STORE_OWNER = 62;
    private static final int BEGIN_INDEX_STORE_NAME = END_INDEX_STORE_OWNER;
    private static final int END_INDEX_STORE_NAME = 80;
    private String file;

    public FileCNABParser(String CNABfile) {
        this.file = CNABfile;
    }

    public static boolean isCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
            CPF.equals("11111111111") ||
            CPF.equals("22222222222") || CPF.equals("33333333333") ||
            CPF.equals("44444444444") || CPF.equals("55555555555") ||
            CPF.equals("66666666666") || CPF.equals("77777777777") ||
            CPF.equals("88888888888") || CPF.equals("99999999999") ||
            (CPF.length() != 11))
            return (false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else
                dig10 = (char) (r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else
                dig11 = (char) (r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return (true);
            else
                return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static String imprimeCPF(String CPF) {
        return (CPF.substring(0, 3) + "." + CPF.substring(3, 6) + "." +
            CPF.substring(6, 9) + "-" + CPF.substring(9, 11));
    }

    public Set<CNABFile> parseCNABFile() {
        BufferedReader reader = readFile();
        String fileLine;
        Set<CNABFile> cnabFiles = new HashSet<>();

        try {
            while ((fileLine = reader.readLine()) != null) {
                CNABFile cnabFile = new CNABFile();

                TransactionType transactionType = getTransactionType(fileLine);
                cnabFile.setTransactionType(transactionType);

                LocalDate parsedDate = getDataCNABFile(fileLine);
                cnabFile.setEventDate(parsedDate);

                Long aLong = getValueTransaction(fileLine);
                cnabFile.setValue(aLong);

                String cpf = getCPF(fileLine);
                cnabFile.setCpf(cpf);

                String cardNumber = getCardNumber(fileLine);
                cnabFile.setCardNumber(cardNumber);

                //get time
                LocalTime transactionTime = getTransactionTime(fileLine);
                cnabFile.setTime(transactionTime);

                String ownerName = fileLine.substring(BEGIN_INDEX_STORE_OWNER, END_INDEX_STORE_OWNER).strip();
                String storeName = fileLine.substring(BEGIN_INDEX_STORE_NAME, END_INDEX_STORE_NAME).strip();
                cnabFile.setStoreOwner(ownerName);
                cnabFile.setStoreName(storeName);

                cnabFiles.add(cnabFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cnabFiles;
    }

    private LocalTime getTransactionTime(String strLine) {
        StringBuilder builder;

        String hour = parseData(strLine, BEGIN_INDEX_TRANSACTION_TIME_HOUR, END_INDEX_TRANSACTION_TIME_HOUR);
        String minute = parseData(strLine, BEGIN_INDEX_TRANSACTION_TIME_MINUTE, END_INDEX_TRANSACTION_TIME_MINUTE);
        String second = parseData(strLine, BEGIN_INDEX_TRANSACTION_TIME_SECOND, END_INDEX_TRANSACTION_TIME_SECOND);

        builder = new StringBuilder();
        builder.append(hour);
        builder.append(":");
        builder.append(minute);
        builder.append(":");
        builder.append(second);
        String extractedTime = builder.toString();

        try {
          return LocalTime.parse(extractedTime);
        } catch (DateTimeParseException e) {
            throw new InvalidParameterException("Invalid CNAB File: \" + \"Invalid transaction time :" + extractedTime + "at line:" + strLine);
        }
    }

    private String getCardNumber(String strLine) {
        return parseData(strLine, BEGIN_INDEX_CARD_NUMBER, END_INDEX_CARD_NUMBER);
    }

    private String getCPF(String strLine) {
        String parsedCPF = parseData(strLine, BEGIN_INDEX_CPF, END_INDEX_CPF);
        if (isCPF(parsedCPF)) {
            return parsedCPF;
        } else {
            throw new InvalidParameterException("Invalid CNAB File: \" + \"Invalid CPF:" + parsedCPF + "at line:" + strLine);
        }
    }

    private Long getValueTransaction(String strLine) {
        String parsedValue = parseData(strLine, BEGIN_INDEX_VALUE_TRANSACTION, END_INDEX_VALUE_TRANSACTION);
        try {
            return Long.valueOf(parsedValue);
        } catch (NumberFormatException e) {
            e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    private LocalDate getDataCNABFile(String strLine) {
        StringBuilder builder = new StringBuilder();
        String year = parseData(strLine, BEGIN_INDEX_YEAR_DATA_TRANSACTION, END_INDEX_YEAR_DATA_TRANSACTION);
        String month = parseData(strLine, BEGIN_INDEX_MONTH_DATA_TRANSACTION, END_INDEX_MONTH_DATA_TRANSACTION);
        String day = parseData(strLine, BEGIN_INDEX_DAY_DATA_TRANSACTION, END_INDEX_DAY_DATA_TRANSACTION);

        builder.append(year);
        builder.append("-");
        builder.append(month);
        builder.append("-");
        builder.append(day);
        String extractedDate = builder.toString();

        LocalDate parsedDate = null;
        try {
            parsedDate = LocalDate.parse(extractedDate);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid CNAB File: Unable to parse the date!" + extractedDate + "at line:" + strLine);
            e.printStackTrace();
        }

        return parsedDate;
    }

    private TransactionType getTransactionType(String strLine) {
        String fileType = parseData(strLine, BEGIN_INDEX_TYPE_TRANSACTION, END_INDEX_TYPE_TRANSACTION);
        TransactionType type = TransactionType.getValue(fileType);
        return type;
    }

    private String parseData(String strLine, int beginIndex, int endIndex) {
        String fileType = strLine.substring(beginIndex, endIndex);
        return fileType;
    }

    private BufferedReader readFile() {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(this.file);
        } catch (FileNotFoundException e) {
            e.getMessage();
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader;
    }
}

