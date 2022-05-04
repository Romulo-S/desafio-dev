package com.launchersoft.cnabparser.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Table(name = "files")
@Data
public class CNABFile {

    String cpf;
    String cardNumber;
    LocalTime time;
    String storeOwner;
    String storeName;
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private TransactionType transactionType;
    private LocalDate eventDate;
    private long value;

    public CNABFile() {

    }

    public CNABFile(TransactionType transactionType, LocalDate eventDate, long value, String cpf, String cardNumber,
        LocalTime time, String storeOwner, String storeName) {
        this.transactionType = transactionType;
        this.eventDate = eventDate;
        this.value = value;
        this.cpf = cpf;
        this.cardNumber = cardNumber;
        this.time = time;
        this.storeOwner = storeOwner;
        this.storeName = storeName;
    }
}
