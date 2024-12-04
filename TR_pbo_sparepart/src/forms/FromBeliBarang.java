package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellEditor;

public class FromBeliBarang extends JFrame {
    private JPanel cartPanel;
    private JTextField txtSearch;
    private JComboBox<String> comboFilter;
    private JButton btnSearch, btnReset;
    private JTable tableResults, tableCart;
    private DefaultTableModel cartModel,tableModelCart;
    private JLabel lblTotalHarga;
    private static String username;
    private JButton btnBeli;

    public FromBeliBarang(String username) {
        setTitle("SpareMaster Application");
        this.username = username;
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel untuk navbar (search bar + filter)
        JPanel navbarPanel = new JPanel();
        navbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel lblSearch = new JLabel("Search:");
        txtSearch = new JTextField(15);

        JLabel lblFilter = new JLabel("Filter by:");
        comboFilter = new JComboBox<>(new String[]{"Nama", "Merk", "Kategori", "Deskripsi"});

        btnSearch = new JButton("Search");
        btnReset = new JButton("Reset");

        navbarPanel.add(lblSearch);
        navbarPanel.add(txtSearch);
        navbarPanel.add(lblFilter);
        navbarPanel.add(comboFilter);
        navbarPanel.add(btnSearch);
        navbarPanel.add(btnReset);

        // Tabel untuk menampilkan hasil pencarian
        String[] cartColumns = {"ID Barang", "Nama", "Merk", "Kategori", "Harga", "Quantity", "Action"};
        tableModelCart = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat tabel tidak dapat diedit
            }
        };
        tableResults = new JTable(tableModelCart);

        // Adjust column widths for results table
        tableResults.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID Barang
        tableResults.getColumnModel().getColumn(1).setPreferredWidth(150); // Nama
        tableResults.getColumnModel().getColumn(2).setPreferredWidth(100); // Merk
        tableResults.getColumnModel().getColumn(3).setPreferredWidth(100); // Kategori
        tableResults.getColumnModel().getColumn(4).setPreferredWidth(80);  // Harga
        tableResults.getColumnModel().getColumn(5).setPreferredWidth(50);  // Stok
        tableResults.getColumnModel().getColumn(6).setPreferredWidth(200); // Deskripsi

        // ScrollPane untuk tabel utama
        JScrollPane scrollPaneResults = new JScrollPane(tableResults);

        // Tabel untuk menampilkan barang yang diklik (keranjang belanja)
        cartModel = new DefaultTableModel(new String[]{"ID Barang", "Nama", "Merk", "Kategori", "Harga", "Quantity", "Action"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6; // Kolom Quantity dan Action dapat diedit
            }
        };
        tableCart = new JTable(cartModel);

        // Adjust column widths for cart table
        tableCart.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID Barang
        tableCart.getColumnModel().getColumn(1).setPreferredWidth(150); // Nama
        tableCart.getColumnModel().getColumn(2).setPreferredWidth(100); // Merk
        tableCart.getColumnModel().getColumn(3).setPreferredWidth(100); // Kategori
        tableCart.getColumnModel().getColumn(4).setPreferredWidth(80);  // Harga
        tableCart.getColumnModel().getColumn(5).setPreferredWidth(100); // Quantity
        tableCart.getColumnModel().getColumn(6).setPreferredWidth(80);  // Action

        // Increase row height
        tableCart.setRowHeight(40);
        tableResults.setRowHeight(25);

        // Custom renderer dan editor untuk kolom Action
        tableCart.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tableCart.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());

        // Tambahkan renderer/editor untuk kolom Quantity
        tableCart.getColumnModel().getColumn(5).setCellRenderer(new QuantityRenderer());
        tableCart.getColumnModel().getColumn(5).setCellEditor(new QuantityEditor());

        // ScrollPane untuk tabel keranjang
        JScrollPane scrollPaneCart = new JScrollPane(tableCart);

        // Panel bawah untuk label keranjang dan total harga
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalHarga = new JLabel("Total Harga: Rp 0");
        bottomPanel.add(lblTotalHarga);

        JLabel lblCart = new JLabel("Keranjang Belanja:");
        cartPanel.add(lblCart, BorderLayout.NORTH);
        cartPanel.add(scrollPaneCart, BorderLayout.CENTER);
        cartPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(navbarPanel, BorderLayout.NORTH);
        add(scrollPaneResults, BorderLayout.CENTER);
        add(cartPanel, BorderLayout.SOUTH);

        JButton btnKembali = new JButton("Kembali");
        navbarPanel.add(btnKembali);

        // Add action listener for the Kembali button
        btnKembali.addActionListener(e -> {
            dispose();
            new MainMenuBuyer(username).setVisible(true);
        });

        btnBeli = new JButton("Beli");
        navbarPanel.add(btnBeli);

        // Event tombol search
        btnSearch.addActionListener(e -> performSearch());

        // Event tombol reset
        btnReset.addActionListener(e -> resetSearch());



        // Event klik pada tabel utama untuk menambah ke keranjang
        tableResults.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableResults.getSelectedRow();
                if (selectedRow != -1) { // Pastikan ada baris yang dipilih
                    addToCart(selectedRow);
                }
            }
        });

        btnBeli.addActionListener(e -> {
            try {
                // Insert cart items into the database
                insertToHistoryTransaksi();
                insertToLaporanBarang();

                // Show success message
                JOptionPane.showMessageDialog(this, "Pembelian berhasil! Menunggu untuk melanjutkan.", "Pembelian Sukses", JOptionPane.INFORMATION_MESSAGE);

                // After the alert is closed, navigate to MainMenuBuyer
                dispose();  // Close the current NavbarForm
                new MainMenuBuyer(username).setVisible(true);  // Open MainMenuBuyer
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saat melakukan pembelian.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        // Load data awal (tampilkan semua data tanpa filter)
        performSearch();
    }
    private void insertToHistoryTransaksi() throws SQLException {
        String insertQuery = "INSERT INTO history_transaksi (username, id_barang, nama_barang, merk, harga, quantity, total_harga) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tr_pbo", "root", "");
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            // Loop through the cart items and insert each one
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                int idBarang = (int) cartModel.getValueAt(i, 0);
                String namaBarang = (String) cartModel.getValueAt(i, 1);
                String merk = (String) cartModel.getValueAt(i, 2);
                double harga = (double) cartModel.getValueAt(i, 4);
                int quantity = (int) cartModel.getValueAt(i, 5);
                double totalHarga = harga * quantity; // Calculate total harga based on quantity

                // Set values for the prepared statement
                stmt.setString(1, username);  // Insert the current user's username
                stmt.setInt(2, idBarang);
                stmt.setString(3, namaBarang);
                stmt.setString(4, merk);
                stmt.setDouble(5, harga);
                stmt.setInt(6, quantity);  // Insert quantity
                stmt.setDouble(7, totalHarga);  // Insert the total price calculated

                // Execute the insert for each cart item
                stmt.executeUpdate();
            }

        }
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
            tableModelCart.setRowCount(0);

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
                tableModelCart.addRow(row);
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

    private void addToCart(int selectedRow) {
        Object idBarang = tableModelCart.getValueAt(selectedRow, 0);
        Object namaBarang = tableModelCart.getValueAt(selectedRow, 1);
        Object merk = tableModelCart.getValueAt(selectedRow, 2);
        Object kategori = tableModelCart.getValueAt(selectedRow, 3);
        Object harga = tableModelCart.getValueAt(selectedRow, 4);

        // Validasi jika ID Barang sudah ada di keranjang
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            if (cartModel.getValueAt(i, 0).equals(idBarang)) {
                JOptionPane.showMessageDialog(this, "Barang sudah ada di keranjang.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        // Tambahkan ke keranjang
        cartModel.addRow(new Object[]{idBarang, namaBarang, merk, kategori, harga, 1, "Delete"});
        updateTotalHarga();

        SwingUtilities.invokeLater(() -> {
            tableCart.revalidate();
            tableCart.repaint();
        });
    }

    private void updateTotalHarga() {
        double total = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            // Use Double.parseDouble to properly handle decimal prices
            double harga = Double.parseDouble(cartModel.getValueAt(i, 4).toString());
            int quantity = Integer.parseInt(cartModel.getValueAt(i, 5).toString());
            total += harga * quantity;
        }
        // Format the total to display with two decimal places
        lblTotalHarga.setText(String.format("Total Harga: Rp %.2f", total));
    }

    // Custom renderer untuk tombol "Delete"
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Delete");
            return this;
        }
    }

    // Custom editor untuk tombol "Delete"
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private int row;
        private JTable table;

        public ButtonEditor() {
            button = new JButton("Delete");
            button.addActionListener(e -> {
                // Use the current row from the table, not a stored row value
                int selectedRow = table.convertRowIndexToModel(row);
                deleteFromCart(selectedRow);
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Delete";
        }
    }

    private void deleteFromCart(int row) {
        // Check if row is valid before removing
        if (row >= 0 && row < cartModel.getRowCount()) {
            cartModel.removeRow(row);
            updateTotalHarga();

            SwingUtilities.invokeLater(() -> {
                tableCart.revalidate();
                tableCart.repaint();
            });
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FromBeliBarang(username).setVisible(true);
        });
    }

    private void insertToLaporanBarang() throws SQLException { 
        String insertQuery = "INSERT INTO laporan_barang (id_barang, stok_barang, barang_masuk, barang_keluar, jumlah_transaksi) VALUES (?, ?, 0, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tr_pbo", "root", "")) {
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                int idBarang = Integer.parseInt(cartModel.getValueAt(i, 0).toString());
                int quantity = Integer.parseInt(cartModel.getValueAt(i, 5).toString());
                double harga = Double.parseDouble(cartModel.getValueAt(i, 4).toString());
    
                // Calculate the total transaction amount
                double jumlahTransaksi = harga * quantity;
    
                // Retrieve current stok from data_barang
                int currentStok = getCurrentStok(conn, idBarang);
    
                // Insert a new record into laporan_barang
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, idBarang);
                    insertStmt.setInt(2, currentStok - quantity);  // Current stock after transaction
                    insertStmt.setInt(3, quantity);  // barang_keluar
                    insertStmt.setDouble(4, jumlahTransaksi);
                    insertStmt.executeUpdate();
                }
    
                // Update stok in data_barang
                updateStokBarang(conn, idBarang, quantity);
            }
        }
    }
    
    // Helper method to get current stok from data_barang
    private int getCurrentStok(Connection conn, int idBarang) throws SQLException {
        String query = "SELECT stok FROM data_barang WHERE id_barang = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idBarang);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("stok") : 0;
        }
    }
    
    // Helper method to update stok in data_barang
    private void updateStokBarang(Connection conn, int idBarang, int quantity) throws SQLException {
        String updateStokQuery = "UPDATE data_barang SET stok = stok - ? WHERE id_barang = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateStokQuery)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, idBarang);
            stmt.executeUpdate();
        }
    }
    // Custom renderer untuk kolom Quantity
    class QuantityRenderer extends JPanel implements TableCellRenderer {
        private JButton btnMinus, btnPlus;
        private JLabel lblQuantity;

        public QuantityRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            btnMinus = new JButton("<");
            btnPlus = new JButton(">");
            lblQuantity = new JLabel("1");

            // Set button and label sizes
            btnMinus.setPreferredSize(new Dimension(50, 25));
            btnPlus.setPreferredSize(new Dimension(50, 25));
            lblQuantity.setPreferredSize(new Dimension(30, 25));

            add(btnMinus);
            add(lblQuantity);
            add(btnPlus);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            lblQuantity.setText(value.toString());
            return this;
        }
    }

    // Custom editor untuk kolom Quantity
    class QuantityEditor extends DefaultCellEditor {
        private JButton btnMinus, btnPlus;
        private JLabel lblQuantity;
        private JPanel panel;
        private int rowToEdit;

        public QuantityEditor() {
            super(new JTextField());
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            btnMinus = new JButton("<");
            btnPlus = new JButton(">");
            lblQuantity = new JLabel("1");

            // Set button and label sizes
            btnMinus.setPreferredSize(new Dimension(50, 25));
            btnPlus.setPreferredSize(new Dimension(50, 25));
            lblQuantity.setPreferredSize(new Dimension(30, 25));

            btnMinus.addActionListener(e -> updateQuantity(-1));
            btnPlus.addActionListener(e -> updateQuantity(1));

            panel.add(btnMinus);
            panel.add(lblQuantity);
            panel.add(btnPlus);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            lblQuantity.setText(value.toString());
            rowToEdit = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return Integer.parseInt(lblQuantity.getText());
        }

        private void updateQuantity(int delta) {
            int currentQuantity = Integer.parseInt(lblQuantity.getText());
            int newQuantity = Math.max(1, currentQuantity + delta); // Minimal 1
            lblQuantity.setText(String.valueOf(newQuantity));
            cartModel.setValueAt(newQuantity, rowToEdit, 5); // Update Quantity di model
            updateTotalHarga();

            SwingUtilities.invokeLater(() -> {
                tableCart.revalidate();
                tableCart.repaint();
            });
        }

    }
}
