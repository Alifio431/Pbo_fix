//admin
//form input barang
package forms;

import javax.swing.*;
import java.awt.event.*;
import utils.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class FormBarang extends JFrame {
    private JTextField txtNamaBarang, txtMerk, txtKategori, txtHarga, txtStok;
    private JTextArea txtDeskripsi;
    private JButton btnSimpan, btnKembali;

    public FormBarang() {
        setTitle("Form Input Barang");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblNamaBarang = new JLabel("Nama Barang:");
        lblNamaBarang.setBounds(20, 30, 100, 25);
        add(lblNamaBarang);

        txtNamaBarang = new JTextField();
        txtNamaBarang.setBounds(150, 30, 200, 25);
        add(txtNamaBarang);

        JLabel lblMerk = new JLabel("Merk:");
        lblMerk.setBounds(20, 70, 100, 25);
        add(lblMerk);

        txtMerk = new JTextField();
        txtMerk.setBounds(150, 70, 200, 25);
        add(txtMerk);

        JLabel lblKategori = new JLabel("Kategori:");
        lblKategori.setBounds(20, 110, 100, 25);
        add(lblKategori);

        txtKategori = new JTextField();
        txtKategori.setBounds(150, 110, 200, 25);
        add(txtKategori);

        JLabel lblHarga = new JLabel("Harga:");
        lblHarga.setBounds(20, 150, 100, 25);
        add(lblHarga);

        txtHarga = new JTextField();
        txtHarga.setBounds(150, 150, 200, 25);
        add(txtHarga);

        JLabel lblStok = new JLabel("Stok:");
        lblStok.setBounds(20, 190, 100, 25);
        add(lblStok);

        txtStok = new JTextField();
        txtStok.setBounds(150, 190, 200, 25);
        add(txtStok);

        JLabel lblDeskripsi = new JLabel("Deskripsi:");
        lblDeskripsi.setBounds(20, 230, 100, 25);
        add(lblDeskripsi);

        txtDeskripsi = new JTextArea();
        txtDeskripsi.setBounds(150, 230, 200, 60);
        add(txtDeskripsi);

        btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(150, 310, 100, 30);
        add(btnSimpan);

        btnSimpan.addActionListener(e -> simpanBarang());

        btnKembali = new JButton("Kembali");
        btnKembali.setBounds(260, 310, 100, 30);
        add(btnKembali);

        btnKembali.addActionListener(e -> {
            new MainMenuForm().setVisible(true);
            dispose();
        });
    }

    private void simpanBarang() {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "INSERT INTO data_barang (nama_barang, merk, kategori, harga, stok, deskripsi_barang) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNamaBarang.getText());
            stmt.setString(2, txtMerk.getText());
            stmt.setString(3, txtKategori.getText());
            stmt.setDouble(4, Double.parseDouble(txtHarga.getText()));
            stmt.setInt(5, Integer.parseInt(txtStok.getText()));
            stmt.setString(6, txtDeskripsi.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Barang berhasil disimpan!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan barang.");
        }
    }
}
