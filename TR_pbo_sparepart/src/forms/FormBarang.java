package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.koneksi;

public class FormBarang extends JFrame {
    private JTextField txtNamaBarang, txtMerk, txtKategori, txtHarga, txtStok;
    private JTextArea txtDeskripsi;
    private JButton btnSimpan, btnKembali;
    private static String username;


    public FormBarang(String username) {
        setTitle("SpareMaster Application");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        // Panel Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204)); // Warna biru cerah
        headerPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 8));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JLabel welcomeLabel = new JLabel("Form Tambah Barang");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        // Panel Utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Komponen Input
        addLabelAndField("Nama Barang", txtNamaBarang = new JTextField(), mainPanel, gbc, 0);
        addLabelAndField("Merk", txtMerk = new JTextField(), mainPanel, gbc, 1);
        addLabelAndField("Kategori", txtKategori = new JTextField(), mainPanel, gbc, 2);
        addLabelAndField("Harga", txtHarga = new JTextField(), mainPanel, gbc, 3);
        addLabelAndField("Stok", txtStok = new JTextField(), mainPanel, gbc, 4);

        // Deskripsi
        JLabel lblDeskripsi = new JLabel("Deskripsi");
        lblDeskripsi.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        mainPanel.add(lblDeskripsi, gbc);

        txtDeskripsi = new JTextArea(3, 25);
        JScrollPane scrollPane = new JScrollPane(txtDeskripsi);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        // Tambahkan Panel Utama
        add(mainPanel, BorderLayout.CENTER);

        // Panel Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.LIGHT_GRAY);
        footerPanel.setPreferredSize(new Dimension(getWidth(), getHeight() /12));
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel footerLabel = new JLabel("© 2024 SpareMaster.co");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        footerLabel.setForeground(new Color(64, 64, 64));
        footerPanel.add(footerLabel);

        add(headerPanel, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.SOUTH);

        // Tombol Simpan dan Kembali
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnSimpan = createButton("Simpan", new Color(0, 153, 76));
        btnKembali = createButton("Kembali", new Color(0, 102, 204));

        btnSimpan.setPreferredSize(new Dimension(100, 40));
        btnKembali.setPreferredSize(new Dimension(100, 40));

        btnSimpan.addActionListener(e -> {
            simpanBarang();

        });
        btnKembali.addActionListener(e -> {
            new MainMenuAdmin(username).setVisible(true);
            dispose();
        });

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnKembali);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        return button;
    }

    private void addLabelAndField(String labelText, JTextField textField, JPanel panel, GridBagConstraints gbc, int row) {
        // Label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        // Text Field
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(200, 30));  // Set ukuran lebar text field
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(textField, gbc);
    }

    private void insertToLaporanBarang(int stokBarang) throws SQLException {
        String getLastIdQuery = "SELECT id_barang FROM data_barang ORDER BY id_barang DESC LIMIT 1";
        String insertQuery = "INSERT INTO laporan_barang (id_barang, stok_barang, barang_masuk, barang_keluar, jumlah_transaksi) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = koneksi.getConnection()) {
            // Get the latest id_barang
            int lastId;
            try (PreparedStatement getLastIdStmt = conn.prepareStatement(getLastIdQuery);
                 ResultSet rs = getLastIdStmt.executeQuery()) {
                if (rs.next()) {
                    lastId = rs.getInt("id_barang");
                } else {
                    throw new SQLException("Failed to get last inserted ID");
                }
            }

            // Insert into laporan_barang
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, lastId);
                insertStmt.setInt(2, stokBarang);
                insertStmt.setInt(3, stokBarang);
                insertStmt.setInt(4, 0);
                insertStmt.setInt(5, 0);

                insertStmt.executeUpdate();
            }
        }
    }

    private void simpanBarang() {
        try {
            // Validation check
            if (txtNamaBarang.getText().isEmpty() || txtMerk.getText().isEmpty() ||
                    txtKategori.getText().isEmpty() || txtHarga.getText().isEmpty() ||
                    txtStok.getText().isEmpty() || txtDeskripsi.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double harga;
            int stok;

            try {
                harga = Double.parseDouble(txtHarga.getText());
                stok = Integer.parseInt(txtStok.getText());

                // Additional validation: Price and Stock must be greater than 0
                if (harga <= 0) {
                    JOptionPane.showMessageDialog(this, "Harga barang harus lebih besar dari 0!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (stok <= 0) {
                    JOptionPane.showMessageDialog(this, "Stok barang harus lebih besar dari 0!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga harus berupa angka dan stok harus bilangan bulat!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = koneksi.getConnection()) {
                // Start transaction
                conn.setAutoCommit(false);

                try {
                    // Insert into data_barang
                    String sql = "INSERT INTO data_barang (nama_barang, merk, kategori, harga, stok, deskripsi_barang) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";

                    PreparedStatement stmt = conn.prepareStatement(sql);
                    String namaBarang = txtNamaBarang.getText();

                    stmt.setString(1, namaBarang);
                    stmt.setString(2, txtMerk.getText());
                    stmt.setString(3, txtKategori.getText());
                    stmt.setDouble(4, harga);
                    stmt.setInt(5, stok);
                    stmt.setString(6, txtDeskripsi.getText());
                    stmt.executeUpdate();

                    // Insert into laporan_barang using nama_barang
                    insertToLaporanBarang(stok);

                    // Commit the transaction
                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Barang berhasil disimpan!");

                    this.dispose();
                    new MainMenuAdmin(username).setVisible(true);
                    // Clear the form fields after successful save
                    clearForm();
                } catch (SQLException ex) {
                    // Rollback in case of error
                    conn.rollback();
                    throw ex;
                } finally {
                    // Restore auto-commit
                    conn.setAutoCommit(true);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan barang: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormBarang(username).setVisible(true));
    }
    private void clearForm() {
        txtNamaBarang.setText("");
        txtMerk.setText("");
        txtKategori.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        txtDeskripsi.setText("");
    }
}
