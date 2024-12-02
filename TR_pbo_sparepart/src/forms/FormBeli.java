//user
//form beli
package forms;

import javax.swing.*;
import java.awt.event.*;
import utils.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class FormBeli extends JFrame {
    private JComboBox<String> comboBarang;
    private JTextField txtJumlah, txtTotalHarga;
    private JButton btnBeli, btnKembali;

    public FormBeli() {
        setTitle("Form Pembelian");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblBarang = new JLabel("Pilih Barang:");
        lblBarang.setBounds(20, 30, 100, 25);
        add(lblBarang);

        comboBarang = new JComboBox<>();
        comboBarang.setBounds(150, 30, 200, 25);
        add(comboBarang);

        loadBarang();

        JLabel lblJumlah = new JLabel("Jumlah:");
        lblJumlah.setBounds(20, 70, 100, 25);
        add(lblJumlah);

        txtJumlah = new JTextField();
        txtJumlah.setBounds(150, 70, 200, 25);
        add(txtJumlah);

        JLabel lblTotalHarga = new JLabel("Total Harga:");
        lblTotalHarga.setBounds(20, 110, 100, 25);
        add(lblTotalHarga);

        txtTotalHarga = new JTextField();
        txtTotalHarga.setBounds(150, 110, 200, 25);
        txtTotalHarga.setEditable(false);
        add(txtTotalHarga);

        btnBeli = new JButton("Beli");
        btnBeli.setBounds(150, 150, 100, 30);
        add(btnBeli);

        btnBeli.addActionListener(e -> prosesPembelian());

        btnKembali = new JButton("Kembali");
        btnKembali.setBounds(260, 150, 100, 30);
        add(btnKembali);

        btnKembali.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }

    private void loadBarang() {
        // Membersihkan item yang ada di combo box untuk menghindari duplikasi saat fungsi dipanggil ulang
        comboBarang.removeAllItems(); 
        comboBarang.addItem("Pilih Barang"); // Item default untuk panduan pengguna
        
        try (Connection conn = koneksi.getConnection();
             Statement stmt = conn.createStatement()) {
            // Query untuk mengambil data barang dari tabel
            String sql = "SELECT id_barang, nama_barang, harga FROM data_barang";
            ResultSet rs = stmt.executeQuery(sql);
    
            // Iterasi melalui hasil query dan tambahkan data ke combo box
            while (rs.next()) {
                int idBarang = rs.getInt("id_barang");
                String namaBarang = rs.getString("nama_barang");
                double harga = rs.getDouble("harga");
    
                // Format item: "ID - Nama Barang - Harga"
                comboBarang.addItem(idBarang + " - " + namaBarang + " - Rp " + harga);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data barang dari database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesPembelian() {
        try {
            String selectedItem = (String) comboBarang.getSelectedItem();
            int selectedId = Integer.parseInt(selectedItem.split(" - ")[0].split(" ")[0]);

            int jumlah = Integer.parseInt(txtJumlah.getText());

            // Dapatkan harga barang yang dipilih
            double harga = 0;
            try (Connection conn = koneksi.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT harga, stok FROM data_barang WHERE id_barang = ?")) {
                stmt.setInt(1, selectedId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    harga = rs.getDouble("harga");
                    int stok = rs.getInt("stok");

                    if(jumlah <= 0){
                        JOptionPane.showMessageDialog(this, "Inputan tidak valid, masukkan jumlah diatas 0 !");
                        return;
                    }
                    if (stok < jumlah) {
                        JOptionPane.showMessageDialog(this, "Stok tidak cukup!");
                        return;
                    }

                    // Update stok barang setelah pembelian
                    try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE data_barang SET stok = stok - ? WHERE id_barang = ?")) {
                        updateStmt.setInt(1, jumlah);
                        updateStmt.setInt(2, selectedId);
                        updateStmt.executeUpdate();
                    }

                    // Simpan transaksi ke tabel laporan_barang
                    try (PreparedStatement laporanStmt = conn.prepareStatement(
                            "INSERT INTO laporan_barang (id_barang, stok_barang, barang_masuk, barang_keluar, jumlah_transaksi) VALUES (?, ?, ?, ?, ?)")) {
                        laporanStmt.setInt(1, selectedId);
                        laporanStmt.setInt(2, stok - jumlah);
                        laporanStmt.setInt(3, 0); // Barang masuk
                        laporanStmt.setInt(4, jumlah); // Barang keluar
                        laporanStmt.setDouble(5, harga * jumlah);
                        laporanStmt.executeUpdate();
                    }

                    // Tampilkan total harga
                    txtTotalHarga.setText("Rp " + (harga * jumlah));
                    JOptionPane.showMessageDialog(this, "Pembelian berhasil!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal melakukan pembelian.");
        }
    }
}
