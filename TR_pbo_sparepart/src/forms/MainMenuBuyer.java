package forms;

import javax.swing.*;
import java.awt.*;

public class MainMenuBuyer extends JFrame {
    private JButton btnBeliBarang;
    private JButton btnHistoryPembelian;
    private JButton btnLogout;

    public MainMenuBuyer(String username) {
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
        headerPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Selamat Datang di Gudang Sparepart!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Panel untuk nama di kanan atas
        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(new Color(0, 102, 204)); // Warna sama dengan header
        profilePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setForeground(Color.WHITE);
        profilePanel.add(usernameLabel);

        headerPanel.add(profilePanel, BorderLayout.EAST);

        // Panel Utama
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridLayout(3, 1, 25, 25));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 250, 40, 250));

        // Tombol Beli Barang
        btnBeliBarang = createButton("  Beli Barang", new Color(0, 153, 76));
        mainPanel.add(btnBeliBarang);

        // Tombol History Pembelian
        btnHistoryPembelian = createButton("  History Pembelian", new Color(255, 102, 0));
        mainPanel.add(btnHistoryPembelian);

        // Tombol Logout
        btnLogout = createButton("  Logout", new Color(204, 0, 0));
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
     * Method untuk membuat tombol dengan desain warna.
     */
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Tambahkan kursor interaktif
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String username = "Default User"; // Contoh pengambilan username
            new MainMenuBuyer(username).setVisible(true);
        });
    }
}
