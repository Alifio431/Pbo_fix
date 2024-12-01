package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewDataBarangForm extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;

    public ViewDataBarangForm() {
        setTitle("Data Barang");
        setSize(600, 400);  // Ukuran frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Membuat tabel dengan model data
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID Barang");
        tableModel.addColumn("Nama Barang");
        tableModel.addColumn("Merk");
        tableModel.addColumn("Kategori");
        tableModel.addColumn("Harga");
        tableModel.addColumn("Stok");
        tableModel.addColumn("Deskripsi Barang");

        // Mengambil data dari database dan menampilkannya
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tr_pbo", "root", "")) {
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

        // Menampilkan tabel di jendela
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
