package udp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;



public class UDPServerThread extends Thread{

	public static final String DIR = "/home/infracom/ISIS3204-lab3-2-grupo-6/src/data/";
	public static final String MB100 = "100MB.test";
	public static final String MB250 = "250MB.test";
	public static final String HASHALG = "MD5";
	
	private DatagramSocket ds;
	private DatagramPacket dp;
	private byte[] buf;
	private String id;
	private int archivo;
	private String llave;
	
	LoggingTester log = new LoggingTester("/home/infracom/ISIS3204-lab3-2-grupo-6/src/LogsServer/Log_");
	Long tiempo;
	
	public UDPServerThread(int iD, int fil, String llave) {
		try {
			ds = new DatagramSocket(10000+iD);
			buf = new byte[1024];
			dp = new DatagramPacket(buf, buf.length);
			id = iD+"";
			archivo = fil;
			this.llave = llave;
			
			
			System.out.println("Server-"+id+" creado");
			
			
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static String encrypt(String ruta) throws Exception{
		String s = "";
		Path p;
		p = Paths.get(ruta);
		MessageDigest md = MessageDigest.getInstance(HASHALG);
		s = (new BigInteger(1,md.digest(Files.readAllBytes(p)))).toString();
		return s;
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
			tiempo = System.currentTimeMillis();
			
			
			while(fil.read(buf) != -1) {
				dp = new DatagramPacket(buf, buf.length, dp.getAddress(), dp.getPort());
				ds.send(dp);
				
				i++;
			}
			
			long elapsedTimeMillis = System.currentTimeMillis()-tiempo;
			float elapsedTimeSec = elapsedTimeMillis/1000F;
			//Se genera el log
			log.doLogging(route, true, elapsedTimeSec,i);
			String end = "END -"+ llave;
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
