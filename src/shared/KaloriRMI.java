package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface KaloriRMI extends Remote {
    double hitungKalori(String jenisKelamin, int usia, double tinggi, double berat, int tingkatAktivitas) throws RemoteException;
}
