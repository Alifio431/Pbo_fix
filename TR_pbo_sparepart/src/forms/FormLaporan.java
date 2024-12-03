package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import utils.koneksi;

public class FormLaporan extends JFrame {
    private JTable tableLaporan;
    private DefaultTableModel tableModel;
    private JButton btnKembali;
    private static String username;


    public FormLaporan(String username) {
        setTitle("SpareMaster Application");

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel welcomeLabel = new JLabel("Laporan Barang");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        // Tabel dan ScrollPanel
        tableModel = new DefaultTableModel(new String[]{"ID Barang", "Stok Barang", "Barang Masuk", "Barang Keluar", "Jumlah Transaksi"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableLaporan = new JTable(tableModel);
        tableLaporan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableLaporan.getTableHeader().setBackground(new Color(0, 102, 204));
        tableLaporan.getTableHeader().setForeground(Color.WHITE);
        tableLaporan.setRowHeight(25);
        JScrollPane scrollPanel = new JScrollPane(tableLaporan);

        // Panel Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnKembali = createButton("Kembali", new Color(0, 102, 204));

        // Tambahkan aksi tombol
        btnKembali.addActionListener(e -> {
            new MainMenuAdmin(username).setVisible(true);
            dispose();
        });

        buttonPanel.add(btnKembali);

        // Panel Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.LIGHT_GRAY);
        JLabel footerLabel = new JLabel("© 2024 SpareMaster.co");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        footerPanel.add(footerLabel);

        // Layout Utama
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(scrollPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Load Data Laporan
        loadLaporan();
    }

    // Method Membuat Tombol dengan Warna
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        return button;
    }

    // Method Load Data Laporan
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
            JOptionPane.showMessageDialog(this, "Gagal memuat data laporan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormLaporan(username).setVisible(true));
    }
}
