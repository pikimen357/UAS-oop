package uas.oop.database;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private long accountNumber;
    private String type; // deposit / withdrawal
    private double amount;
    private String description;
    private LocalDateTime createdAt;

    public Transaction(long accountNumber, String type, double amount, String description) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public void printDetails() {
        System.out.printf("[%s] %s: Rp %.2f - %s\n", createdAt, type.toUpperCase(), amount, description);
    }
}

