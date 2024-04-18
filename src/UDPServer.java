import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class UDPServer {
    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        Set<InetAddress> clientAddresses = new HashSet<>();
        byte[] receiveData = new byte[1024];

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            clientAddresses.add(IPAddress);  // Keep track of client addresses

            String message = new String(receivePacket.getData()).trim();
            System.out.println("Received: " + message);

            byte[] sendData = message.getBytes();
            for (InetAddress client : clientAddresses) {
                if (!client.equals(IPAddress)) {  // Send to all clients except the sender
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client, port);
                    serverSocket.send(sendPacket);
                }
            }

            if (message.equals("exit")) {
                clientAddresses.remove(IPAddress);
                if (clientAddresses.isEmpty()) {
                    break;
                }
            }
        }
        serverSocket.close();
    }
}
