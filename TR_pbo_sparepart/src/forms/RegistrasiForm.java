package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrasiForm extends JFrame {
    private static final String VERIFICATION_CODE = "0412";

    // Konfigurasi Database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tr_pbo";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    public RegistrasiForm() {
        setTitle("Registrasi Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setMinimumSize(new Dimension(300, 300));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Judul
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 123, 255));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel titleLabel = new JLabel("Registrasi", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Panel Formulir
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Elemen Formulir
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(200, 25));
        usernameField.setMinimumSize(new Dimension(100, 25));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 25));
        passwordField.setMinimumSize(new Dimension(100, 25));

        JLabel verificationLabel = new JLabel("Kode Verifikasi (opsional):");
        verificationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField verificationField = new JTextField();
        verificationField.setFont(new Font("Arial", Font.PLAIN, 14));
        verificationField.setPreferredSize(new Dimension(200, 25));
        verificationField.setMinimumSize(new Dimension(100, 25));

        JButton registerButton = new JButton("Registrasi");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);

        // Tambahkan Elemen ke Formulir
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(verificationLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(verificationField, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(registerButton, gbc);

        // Event Tombol Registrasi
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String verificationCode = verificationField.getText();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username dan password harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String permission = VERIFICATION_CODE.equals(verificationCode) ? "admin" : "user";

                // Simpan ke database
                if (saveAccountToDatabase(username, password, permission)) {
                    JOptionPane.showMessageDialog(null, "Registrasi berhasil!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                    dispose();

                    if(permission.equals("admin")) {
                        new MainMenuAdmin(username).setVisible(true);
                    } else {
                        new MainMenuBuyer(username).setVisible(true);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Registrasi gagal. Coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Tambahkan panel ke frame
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);


    }

    private boolean saveAccountToDatabase(String username, String password, String permission) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "INSERT INTO akun (username, password, permission) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, permission);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrasiForm().setVisible(true));
    }
}
