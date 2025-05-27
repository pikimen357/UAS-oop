package uas.oop.database;

public class Admin extends User {
    public Admin( String username, String passwordHash,String email) {
        super( username, passwordHash, "admin", email);
    }

    @Override
    public void showInfo() {
        System.out.println("Admin User: " + getUsername());
    }
}

