package forms;

import javax.swing.*;
import java.awt.*;

public class MainMenuAdmin extends JFrame {
    private JButton btnFormBarang;
    private JButton btnFormLaporan;
    private JButton btnLogout;
    private JButton btnViewDataBarang;

    public MainMenuAdmin(String username) {
        setTitle("Main Menu");

        // Ukuran frame relatif terhadap layar
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = screenSize.width / 2;
        int frameHeight = screenSize.height / 2;
        setSize(frameWidth, frameHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Izinkan pengguna mengubah ukuran jendela
        setResizable(true);
        setLocationRelativeTo(null);

        // Gunakan layout manager utama
        setLayout(new BorderLayout());

        // Panel Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204)); // Warna biru cerah
        headerPanel.setPreferredSize(new Dimension(frameWidth, frameHeight / 6)); // Tinggi header 1/6 frame
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JLabel welcomeLabel = new JLabel("Selamat Datang di Gudang Sparepart!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        // Panel Utama
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridLayout(2, 2, 25, 25));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Tombol Input Barang
        btnFormBarang = createButton("  Input Barang", "/icons/input.png", new Color(0, 153, 76));
        btnFormBarang.addActionListener(e -> {
            new FormBarang(username).setVisible(true);
            dispose();
        });
        mainPanel.add(btnFormBarang);

        // Tombol Laporan
        btnFormLaporan = createButton("  Laporan", "/icons/report.png", new Color(255, 102, 0));
        btnFormLaporan.addActionListener(e -> {
            new FormLaporan(username).setVisible(true);
            dispose();
        });
        mainPanel.add(btnFormLaporan);

        // Tombol Lihat Data Barang
        btnViewDataBarang = createButton("  Lihat Data Barang", "/icons/view.png", new Color(0, 153, 204));
        btnViewDataBarang.addActionListener(e -> {
            new ViewDataBarangForm(username).setVisible(true);
        });
        mainPanel.add(btnViewDataBarang);

        // Tombol Logout
        btnLogout = createButton("  Logout", "/icons/logout.png", new Color(204, 0, 0));
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
        mainPanel.add(btnLogout);

        // Panel Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.LIGHT_GRAY);
        footerPanel.setPreferredSize(new Dimension(frameWidth, frameHeight / 10)); // Tinggi footer 1/10 frame
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel footerLabel = new JLabel("© 2024 SpareMaster.co");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        footerLabel.setForeground(new Color(64, 64, 64));
        footerPanel.add(footerLabel);

        // Tambahkan panel ke frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Method untuk membuat tombol dengan ikon dan desain warna.
     */
    private JButton createButton(String text, String iconPath, Color bgColor) {
        JButton button = new JButton(text);
        button.setIcon(loadIcon(iconPath)); // Tambahkan ikon
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Tambahkan kursor interaktif
        return button;
    }

    /**
     * Method untuk memuat ikon dari path.
     */
    private ImageIcon loadIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH); // Ukuran ikon diperbesar
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String username = "Default User"; // Contoh pengambilan username
            new MainMenuBuyer(username).setVisible(true);
        });
    }
}
