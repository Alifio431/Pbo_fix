//admin
//digunakan untuk melihat data laporan
package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import utils.koneksi;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FormLaporan extends JFrame {
    private JTable tableLaporan;
    private DefaultTableModel tableModel;
    private JButton btnKembali;

    public FormLaporan() {
        setTitle("Laporan Barang");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID Barang");
        tableModel.addColumn("Stok Barang");
        tableModel.addColumn("Barang Masuk");
        tableModel.addColumn("Barang Keluar");
        tableModel.addColumn("Jumlah Transaksi");

        tableLaporan = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableLaporan);
        scrollPane.setBounds(20, 20, 540, 250);
        add(scrollPane);

        btnKembali = new JButton("Kembali");
        btnKembali.setBounds(250, 300, 100, 30);
        add(btnKembali);

        btnKembali.addActionListener(e -> {
            new MainMenuForm().setVisible(true);
            dispose();
        });

        loadLaporan();
    }

    private void loadLaporan() {
        try (Connection conn = koneksi.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM laporan_barang";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_barang"),
                        rs.getInt("stok_barang"),
                        rs.getInt("barang_masuk"),
                        rs.getInt("barang_keluar"),
                        rs.getDouble("jumlah_transaksi")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
