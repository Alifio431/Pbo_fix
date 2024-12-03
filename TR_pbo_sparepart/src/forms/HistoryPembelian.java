package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class HistoryPembelian extends JFrame {
    private JTable tableTransactions;
    private DefaultTableModel tableModel;
    private static String username;

    public HistoryPembelian(String username) {
        this.username = username;
        setTitle("Transaction History");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for title
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Transaction History"));

        // Table Model for displaying transaction history
        tableModel = new DefaultTableModel(new String[]{
                "Id", "Username", "Id Barang", "Nama Barang", "Merk", "Harga", "Quantity", "Total Harga"
        }, 0);
        
        tableTransactions = new JTable(tableModel);

        // Adjust column widths
        tableTransactions.getColumnModel().getColumn(0).setPreferredWidth(100);  
        tableTransactions.getColumnModel().getColumn(1).setPreferredWidth(100);  
        tableTransactions.getColumnModel().getColumn(2).setPreferredWidth(150);  
        tableTransactions.getColumnModel().getColumn(3).setPreferredWidth(100); 
        tableTransactions.getColumnModel().getColumn(4).setPreferredWidth(80);  
        tableTransactions.getColumnModel().getColumn(5).setPreferredWidth(80);   
        tableTransactions.getColumnModel().getColumn(6).setPreferredWidth(100); 
        tableTransactions.getColumnModel().getColumn(7).setPreferredWidth(150);

        // ScrollPane for the transactions table
        JScrollPane scrollPane = new JScrollPane(tableTransactions);

        // Add components to the frame
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load the user's transaction history
        loadTransactionHistory();

        setLocationRelativeTo(null);
    }

    private void loadTransactionHistory() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tr_pbo", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM history_transaksi WHERE username = ?")) {

            stmt.setString(1, username);  // Fetch transactions for the logged-in user
            ResultSet rs = stmt.executeQuery();

            // Clear the existing rows in the table
            tableModel.setRowCount(0);

            // Add the retrieved transaction records to the table
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id_penjualan"),
                        rs.getString("username"),
                        rs.getInt("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("merk"),
                        rs.getDouble("harga"),
                        rs.getInt("quantity"),
                        rs.getDouble("total_harga")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading transaction history.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HistoryPembelian(username).setVisible(true);
        });
    }
}
