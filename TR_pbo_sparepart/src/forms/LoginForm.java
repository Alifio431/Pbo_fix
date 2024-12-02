//log in universal
package forms;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.koneksi;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {
        setTitle("Login Form");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 30, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 30, 150, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 70, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 70, 150, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 110, 80, 25);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                String permission = validateLogin(username, password);
                if (permission != null) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    if (permission.equals("admin")) {
                        new MainMenuForm().setVisible(true);
                    } 
                    if (permission.equals("user")) {
                        new FormBeli().setVisible(true);
                    }
                    dispose();
                } 
                else {
                    JOptionPane.showMessageDialog(null, "Invalid Credentials!");
                }
            }
        });
    }

    private String validateLogin(String username, String password) {
        String permission = null;
        try {
            Connection conn = koneksi.getConnection(); // Assuming you have a method to get a DB connection
            String sql = "SELECT permission FROM akun WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                permission = rs.getString("permission");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return permission; // Returns null if no match is found
    }

    public static void main(String[] args) {
        new LoginForm().setVisible(true);
    }
}