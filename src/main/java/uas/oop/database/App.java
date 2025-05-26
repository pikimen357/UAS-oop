package uas.oop.database;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Customer customer = new Customer(
                1, "budi123", "hashed_pw", "customer",
                "Budi Santoso", "1234567890123456", "081234567890", "Jl. Mawar No. 5"
        );

        SavingsAccount account = new SavingsAccount(10000001L, 500_000);

        customer.showInfo();
        account.showBalance();

        account.deposit(250_000);
        account.withdraw(100_000);
        account.showBalance();

        Transaction t1 = new Transaction(account.getAccountNumber(), "deposit", 250_000, "Setoran tunai");
        Transaction t2 = new Transaction(account.getAccountNumber(), "withdrawal", 100_000, "Tarik tunai");

        t1.printDetails();
        t2.printDetails();
    }
}

