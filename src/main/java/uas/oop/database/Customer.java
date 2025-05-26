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

//    public void testExecuteUpdate () throws SQLException {
//        Connection connection = ConnectionUtil.getDataSource().getConnection();
//        Statement statement = connection.createStatement();
//
//        String sql = """
//        INSERT INTO customer (fullName, nik, phone, address)
//        """;
//
//        int updateCount = statement.executeUpdate(sql);
//        System.out.println(updateCount);
//
//        statement.close();
//        connection.close();
//    }


    public String getFullName() {  return fullName;  }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public void setNik(String nik) { this.nik = nik; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setAddress(String address) { this.address = address; }

    public String getNik() { return nik; }
    public String getAddress() { return address; }
}

