package udp;

import java.net.*;
import java.util.Scanner;

public class UDPClient {
	public static InetAddress ip;
	public static void main(String[] args) {
		try {
			ip = InetAddress.getByName("localhost");
			//while(true){
			Scanner in = new Scanner(System.in);
			
			System.out.println("Seleccione la cantidad de clientes");
			int cantidad = in.nextInt();
			
			System.out.println("Seleccione el archivo:\n1-100MB\n2-250MB");
			int archivo = in.nextInt();
			
			System.out.println("Se va a inciar la conexion con cada cliente e inmediatamente se comenzara la transferencia del archivo seleccionado");
			System.out.println("Ingrese 1 para continuar");
			in.nextInt();
			
			String s = cantidad+"-"+archivo;
			prepararServidor(s);
			
			DatagramSocket ds = new DatagramSocket();
			UDPClientThread[] clientes = new UDPClientThread[cantidad];
			for(int i = 0; i < cantidad; i++) {
				clientes[i] = new UDPClientThread(i+"", ip, ds, archivo, cantidad);
			}
			for(int i = 0; i < cantidad; i++) {
				clientes[i].start();
			}
			//}
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void prepararServidor(String s) {
		byte[] buff = s.getBytes();
		System.out.println("El tamano del buffer es: " + buff.length);
		try {
			DatagramSocket ds = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(buff, buff.length, ip, 9999);
			ds.send(dp);
			ds.receive(dp);
			ds.close();
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
