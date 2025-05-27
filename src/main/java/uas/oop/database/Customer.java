package uas.oop.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer extends User {
    private String fullName;
    private String nik;
    private String phone;
    private String address;

    public Customer( String username, String passwordHash, String role, String email,
                    String fullName, String nik, String phone, String address) {
        super( username, passwordHash, role, email);
        this.fullName = fullName;
        this.nik = nik;
        this.phone = phone;
        this.address = address;
    }

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

    public void insertToCustomer() throws SQLException {
        int userId = super.insertToUser(); // simpan user, dapatkan id dari DB

        Connection connection = ConnectionUtil.getDataSource().getConnection();

        String sql = """
        INSERT INTO customers (user_id, full_name, nik, phone, address)
        VALUES (?, ?, ?, ?, ?)
    """;

        var preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, userId); // gunakan id dari DB
        preparedStatement.setString(2, fullName);
        preparedStatement.setString(3, nik);
        preparedStatement.setString(4, phone);
        preparedStatement.setString(5, address);

        int updateCount = preparedStatement.executeUpdate();
        if (updateCount > 0) {
            System.out.println("Insert Customer successful");
        } else {
            System.out.println("Insert Customer failed");
        }

        preparedStatement.close();
        connection.close();
    }

}

