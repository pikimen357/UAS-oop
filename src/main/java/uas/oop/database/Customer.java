package uas.oop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer extends User {
    private String fullName;
    private String nik;
    private String phone;
    private String address;

    private int idCustomer;
    private int  counter;

    public Customer( String username, String passwordHash, String role, String email,
                    String fullName, String nik, String phone, String address) {
        super( username, passwordHash, role, email);
        this.idCustomer = counter++;
        this.fullName = fullName;
        this.nik = nik;
        this.phone = phone;
        this.address = address;
    }

    public int getIdCustomer() { return idCustomer; }

    @Override
    public void showInfo() {
        System.out.println("Customer: " + fullName + ", Phone: " + phone);
    }

    public String getFullName() {  return fullName;  }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public void setNik(String nik) { this.nik = nik; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setAddress(String address) { this.address = address; }

    public String getNik() { return nik; }
    public String getAddress() { return address; }


    public int insertToCustomer(Connection connection) throws SQLException {
        int userId = this.getIdUser(); // ambil ID yang sudah diset setelah insertToUser()

        String sql = """
        INSERT INTO customers (user_id, full_name, nik, phone, address)
        VALUES (?, ?, ?, ?, ?);
    """;

        var preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, fullName);
        preparedStatement.setString(3, nik);
        preparedStatement.setString(4, phone);
        preparedStatement.setString(5, address);

        int updateCount = preparedStatement.executeUpdate();
        if (updateCount > 0) {
            System.out.println("Insert Customer successful");
        } else {
            throw new SQLException("Insert customer gagal");
        }

        preparedStatement.close();

        return this.idCustomer;
    }

    public static Customer showCustomer(int id_cust, Connection conn) throws SQLException {
        String sql = """
        SELECT 
            u.id AS user_id, u.username, u.password_hash, u.role, u.email,
            c.id AS customer_id, c.full_name, c.nik, c.phone, c.address
        FROM customers c
        JOIN users u ON c.user_id = u.id
        WHERE c.id = ?;
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_cust);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Data dari tabel users
                String username = rs.getString("username");
                String passwordHash = rs.getString("password_hash");
                String role = rs.getString("role");
                String email = rs.getString("email");

                // Data dari tabel customers
                String fullName = rs.getString("full_name");
                String nik = rs.getString("nik");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                // Buat objek Customer
                Customer customer = new Customer(username, passwordHash, role, email, fullName, nik, phone, address);

                // Set id user dan id customer jika kamu punya setter
                customer.setIdUser(rs.getInt("user_id")); // kamu harus punya method setIdUser() di kelas User
                customer.idCustomer = rs.getInt("customer_id");

                return customer;
            } else {
                throw new SQLException("Customer tidak ditemukan.");
            }
        }
    }


}

