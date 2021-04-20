package udp;

import java.io.File;
import java.io.FileOutputStream;
import java.net.*;
import java.util.Random;

public class UDPClientThread extends Thread{
	public static final String DIR = "F://Users//usuario//Desktop//redes//ISIS3204-lab3-2-grupo-6//src//data//";
	public static final String MB100 = "100MB.txt";
	public static final String MB250 = "250MB.txt";
	
	private DatagramSocket ds;
	private String id;
	private InetAddress add;
	private int arch;
	private int totalClientes;
	
	LoggingTester log = new LoggingTester("F://Users//usuario//Desktop//redes//ISIS3204-lab3-2-grupo-6//src//LogsClient//Log_");
	Long tiempo;
	
	
	public UDPClientThread(String iD, InetAddress address, DatagramSocket socket, int archivo, int cantidad) {
		arch = archivo;
		add = address;
		id = iD;
		totalClientes = cantidad;
		//System.out.println("Client-"+id+" creado");

	}

	public synchronized void run() {
		try {
			DatagramSocket ds = new DatagramSocket();
			//System.out.println("Client-"+id+" corriendo");
			byte[] buf = new byte[10240];
			Random rand = new Random();
			int r = rand.nextInt();
			//System.out.println("El random del cliente-"+id+" es: "+r);
			String s = "" + id+"-"+r;
			buf = s.getBytes();
			int elId = Integer.parseInt(id);
			DatagramPacket dp = new DatagramPacket(buf, buf.length, add, 6969 + elId);
			String received = new String(dp.getData(), 0, dp.getLength());
			System.out.println("El cliente-"+id+" va a enviar: "+received);
			ds.send(dp);
			
			String ruta = DIR + "Cliente" + id + "-Prueba-" + totalClientes + "-conexiones-tamano-" + (arch == 1 ? MB100 : MB250);
			FileOutputStream fos = new FileOutputStream(new File(ruta));
			ds.receive(dp);
			s = new String(dp.getData(), 0, dp.getLength());
			int i = 0;
			tiempo = System.currentTimeMillis();
			while(!s.contains("END")) {
				fos.write(buf);
				ds.receive(dp);
				s = new String(dp.getData(), 0, dp.getLength());
				i++;
			}
			long elapsedTimeMillis = System.currentTimeMillis()-tiempo;
			float elapsedTimeSec = elapsedTimeMillis/1000F;
			//Se genera el log
			log.doLogging(ruta, true, elapsedTimeSec,i);
			fos.flush();
			System.out.println("Terminao de escribir, se hicieron: " + i + " ciclos");
			ds.close();
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		//System.out.println("Client-"+id+" terminado");
	}



}
