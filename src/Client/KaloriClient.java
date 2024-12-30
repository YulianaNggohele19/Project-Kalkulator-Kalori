package Client;

import shared.KaloriRMI;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Calendar;
import java.util.Date;

public class KaloriClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new KaloriClient().createAndShowGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void createAndShowGUI() throws Exception {
        // Meminta alamat IP server dan port dalam satu input dialog
        String serverInput = JOptionPane.showInputDialog("Masukkan IP Server dan Port (format: IP:port):");

        // Memeriksa apakah input null atau kosong
        if (serverInput == null || serverInput.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Input tidak valid. Aplikasi akan ditutup.");
            return;
        }

        // Memisahkan IP dan port dari input
        String[] parts = serverInput.split(":");
        if (parts.length != 2) {
            JOptionPane.showMessageDialog(null, "Format input tidak valid. Harap gunakan format IP:port.");
            return;
        }

        String serverIP = parts[0];
        int port = 1099; // Default port
        try {
            port = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Port tidak valid, menggunakan port default 1099.");
        }

        // Menghubungkan ke RMI Registry di alamat IP dan port server
        Registry registry = LocateRegistry.getRegistry(serverIP, port);
        KaloriRMI service = (KaloriRMI) registry.lookup("KaloriService");

        // Frame utama
        JFrame frame = new JFrame("Kalkulator Kebutuhan Kalori Harian");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Panel utama dengan layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("Kalkulator Kebutuhan Kalori", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(51, 102, 255));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        JLabel lblTanggalLahir = new JLabel("Tanggal Lahir:");
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        JLabel lblJenisKelamin = new JLabel("Jenis Kelamin:");
        JComboBox<String> cmbJenisKelamin = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});

        JLabel lblTinggi = new JLabel("Tinggi Badan (cm):");
        JTextField txtTinggi = new JTextField();

        JLabel lblBerat = new JLabel("Berat Badan (kg):");
        JTextField txtBerat = new JTextField();

        JLabel lblAktivitas = new JLabel("Tingkat Aktivitas Fisik:");
        JComboBox<String> cmbAktivitas = new JComboBox<>(new String[]{
                "Sangat Jarang Beraktivitas", 
                "Jarang Beraktivitas", 
                "Terkadang Beraktivitas", 
                "Sering Beraktivitas", 
                "Sangat Sering Beraktivitas"
        });

        JLabel lblHasil = new JLabel("Hasil:");
        JTextField txtHasil = new JTextField();
        txtHasil.setEditable(false);

        formPanel.add(lblTanggalLahir);
        formPanel.add(dateChooser);
        formPanel.add(lblJenisKelamin);
        formPanel.add(cmbJenisKelamin);
        formPanel.add(lblTinggi);
        formPanel.add(txtTinggi);
        formPanel.add(lblBerat);
        formPanel.add(txtBerat);
        formPanel.add(lblAktivitas);
        formPanel.add(cmbAktivitas);
        formPanel.add(lblHasil);
        formPanel.add(txtHasil);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Tombol Hitung
        JButton btnHitung = new JButton("Hitung Kalori");
        btnHitung.setBackground(new Color(51, 153, 255));
        btnHitung.setForeground(Color.WHITE);
        btnHitung.setFont(new Font("Arial", Font.BOLD, 16));
        btnHitung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Mengambil tahun lahir dari dateChooser
                    Date tanggalLahir = dateChooser.getDate();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(tanggalLahir);
                    int tahunLahir = calendar.get(Calendar.YEAR);
                    int usia = 2024 - tahunLahir;

                    String jenisKelamin = (String) cmbJenisKelamin.getSelectedItem();
                    double tinggi = Double.parseDouble(txtTinggi.getText());
                    double berat = Double.parseDouble(txtBerat.getText());
                    int tingkatAktivitas = cmbAktivitas.getSelectedIndex();

                    // Menghitung kalori berdasarkan input
                    double kalori = service.hitungKalori(jenisKelamin, usia, tinggi, berat, tingkatAktivitas);
                    txtHasil.setText(String.format("%.2f kalori", kalori));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Input tidak valid: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mainPanel.add(btnHitung, BorderLayout.SOUTH);

        // Menampilkan frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
