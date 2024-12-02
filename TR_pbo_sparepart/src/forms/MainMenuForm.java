//admin
//main menu 
package forms;

import javax.swing.*;
import java.awt.event.*;

public class MainMenuForm extends JFrame {
    private JButton btnFormBarang;
    private JButton btnFormLaporan;
    private JButton btnLogout;
    private JButton btnViewDataBarang;  // Tambahkan tombol baru untuk melihat data barang

    public MainMenuForm() {
        setTitle("Main Menu");
        setSize(400, 400);  // Sesuaikan ukuran frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel welcomeLabel = new JLabel("Selamat Datang di Gudang Sparepart!");
        welcomeLabel.setBounds(100, 20, 250, 25);
        add(welcomeLabel);

        btnFormBarang = new JButton("Input Barang");
        btnFormBarang.setBounds(100, 70, 200, 30);
        add(btnFormBarang);

        btnFormBarang.addActionListener(e -> {
            new FormBarang().setVisible(true);
            dispose();
        });

        btnFormLaporan = new JButton("Laporan");
        btnFormLaporan.setBounds(100, 120, 200, 30);
        add(btnFormLaporan);

        btnFormLaporan.addActionListener(e -> {
            new FormLaporan().setVisible(true);
            dispose();
        });

        btnViewDataBarang = new JButton("Lihat Data Barang");
        btnViewDataBarang.setBounds(100, 170, 200, 30);
        add(btnViewDataBarang);

        btnViewDataBarang.addActionListener(e -> {
            new ViewDataBarangForm().setVisible(true);  // Menampilkan form untuk melihat data barang
        });

        btnLogout = new JButton("Logout");
        btnLogout.setBounds(100, 220, 200, 30);
        add(btnLogout);

        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        // Tombol baru untuk menampilkan data barang
    }
}
