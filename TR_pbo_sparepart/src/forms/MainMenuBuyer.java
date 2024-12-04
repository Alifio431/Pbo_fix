package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainMenuBuyer extends JFrame {
    private JButton btnBeliBarang;
    private JButton btnHistoryPembelian;
    private JButton btnLogout;
    private JLabel usernameLabel;

    public MainMenuBuyer(String username) {
        setTitle("SpareMaster Application");

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
        headerPanel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Selamat Datang di Gudang Sparepart!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(50, 20, 600, 50);
        headerPanel.add(welcomeLabel);

        // Label untuk nama di kanan atas
        usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setForeground(Color.WHITE);
        headerPanel.add(usernameLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Panel Utama
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridLayout(3, 1, 25, 25));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Tombol Beli Barang
        btnBeliBarang = createButton("  Beli Barang","/icons/cart.png", new Color(0, 153, 76));
        btnBeliBarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FromBeliBarang(username).setVisible(true);
                dispose();
            }
        });
        mainPanel.add(btnBeliBarang);

        // Tombol History Pembelian
        btnHistoryPembelian = createButton("  History Pembelian", "/icons/history.png", new Color(255, 102, 0));
        btnHistoryPembelian.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HistoryPembelian(username).setVisible(true);
                dispose();
            }
        });
        mainPanel.add(btnHistoryPembelian);

        // Tombol Logout
        btnLogout = createButton("  Logout", "/icons/logout.png", new Color(204, 0, 0));
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginForm().setVisible(true);
                dispose();
            }
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

        // Component listener to adjust the position of usernameLabel on window resize
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int frameWidth = getWidth();
                int labelWidth = usernameLabel.getPreferredSize().width;
                usernameLabel.setBounds(frameWidth - labelWidth - 50, 20, labelWidth, 50); // 50px from the right side
            }
        });
    }

    /**
     * Method untuk membuat tombol dengan desain warna.
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
