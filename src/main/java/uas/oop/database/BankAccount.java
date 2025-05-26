package uas.oop.database;

public abstract class BankAccount {
    protected long accountNumber;
    protected double balance;

    public BankAccount(long accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public long getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }

    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);

    public void showBalance() {
        System.out.printf("Account %d balance: Rp %.2f\n", accountNumber, balance);
    }
}
