package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewDataBarangForm extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private JButton btnUpdate, btnDelete, btnBack;
    private Connection conn;

    public ViewDataBarangForm() {
        setTitle("Data Barang");
        setSize(800, 600);  // Ukuran frame lebih besar untuk tombol dan desain yang lebih baik
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204)); // Warna biru cerah
        headerPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 6)); // Tinggi header 1/6 frame
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JLabel welcomeLabel = new JLabel("Data Barang");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        // Panel untuk Tabel
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hanya kolom tertentu yang bisa diedit
                return column > 0 && column < 6;
            }
        };

        // Menambahkan kolom
        tableModel.addColumn("ID Barang");
        tableModel.addColumn("Nama Barang");
        tableModel.addColumn("Merk");
        tableModel.addColumn("Kategori");
        tableModel.addColumn("Harga");
        tableModel.addColumn("Stok");
        tableModel.addColumn("Deskripsi Barang");

        // Mengambil data dari database dan menampilkannya
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tr_pbo", "root", "");
            loadDataBarang();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kesalahan koneksi database: " + e.getMessage());
        }

        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);

        // Panel untuk tombol
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnBack = new JButton("Kembali");

        // Menambahkan aksi tombol
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBarang();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusBarang();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenuForm().setVisible(true); // Kembali ke menu utama
                dispose();
            }
        });

        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);

        // Panel Footer
//        JPanel footerPanel = new JPanel();
//        footerPanel.setBackground(Color.LIGHT_GRAY);
//        footerPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / 10)); // Tinggi footer 1/10 frame
//        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//
//        JLabel footerLabel = new JLabel("© 2024 SpareMaster.co");
//        footerLabel.setFont(new Font("Arial", Font.ITALIC, 14));
//        footerLabel.setForeground(new Color(64, 64, 64));
//        footerPanel.add(footerLabel);

        // Layout utama
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
//        add(footerPanel, BorderLayout.SOUTH);
    }

    // Method untuk memuat data barang dari database
    private void loadDataBarang() {
        try {
            // Hapus data yang ada sebelumnya
            tableModel.setRowCount(0);

            String query = "SELECT * FROM data_barang";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Object[] rowData = new Object[7];
                rowData[0] = rs.getInt("id_barang");
                rowData[1] = rs.getString("nama_barang");
                rowData[2] = rs.getString("merk");
                rowData[3] = rs.getString("kategori");
                rowData[4] = rs.getDouble("harga");
                rowData[5] = rs.getInt("stok");
                rowData[6] = rs.getString("deskripsi_barang");
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan dalam mengambil data: " + e.getMessage());
        }
    }

    // Method untuk update barang
    private void updateBarang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan diupdate");
            return;
        }

        try {
            // Get data from selected row
            int idBarang = (int) tableModel.getValueAt(selectedRow, 0);
            String namaBarang = (String) tableModel.getValueAt(selectedRow, 1);
            String merk = (String) tableModel.getValueAt(selectedRow, 2);
            String kategori = (String) tableModel.getValueAt(selectedRow, 3);
            Double harga = (Double) tableModel.getValueAt(selectedRow, 4); // Use Double directly for harga
            int stok = ((Number) tableModel.getValueAt(selectedRow, 5)).intValue();
            String deskripsi = (String) tableModel.getValueAt(selectedRow, 6);

            // Update query
            String query = "UPDATE data_barang SET " +
                    "nama_barang = ?, merk = ?, kategori = ?, " +
                    "harga = ?, stok = ?, deskripsi_barang = ? " +
                    "WHERE id_barang = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, namaBarang);
            pstmt.setString(2, merk);
            pstmt.setString(3, kategori);
            pstmt.setDouble(4, harga); // Set harga as Double directly
            pstmt.setInt(5, stok);
            pstmt.setString(6, deskripsi);
            pstmt.setInt(7, idBarang);

            // Execute update
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate");
                // Refresh table
                loadDataBarang();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal update data: " + e.getMessage());
        }
    }

    // Method untuk hapus barang
    private void hapusBarang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus");
            return;
        }

        // Konfirmasi penghapusan
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus data ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                // Ambil ID barang dari baris yang dipilih
                int idBarang = (int) tableModel.getValueAt(selectedRow, 0);

                // Query delete
                String query = "DELETE FROM data_barang WHERE id_barang = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, idBarang);

                // Eksekusi delete
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
                    // Refresh tabel
                    loadDataBarang();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
            }
        }
    }

    // Metode untuk menutup koneksi database
    public void dispose() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewDataBarangForm().setVisible(true));
    }
}
