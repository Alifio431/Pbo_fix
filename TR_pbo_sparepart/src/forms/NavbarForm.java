package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class NavbarForm extends JFrame {
    private JTextField txtSearch;
    private JComboBox<String> comboFilter;
    private JButton btnSearch, btnReset;
    private JTable tableResults;
    private DefaultTableModel tableModel;

    public NavbarForm() {
        setTitle("Gudang Barang - Navbar");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel untuk navbar (search bar + filter)
        JPanel navbarPanel = new JPanel();
        navbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel lblSearch = new JLabel("Search:");
        txtSearch = new JTextField(15);

        JLabel lblFilter = new JLabel("Filter by:");
        comboFilter = new JComboBox<>(new String[] {"Nama", "Merk", "Kategori", "Deskripsi"});

        btnSearch = new JButton("Search");
        btnReset = new JButton("Reset");

        navbarPanel.add(lblSearch);
        navbarPanel.add(txtSearch);
        navbarPanel.add(lblFilter);
        navbarPanel.add(comboFilter);
        navbarPanel.add(btnSearch);
        navbarPanel.add(btnReset);

        // Tabel untuk menampilkan hasil pencarian
        tableModel = new DefaultTableModel(new String[] {"ID Barang", "Nama", "Merk", "Kategori", "Harga", "Stok", "Deskripsi"}, 0);
        tableResults = new JTable(tableModel);

        // ScrollPane untuk tabel
        JScrollPane scrollPane = new JScrollPane(tableResults);

        add(navbarPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event tombol search
        btnSearch.addActionListener(e -> performSearch());

        // Event tombol reset
        btnReset.addActionListener(e -> resetSearch());

        // Load data awal (tampilkan semua data tanpa filter)
        performSearch();
    }

    private void performSearch() {
        String searchText = txtSearch.getText().trim();
        String filterBy = comboFilter.getSelectedItem().toString(); // Nama, Merk, etc.

        // Mapkan nama kolom filter ke nama kolom di database
        switch (filterBy) {
            case "Nama":
                filterBy = "nama_barang";
                break;
            case "Merk":
                filterBy = "merk";
                break;
            case "Kategori":
                filterBy = "kategori";
                break;
            case "Deskripsi":
                filterBy = "deskripsi_barang";
                break;
            default:
                filterBy = "id_barang"; // Default jika ada nilai tidak valid
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tr_pbo", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM data_barang WHERE " + filterBy + " LIKE ?")) {

            stmt.setString(1, "%" + searchText + "%"); // Masukkan nilai pencarian
            ResultSet rs = stmt.executeQuery();

            // Bersihkan data tabel
            tableModel.setRowCount(0);

            // Tambahkan data hasil pencarian ke tabel
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("merk"),
                    rs.getString("kategori"),
                    rs.getDouble("harga"),
                    rs.getInt("stok"),
                    rs.getString("deskripsi_barang")
                };
                tableModel.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetSearch() {
        txtSearch.setText("");
        comboFilter.setSelectedIndex(0);

        // Bersihkan data tabel dan tampilkan semua data
        performSearch();
    }

    public static void main(String[] args) {
        new NavbarForm().setVisible(true);
    }
}
