package Server;

import shared.KaloriRMI;

import java.rmi.RemoteException;

public class KaloriCalculatorImpl implements KaloriRMI {
    @Override
    public double hitungKalori(String jenisKelamin, int usia, double tinggi, double berat, int tingkatAktivitas) throws RemoteException {
        // Rumus untuk menghitung kalori
        double bmr = 0;
        if (jenisKelamin.equals("Laki-laki")) {
            bmr = 66.5 + (13.75 * berat) + (5.003 * tinggi) - (6.75 * usia);
        } else if (jenisKelamin.equals("Perempuan")) {
            bmr = 655 + (9.563 * berat) + (1.850 * tinggi) - (4.676 * usia);
        }

        // Menyesuaikan dengan tingkat aktivitas
        double faktorAktivitas = 1.2;
        switch (tingkatAktivitas) {
            case 1:
                faktorAktivitas = 1.2; // Tidak aktif
                break;
            case 2:
                faktorAktivitas = 1.375; // Sedikit aktif
                break;
            case 3:
                faktorAktivitas = 1.55; // Aktif
                break;
            case 4:
                faktorAktivitas = 1.725; // Sangat aktif
                break;
        }

        // Menghitung kebutuhan kalori harian
        return bmr * faktorAktivitas;
    }
}
