package uas.oop.database;

public abstract class BankAccount {
    protected long accountNumber;
    protected double balance;
    protected int customerId; // id dari tabel customers

    public BankAccount(long accountNumber, double balance, int customerId) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.customerId = customerId;
    }

    public long getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public int getCustomerId() { return customerId; }

    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);

    public void showBalance() {
        System.out.printf("Account %d (Customer ID %d) balance: Rp %.2f\n", accountNumber, customerId, balance);
    }
}
