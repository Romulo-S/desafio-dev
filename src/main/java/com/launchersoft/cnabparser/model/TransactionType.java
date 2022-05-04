package com.launchersoft.cnabparser.model;

import java.security.InvalidParameterException;
import java.util.EnumMap;

import lombok.Getter;

@Getter
public enum TransactionType {
    DEBITO,
    BOLETO,
    FINANCIAMENTO,
    CREDITO,
    RECEBIMENTO_EMPRESTIMO,
    VENDAS,
    RECEBIMENTO_TED,
    RECEBIMENTO_DOC,
    ALUGUEL;

    private static final EnumMap<TransactionType, Operation> typeOperationPair;

    static {
        typeOperationPair = new EnumMap<>(TransactionType.class);
        typeOperationPair.put(TransactionType.DEBITO, Operation.Entrada);
        typeOperationPair.put(TransactionType.BOLETO, Operation.Saída);
        typeOperationPair.put(TransactionType.FINANCIAMENTO, Operation.Saída);
        typeOperationPair.put(TransactionType.CREDITO, Operation.Entrada);
        typeOperationPair.put(TransactionType.RECEBIMENTO_EMPRESTIMO, Operation.Entrada);
        typeOperationPair.put(TransactionType.VENDAS, Operation.Entrada);
        typeOperationPair.put(TransactionType.RECEBIMENTO_TED, Operation.Entrada);
        typeOperationPair.put(TransactionType.RECEBIMENTO_DOC, Operation.Entrada);
        typeOperationPair.put(TransactionType.ALUGUEL, Operation.Saída);
    }

    public static TransactionType getValue(String fileType) {
        int type = Integer.parseInt(fileType);
        switch (type){

            case 1: return TransactionType.DEBITO;
            case 2: return TransactionType.BOLETO;
            case 3: return TransactionType.FINANCIAMENTO;
            case 4: return TransactionType.CREDITO;
            case 5: return TransactionType.RECEBIMENTO_EMPRESTIMO;
            case 6: return TransactionType.VENDAS;
            case 7: return TransactionType.RECEBIMENTO_TED;
            case 8: return TransactionType.RECEBIMENTO_DOC;
            case 9: return TransactionType.ALUGUEL;
            default: throw new InvalidParameterException("Invalid CNAB File: \" + \"Expected File Type at index 1, allowed values between 1 and 9, but it was:" + type);
        }
    }
}
