package Server;

import shared.KaloriRMI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerKalori {
    public static void main(String[] args) {
        try {
            // Membuat objek dari layanan KaloriCalculatorImpl
            KaloriRMI kaloriService = new KaloriCalculatorImpl();

            // Menyediakan layanan pada registry
            KaloriRMI stub = (KaloriRMI) UnicastRemoteObject.exportObject(kaloriService, 0);

            // Menentukan port yang digunakan
            int port = 1088;

            // Membuat registry RMI pada port yang ditentukan
            Registry registry = LocateRegistry.createRegistry(port);

            // Mendaftarkan layanan dengan nama "KaloriService"
            registry.rebind("KaloriService", stub);

            // Mendapatkan alamat IP server
            InetAddress ipAddress = InetAddress.getLocalHost();
            String serverIP = ipAddress.getHostAddress();

            // Menampilkan alamat IP server dan port yang digunakan
            System.out.println("Server Kalori telah berjalan pada port " + port + "...");
            System.out.println("Alamat IP Server: " + serverIP);
            System.out.println("Port yang digunakan: " + port);

        } catch (UnknownHostException e) {
            System.out.println("Gagal mendapatkan alamat IP server.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
