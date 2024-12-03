package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.koneksi;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;
    private String username;
    
    public LoginForm() {
        setTitle("SpareMaster Application");
        setSize(400, 450); // Tinggi diperbesar untuk memberikan lebih banyak ruang
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 400, 150); // Tinggi diperbesar untuk ruang logo dan teks
        headerPanel.setBackground(new Color(200, 12, 10));
        headerPanel.setLayout(null);

        try {
            // Load logo using getResource
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logo.jpg"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setBounds(150, 10, 100, 100);
            headerPanel.add(logoLabel);
        } catch (Exception ex) {
            System.err.println("Error loading logo: " + ex.getMessage());
            JLabel errorLabel = new JLabel("Logo not found!");
            errorLabel.setBounds(150, 40, 100, 25);
            headerPanel.add(errorLabel);
        }

        JLabel headerTitle = new JLabel("Welcome to SpareMaster");
        headerTitle.setBounds(110, 110, 200, 30);
        headerTitle.setFont(new Font("Arial", Font.BOLD, 16));
        headerTitle.setForeground(Color.WHITE);
        headerPanel.add(headerTitle);

        add(headerPanel);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(0, 180, 500, 200);

        mainPanel.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(65, 10, 100, 25);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(64, 64, 64)); // Abu-abu gelap
        mainPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 10, 150, 25);
        mainPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(65, 50, 100, 25);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(64, 64, 64)); // Abu-abu gelap
        mainPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 50, 150, 25);
        mainPanel.add(passwordField);

        errorLabel = new JLabel("Password incorrect!");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(70, 90, 200, 25);
        errorLabel.setVisible(false);
        mainPanel.add(errorLabel);

        loginButton = new JButton("Login");
        loginButton.setBounds(160, 110, 100, 30);
        loginButton.setBackground(new Color(0, 153, 76)); // Hijau terang
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                String permission = validateLogin(username, password);
                if (permission != null) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    if (permission.equals("admin")) {
                        new MainMenuForm().setVisible(true);
                    }
                    if (permission.equals("user")) {
                        new MainMenuBuyer(username).setVisible(true);
                    }
                    dispose();
                } else {
                    errorLabel.setVisible(true);
                }
            }
        });

        add(mainPanel);

        // Footer
        JLabel footerLabel = new JLabel("© 2024 SpareMaster.co");
        footerLabel.setBounds(20, 380, 200, 15); // Dipindahkan sesuai tinggi frame
        footerLabel.setForeground(new Color(128, 128, 128)); // Abu-abu
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        add(footerLabel);
    }

    private String validateLogin(String username, String password) {
        String permission = null;
        try {
            Connection conn = koneksi.getConnection();
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
            e.printStackTrace();
        }
        return permission;
    }

    public static void main(String[] args) {
        new LoginForm().setVisible(true);
    }
}
