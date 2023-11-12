package serveur;

import java.io.*;
import java.net.*;




public class Server extends Thread{
	int ord;
	public static void main(String[] args) {
		new Server().start();

	}
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(1234);
			System.out.println("Démarrage du Server");
			while(true) {
				Socket s = ss.accept();
				new ClientProcess(s, ++ord).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public class ClientProcess extends Thread {
		Socket socket;
		private int numClient;
		public ClientProcess(Socket socket,int numClient) {
			super();
			this.socket = socket;
			this.numClient = numClient; 
		}
		public void run() {
			try {

				DatagramSocket serverSocket = new DatagramSocket(1234);
	            byte[] receiveData = new byte[1024];
				System.out.println("Le serveur est prêt à recevoir des connexions...");

				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                String response = "Bienvenue " + clientMessage;
                byte[] sendData = response.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);

                System.out.println("Message reçu: " + clientMessage);
                System.out.println("Client connecté depuis " + clientAddress.getHostAddress() + ":" + clientPort);
				serverSocket.close();
			} catch (IOException  e) {
				e.printStackTrace();
			}
		}
	}

}