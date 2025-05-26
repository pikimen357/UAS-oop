package uas.oop.database;

public class Admin extends User {
    public Admin(int id, String username, String passwordHash) {
        super(id, username, passwordHash, "admin");
    }

    @Override
    public void showInfo() {
        System.out.println("Admin User: " + getUsername());
    }
}

