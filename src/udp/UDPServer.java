package udp;


import java.net.*;

public class UDPServer {
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
			UDPServerThread[] servs = new UDPServerThread[c];
			for(int i = 0; i < c; i++) {
				servs[i] = new UDPServerThread(i, arch);
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
