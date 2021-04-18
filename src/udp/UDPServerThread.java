package udp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;

public class UDPServerThread extends Thread{

	public static final String DIR = "C:\\Users\\Nicolás\\Documents\\ISI3204 lab 3 2\\src\\data\\";
	public static final String MB100 = "100MB.test";
	public static final String MB250 = "250MB.test";
	
	private DatagramSocket ds;
	private DatagramPacket dp;
	private byte[] buf;
	private String id;
	private int archivo;
	public UDPServerThread(int iD, int fil) {
		try {
			ds = new DatagramSocket(6969+iD);
			buf = new byte[12];
			dp = new DatagramPacket(buf, buf.length);
			id = iD+"";
			archivo = fil;
			System.out.println("Server-"+id+" creado");
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void run() {
		try {
			ds.receive(dp);
			String received = new String(dp.getData(), 0, dp.getLength());
			System.out.println("El servidor-"+id+" recibio el mensaje: "+received);
			dp = new DatagramPacket(buf, buf.length, dp.getAddress(), dp.getPort());
			
			String route = archivo == 1 ? DIR + MB100 : DIR + MB250;
			File file = new File(route);
			FileInputStream fil = new FileInputStream(file);
			int i = 0;
			while(fil.read(buf) != -1) {
				dp = new DatagramPacket(buf, buf.length, dp.getAddress(), dp.getPort());
				ds.send(dp);
				i++;
			}
			String end = "END";
			System.out.println("Termino de enviar, se hicieron: " + i + " ciclos");
			buf = end.getBytes();
			dp = new DatagramPacket(buf, buf.length, dp.getAddress(), dp.getPort());
			ds.send(dp);
			
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		ds.close();
	}
	
	

}
