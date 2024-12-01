package main;

public class Akun {
    private String username;
    private String password;
    private String permission; // admin/user

    public Akun(String username, String password, String permission) {
        this.username = username;
        this.password = password;
        this.permission = permission;
    }

    // Getter and Setter (Encapsulation)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public class Admin extends Akun {
        public Admin(String username, String password) {
            super(username, password, "admin");
        }
    
        public void manageBarang() {
            System.out.println("Admin can manage barang.");
        }
    }
    
    public class User extends Akun {
        public User(String username, String password) {
            super(username, password, "user");
        }
    
        public void buyBarang() {
            System.out.println("User can buy barang.");
        }
    }
}
