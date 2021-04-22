package udp;


import java.math.BigInteger;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class UDPServer {
	public static final String DIR = "/home/infracom/ISIS3204-lab3-2-grupo-6/src/data/";
	public static final String MB100 = "100MB.test";
	public static final String MB250 = "250MB.test";
	public static final String HASHALG = "MD5";
	
	public static String encrypt(String ruta) throws Exception{
		String s = "";
		Path p;
		p = Paths.get(ruta);
		MessageDigest md = MessageDigest.getInstance(HASHALG);
		s = (new BigInteger(1,md.digest(Files.readAllBytes(p)))).toString();
		return s;
	}
	
	public static synchronized void main(String[] args) {
		try {
			
			DatagramSocket ds = new DatagramSocket(9999);
			byte[] buf = new byte[4];
			
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			ds.receive(dp);
			
			String received = new String(dp.getData(), 0, dp.getLength());
			String[] data = received.split("-");
			System.out.println("Son "+data[0]+" clientes y el archivo es " + data[1]);
			
			int c = Integer.parseInt(data[0]);
			int arch = Integer.parseInt(data[1]);
			
			
			String route = arch == 1 ? DIR + MB100 : DIR + MB250;
			String llave = encrypt(route);
			
			
			
			UDPServerThread[] servs = new UDPServerThread[c];
			for(int i = 0; i < c; i++) {
				servs[i] = new UDPServerThread(i, arch, llave);
			}
			for(int i = 0; i < c; i++) {
				servs[i].start();
			}
			received = "01";
			buf = received.getBytes();
			dp = new DatagramPacket(buf, buf.length, dp.getAddress(), dp.getPort());
			ds.send(dp);
			ds.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
