package BlackJack.packet.server;

import java.awt.List;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import BlackJack.game.player.PlayerConnection;
import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class DatagramServer extends Thread {

    private DatagramSocket socket;
    private final ServerPackets packetHandler;
    private boolean running;
    private static final int BUFFER_SIZE = 1024; // Tamanho máximo do pacote (1KB é mais que suficiente para o
                                                 // BlackJack)
    public static DatagramServer instance;

    public DatagramServer(int port) {
        instance = this;
        this.packetHandler = new ServerPackets();
        try {
            // Inicializa o socket UDP na porta escolhida
            this.socket = new DatagramSocket(port);
            this.running = true;
            System.out.println("BlackJack Datagram Server started on port " + port);
        } catch (Exception e) {
            System.out.println("Error initializing server socket: " + e.getMessage());
        }
    }

    public ConcurrentHashMap<String, PlayerConnection> playerConnections = new ConcurrentHashMap<>();

    @Override
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];

        while (running) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);
                byte[] data = new byte[receivePacket.getLength()];
                System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0,
                        receivePacket.getLength());

                // Captura as informações de quem enviou (IP e Porta) para responder depois
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String clientKey = clientAddress.getHostAddress() + ":" + clientPort;
                System.out.println("Received packet from " + clientAddress + ":" + clientPort);
                if (!playerConnections.containsKey(clientKey)) {
                    playerConnections.put(clientKey, new PlayerConnection(clientAddress, clientPort, null));
                }
                PlayerConnection playerConnection = playerConnections.get(clientKey);
                if (data == null || data.length == 0)
                    return;

                PacketBuffer packetBuffer = new PacketBuffer(data);
                byte opcode = packetBuffer.readByte();
                System.out.println("Opcode: " + opcode);
                Packet packet = PacketIdentifier.getInstance().getPacket(opcode);
                packet.read(packetBuffer);
                packetHandler.handleIncomingPacket(packet, playerConnection);

            } catch (Exception e) {
                if (!running) {
                    System.out.println("Server socket closed gracefully.");
                } else {
                    System.out.println("Error receiving packet: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Envia um pacote estruturado para um cliente específico via UDP.
     */
    public void sendPacket(byte[] data, InetAddress address, int port) {
        try {
            System.out.println("sent packet to " + address + ":" + port + " with " + data.length + " bytes.");
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, port);
            socket.send(sendPacket);
        } catch (Exception e) {
            System.out.println("Error sending packet: " + e.getMessage());
        }
    }

    public void broadcastPacket(Packet packet) {
        for (PlayerConnection playerConnection : playerConnections.values()) {
            System.out.println("broadcast packet to: " + playerConnection.getPlayer().getName() + ": "
                    + playerConnection.getPort() + ": " + packet.getClass().getSimpleName());
            playerConnection.sendPacket(packet);

        }
    }

    /**
     * Desliga o servidor de forma limpa.
     */
    public void shutdown() {
        this.running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}