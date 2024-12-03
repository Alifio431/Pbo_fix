package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;

public class ViewDataBarangForm extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnUpdate, btnDelete, btnBack;
    private Connection conn;

    public ViewDataBarangForm() {
        setTitle("Data Barang");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel welcomeLabel = new JLabel("Data Barang");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        // Tabel dan ScrollPanel
        tableModel = new DefaultTableModel(new String[]{"ID Barang", "Nama Barang", "Merk", "Kategori", "Harga", "Stok", "Deskripsi"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnUpdate = createButton("Update", new Color(0, 153, 76));
        btnDelete = createButton("Delete", new Color(204, 0, 0));
        btnBack = createButton("Kembali", new Color(0, 102, 204));

        // Tambahkan aksi tombol
        btnUpdate.addActionListener(e -> updateBarang());
        btnDelete.addActionListener(e -> hapusBarang());
        btnBack.addActionListener(e -> {
            new MainMenuAdmin().setVisible(true);
            dispose();
        });

        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);

        // Panel Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.LIGHT_GRAY);
        JLabel footerLabel = new JLabel("© 2024 SpareMaster.co");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        footerPanel.add(footerLabel);

        // Layout Utama
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Koneksi Database dan Load Data
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tr_pbo", "root", "");
            loadDataBarang();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kesalahan koneksi database: " + e.getMessage());
        }
    }

    // Method Membuat Tombol dengan Warna
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        return button;
    }

    // Method Load Data Barang
    private void loadDataBarang() {
        try {
            tableModel.setRowCount(0);
            String query = "SELECT * FROM data_barang";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("merk"),
                        rs.getString("kategori"),
                        rs.getDouble("harga"),
                        rs.getInt("stok"),
                        rs.getString("deskripsi_barang")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan dalam mengambil data: " + e.getMessage());
        }
    }

    // Method Update Barang
    private void updateBarang() {
        int selectedRow = table.getSelectedRow();

        // Validasi jika tidak ada baris yang dipilih
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan diupdate.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil data dari baris yang dipilih
        int idBarang = (int) tableModel.getValueAt(selectedRow, 0);
        String currentNamaBarang = (String) tableModel.getValueAt(selectedRow, 1);
        String currentMerk = (String) tableModel.getValueAt(selectedRow, 2);
        String currentKategori = (String) tableModel.getValueAt(selectedRow, 3);
        double currentHarga = (double) tableModel.getValueAt(selectedRow, 4);
        int currentStok = (int) tableModel.getValueAt(selectedRow, 5);
        String currentDeskripsi = (String) tableModel.getValueAt(selectedRow, 6);

        // Panel form dialog
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        JTextField txtNamaBarang = new JTextField(currentNamaBarang);
        JTextField txtMerk = new JTextField(currentMerk);
        JTextField txtKategori = new JTextField(currentKategori);
        JTextField txtHarga = new JTextField(String.valueOf(currentHarga));
        JTextField txtStok = new JTextField(String.valueOf(currentStok));
        JTextField txtDeskripsi = new JTextField(currentDeskripsi);

        panel.add(new JLabel("Nama Barang:"));
        panel.add(txtNamaBarang);
        panel.add(new JLabel("Merk:"));
        panel.add(txtMerk);
        panel.add(new JLabel("Kategori:"));
        panel.add(txtKategori);
        panel.add(new JLabel("Harga:"));
        panel.add(txtHarga);
        panel.add(new JLabel("Stok:"));
        panel.add(txtStok);
        panel.add(new JLabel("Deskripsi Barang:"));
        panel.add(txtDeskripsi);

        // Menampilkan dialog untuk input data baru
        int result = JOptionPane.showConfirmDialog(this, panel, "Update Data Barang", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Jika pengguna menekan OK
        if (result == JOptionPane.OK_OPTION) {
            try {
                String newNamaBarang = txtNamaBarang.getText().trim();
                String newMerk = txtMerk.getText().trim();
                String newKategori = txtKategori.getText().trim();
                double newHarga = Double.parseDouble(txtHarga.getText().trim());
                int newStok = Integer.parseInt(txtStok.getText().trim());
                String newDeskripsi = txtDeskripsi.getText().trim();

                // Validasi input kosong
                if (newNamaBarang.isEmpty() || newMerk.isEmpty() || newKategori.isEmpty() || newDeskripsi.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Semua kolom harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Query untuk update data
                String query = "UPDATE data_barang SET nama_barang=?, merk=?, kategori=?, harga=?, stok=?, deskripsi_barang=? WHERE id_barang=?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, newNamaBarang);
                pstmt.setString(2, newMerk);
                pstmt.setString(3, newKategori);
                pstmt.setDouble(4, newHarga);
                pstmt.setInt(5, newStok);
                pstmt.setString(6, newDeskripsi);
                pstmt.setInt(7, idBarang);

                // Eksekusi update
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil diupdate.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    loadDataBarang(); // Refresh tabel
                } else {
                    JOptionPane.showMessageDialog(this, "Tidak ada perubahan yang dilakukan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Harga dan stok harus berupa angka yang valid.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengupdate data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    // Method Hapus Barang
    private void hapusBarang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                int idBarang = (int) tableModel.getValueAt(selectedRow, 0);
                String query = "DELETE FROM data_barang WHERE id_barang=?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, idBarang);

                if (pstmt.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
                    loadDataBarang();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
            }
        }
    }

    @Override
    public void dispose() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewDataBarangForm().setVisible(true));
    }
}
