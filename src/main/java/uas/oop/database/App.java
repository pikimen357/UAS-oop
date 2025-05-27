package uas.oop.database;
import java.sql.SQLException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Customer customer = new Customer(
                "budi123", "hashed_pw", "customer","budi@gmail.com",
                "Budi Santoso", "1234567890123456", "081234567890", "Jl. Mawar No. 5"
        );

        SavingsAccount account = new SavingsAccount(10000001L, 500_000);

        try {
            customer.insertToCustomer(); // hanya bagian ini yang berpotensi SQLException
        } catch (SQLException e) {
            System.out.println("Gagal insert customer: " + e.getMessage());
        }

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

