package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import utils.koneksi;

public class FormBarang extends JFrame {
    private JTextField txtNamaBarang, txtMerk, txtKategori, txtHarga, txtStok;
    private JTextArea txtDeskripsi;
    private JButton btnSimpan, btnKembali;

    public FormBarang() {
        setTitle("Form Input Barang");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        // Panel Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204)); // Warna biru cerah
        headerPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 6)); // Tinggi header 1/6 frame
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JLabel welcomeLabel = new JLabel("Form Input Barang");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        // Panel Utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding antar elemen
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Komponen Input
        addLabelAndField("Nama Barang:", txtNamaBarang = new JTextField(), mainPanel, gbc, 0);
        addLabelAndField("Merk:", txtMerk = new JTextField(), mainPanel, gbc, 1);
        addLabelAndField("Kategori:", txtKategori = new JTextField(), mainPanel, gbc, 2);
        addLabelAndField("Harga:", txtHarga = new JTextField(), mainPanel, gbc, 3);
        addLabelAndField("Stok:", txtStok = new JTextField(), mainPanel, gbc, 4);

        // Deskripsi
        JLabel lblDeskripsi = new JLabel("Deskripsi:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        mainPanel.add(lblDeskripsi, gbc);

        txtDeskripsi = new JTextArea(3, 20);
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
        footerPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 10)); // Tinggi footer 1/10 frame
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel footerLabel = new JLabel("© 2024 SpareMaster.co");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        footerLabel.setForeground(new Color(64, 64, 64));
        footerPanel.add(footerLabel);

        // Tambahkan panel header dan footer
        add(headerPanel, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.SOUTH);

        // Tombol Simpan dan Kembali
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSimpan = new JButton("Simpan");
        btnKembali = new JButton("Kembali");

        // Styling tombol
        btnSimpan.setPreferredSize(new Dimension(100, 40));
        btnKembali.setPreferredSize(new Dimension(100, 40));

        btnSimpan.addActionListener(e -> simpanBarang());
        btnKembali.addActionListener(e -> {
            new MainMenuForm().setVisible(true);
            dispose();
        });

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnKembali);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Method untuk menambahkan label dan input field ke panel dengan GridBagLayout.
     */
    private void addLabelAndField(String labelText, JTextField textField, JPanel panel, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14)); // Menambahkan font untuk label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        textField.setFont(new Font("Arial", Font.PLAIN, 14)); // Menambahkan font untuk field
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(textField, gbc);
    }

    /**
     * Method untuk menyimpan data barang ke database.
     */
    private void simpanBarang() {
        try {
            // Validasi Input
            if (txtNamaBarang.getText().isEmpty() || txtMerk.getText().isEmpty() || txtKategori.getText().isEmpty() ||
                    txtHarga.getText().isEmpty() || txtStok.getText().isEmpty() || txtDeskripsi.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double harga;
            int stok;

            try {
                harga = Double.parseDouble(txtHarga.getText());
                stok = Integer.parseInt(txtStok.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga harus berupa angka desimal dan stok harus berupa bilangan bulat!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Simpan ke Database
            try (Connection conn = koneksi.getConnection()) {
                String sql = "INSERT INTO data_barang (nama_barang, merk, kategori, harga, stok, deskripsi_barang) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, txtNamaBarang.getText());
                stmt.setString(2, txtMerk.getText());
                stmt.setString(3, txtKategori.getText());
                stmt.setDouble(4, harga);
                stmt.setInt(5, stok);
                stmt.setString(6, txtDeskripsi.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Barang berhasil disimpan!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan barang. Coba lagi nanti.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormBarang().setVisible(true));
    }
}
