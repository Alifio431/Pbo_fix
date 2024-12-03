package forms;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class NavbarForm extends JFrame {
    private JTextField txtSearch;
    private JComboBox<String> comboFilter;
    private JButton btnSearch, btnReset, btnBeli, btnKembali;
    private JTable tableResults, tableCart;
    private DefaultTableModel tableModelResults, tableModelCart;
    private JLabel lblTotalHarga;
    private static String username;

    public NavbarForm(String username) {
        this.username = username;
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadInitialData();
    }

    private void initializeComponents() {
        setTitle("Gudang Barang - Navbar");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Search components
        txtSearch = new JTextField(15);
        comboFilter = new JComboBox<>(new String[]{"Nama", "Merk", "Kategori", "Deskripsi"});
        btnSearch = new JButton("Search");
        btnReset = new JButton("Reset");

        // Results Table
        String[] resultColumns = {"ID Barang", "Nama", "Merk", "Kategori", "Harga", "Stok", "Deskripsi"};
        tableModelResults = new DefaultTableModel(resultColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableResults = new JTable(tableModelResults);
        configureTableColumns(tableResults, new int[]{50, 150, 100, 100, 80, 50, 200});

        // Cart Table
        String[] cartColumns = {"ID Barang", "Nama", "Merk", "Kategori", "Harga", "Quantity", "Action"};
        tableModelCart = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6;
            }
        };
        tableCart = new JTable(tableModelCart);
        configureTableColumns(tableCart, new int[]{50, 150, 100, 100, 80, 100, 80});
        tableCart.setRowHeight(40);

        // Buttons
        btnBeli = new JButton("Beli");
        btnKembali = new JButton("Kembali");
        
        // Total Harga Label
        lblTotalHarga = new JLabel("Total Harga: Rp 0.00");
    }

    private void configureTableColumns(JTable table, int[] columnWidths) {
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
    }

    private void setupLayout() {
        // Navbar Panel
        JPanel navbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbarPanel.add(new JLabel("Search:"));
        navbarPanel.add(txtSearch);
        navbarPanel.add(new JLabel("Filter by:"));
        navbarPanel.add(comboFilter);
        navbarPanel.add(btnSearch);
        navbarPanel.add(btnReset);

        // Results Table Scroll Pane
        JScrollPane scrollPaneResults = new JScrollPane(tableResults);

        // Cart Panel
        JPanel cartPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPaneCart = new JScrollPane(tableCart);
        cartPanel.add(new JLabel("Keranjang Belanja:"), BorderLayout.NORTH);
        
        // Bottom Panel with Total and Buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.add(lblTotalHarga);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnBeli);
        buttonPanel.add(btnKembali);

        bottomPanel.add(totalPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        cartPanel.add(scrollPaneCart, BorderLayout.CENTER);
        cartPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add to main frame
        add(navbarPanel, BorderLayout.NORTH);
        add(scrollPaneResults, BorderLayout.CENTER);
        add(cartPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        // Search button
        btnSearch.addActionListener(e -> performSearch());

        // Reset button
        btnReset.addActionListener(e -> resetSearch());

        // Results table click listener
        tableResults.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableResults.getSelectedRow();
                if (selectedRow != -1) {
                    addToCart(selectedRow);
                }
            }
        });

        // Beli button
        btnBeli.addActionListener(e -> processPurchase());

        // Kembali button
        btnKembali.addActionListener(e -> {
            dispose();
            new MainMenuBuyer(username).setVisible(true);
        });
    }

    private void performSearch() {
        String searchText = txtSearch.getText().trim();
        String filterColumn = getFilterColumn();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tr_pbo", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM data_barang WHERE " + filterColumn + " LIKE ?")) {

            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();

            tableModelResults.setRowCount(0);
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
                tableModelResults.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getFilterColumn() {
        switch (comboFilter.getSelectedItem().toString()) {
            case "Nama": return "nama_barang";
            case "Merk": return "merk";
            case "Kategori": return "kategori";
            case "Deskripsi": return "deskripsi_barang";
            default: return "id_barang";
        }
    }

    private void resetSearch() {
        txtSearch.setText("");
        comboFilter.setSelectedIndex(0);
        performSearch();
    }

    private void addToCart(int selectedRow) {
        Object[] rowData = new Object[6];
        for (int i = 0; i < 5; i++) {
            rowData[i] = tableModelResults.getValueAt(selectedRow, i);
        }
        rowData[5] = 1; // Default quantity

        // Check if item already exists in cart
        for (int i = 0; i < tableModelCart.getRowCount(); i++) {
            if (tableModelCart.getValueAt(i, 0).equals(rowData[0])) {
                JOptionPane.showMessageDialog(this, "Barang sudah ada di keranjang.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        tableModelCart.addRow(rowData);
        updateTotalHarga();
    }

    private void updateTotalHarga() {
        double total = 0.0;
        for (int i = 0; i < tableModelCart.getRowCount(); i++) {
            double harga = Double.parseDouble(tableModelCart.getValueAt(i, 4).toString());
            int quantity = Integer.parseInt(tableModelCart.getValueAt(i, 5).toString());
            total += harga * quantity;
        }
        lblTotalHarga.setText(String.format("Total Harga: Rp %.2f", total));
    }

    private void processPurchase() {
        try {
            insertToHistoryTransaksi();
            insertToLaporanBarang();
            
            JOptionPane.showMessageDialog(this, "Pembelian berhasil!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            new MainMenuBuyer(username).setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal melakukan pembelian.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertToHistoryTransaksi() throws SQLException {
        String query = "INSERT INTO history_transaksi (username, id_barang, nama_barang, merk, harga, quantity, total_harga) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tr_pbo", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            for (int i = 0; i < tableModelCart.getRowCount(); i++) {
                int idBarang = Integer.parseInt(tableModelCart.getValueAt(i, 0).toString());
                String namaBarang = tableModelCart.getValueAt(i, 1).toString();
                String merk = tableModelCart.getValueAt(i, 2).toString();
                double harga = Double.parseDouble(tableModelCart.getValueAt(i, 4).toString());
                int quantity = Integer.parseInt(tableModelCart.getValueAt(i, 5).toString());
                double totalHarga = harga * quantity;

                stmt.setString(1, username);
                stmt.setInt(2, idBarang);
                stmt.setString(3, namaBarang);
                stmt.setString(4, merk);
                stmt.setDouble(5, harga);
                stmt.setInt(6, quantity);
                stmt.setDouble(7, totalHarga);

                stmt.executeUpdate();
            }
        }
    }

    private void insertToLaporanBarang() throws SQLException {
        String query = "INSERT INTO laporan_barang (id_barang, stok_barang, barang_masuk, barang_keluar, jumlah_transaksi) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tr_pbo", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            for (int i = 0; i < tableModelCart.getRowCount(); i++) {
                int idBarang = Integer.parseInt(tableModelCart.getValueAt(i, 0).toString());
                int stokBarang = Integer.parseInt(tableModelCart.getValueAt(i, 5).toString());
                double harga = Double.parseDouble(tableModelCart.getValueAt(i, 4).toString());
                double jumlahTransaksi = harga * stokBarang;

                stmt.setInt(1, idBarang);
                stmt.setInt(2, stokBarang);
                stmt.setInt(3, stokBarang);
                stmt.setInt(4, 0);
                stmt.setDouble(5, jumlahTransaksi);

                stmt.executeUpdate();
            }
        }
    }

    private void loadInitialData() {
        performSearch(); // Load all data initially
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NavbarForm(username).setVisible(true);
        });
    }
}
