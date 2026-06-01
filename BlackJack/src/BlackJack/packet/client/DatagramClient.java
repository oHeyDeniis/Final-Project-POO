package BlackJack.packet.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import BlackJack.game.graphic.BetDialog;
import BlackJack.game.graphic.GameGraphic;
import BlackJack.game.graphic.GameLobby;
import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;
import BlackJack.packet.server.AddCardPacket;
import BlackJack.packet.server.AddPlayerPacket;
import BlackJack.packet.server.BetValuePacket;
import BlackJack.packet.server.ClearCardsPacket;
import BlackJack.packet.server.PingPongPacket;
import BlackJack.packet.server.PlayerDisconnectPacket;
import BlackJack.packet.server.RequestPlayerActionPacket;
import BlackJack.packet.server.ResponseJoinGamePacket;
import BlackJack.packet.server.ResultGamePacket;
import BlackJack.packet.server.StartTimePacket;
import BlackJack.packet.server.TitleGamePacket;
import BlackJack.packet.server.UpdatePlayerNameText;

public class DatagramClient extends Thread {

    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private boolean running;
    private final GameGraphic gameGraphic;
    private static final int BUFFER_SIZE = 1024;

    public static DatagramClient instance;

    public DatagramClient(String serverIp, int serverPort, GameGraphic gameGraphic) {
        instance = this;
        this.serverPort = serverPort;
        this.gameGraphic = gameGraphic;
        try {

            this.socket = new DatagramSocket();
            this.serverAddress = InetAddress.getByName(serverIp);
            this.running = true;
            System.out.println("BlackJack Client started. Local port: " + socket.getLocalPort());
        } catch (Exception e) {
            System.out.println("Error initializing client socket: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];

        while (running) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                // Aguarda pacotes vindos do servidor (Bloqueia aqui até chegar algo)
                socket.receive(receivePacket);

                // Isola os bytes úteis recebidos
                byte[] data = new byte[receivePacket.getLength()];
                System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0,
                        receivePacket.getLength());
                if (data == null || data.length == 0)
                    return;

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String clientKey = clientAddress.getHostAddress() + ":" + clientPort;
                System.out.println("Received packet from " + clientAddress + ":" + clientPort + " with " + data.length
                        + " bytes.");
                PacketBuffer packetBuffer = new PacketBuffer(data);
                byte opcode = packetBuffer.readByte();
                System.out.println("Opcode: " + opcode);
                Packet packet = PacketIdentifier.getInstance().getPacket(opcode);
                packet.read(packetBuffer);
                handleServerPacket(packet);

            } catch (Exception e) {
                if (!running) {
                    System.out.println("Client socket closed gracefully.");
                } else {
                    System.out.println("Error receiving server packet: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public static String selfPosition = null;

    private void handleServerPacket(Packet packet) {
        if (packet instanceof PingPongPacket) {
            this.sendPacketToServer(new PingPongPacket());
        }
        if (packet instanceof ResponseJoinGamePacket) {
            ResponseJoinGamePacket responseJoinGamePacket = (ResponseJoinGamePacket) packet;
            if (responseJoinGamePacket.value) {
                GameLobby.instance.isConnected = true;
                GameLobby.instance.lobbyScreen.dispose();
                gameGraphic.render();
            } else {
                GameLobby.instance.isConnected = false;
                GameLobby.instance.lblSubtitle.setText("Error ao entrar: " + responseJoinGamePacket.message);
                System.out.println("Error joining game: " + responseJoinGamePacket.message);
            }
        }
        if (packet instanceof AddPlayerPacket) {
            if (!gameGraphic.isRendered) {
                System.out.println("Game not rendered yet");
                return;
            }
            AddPlayerPacket addPlayerPacket = (AddPlayerPacket) packet;
            if (addPlayerPacket.isSelf) {
                selfPosition = addPlayerPacket.position;
            }
            System.out.println("Adding player " + addPlayerPacket.name + " in position " + addPlayerPacket.position);
            gameGraphic.addPlayer(addPlayerPacket.position, addPlayerPacket.name);
        }
        if (packet instanceof AddCardPacket) {
            AddCardPacket addCardPacket = (AddCardPacket) packet;
            gameGraphic.addCardToPlayer(addCardPacket.position, addCardPacket.card.toCardGraphic());
        }
        if (packet instanceof ClearCardsPacket) {
            gameGraphic.clearCards(((ClearCardsPacket) packet).position);
        }
        if (packet instanceof StartTimePacket) {
            System.out.println("Tempo: " + ((StartTimePacket) packet).time);
            // gameGraphic.info.setText("Tempo: " + ((StartTimePacket) packet).time);
            int startTime = ((StartTimePacket) packet).time;
            gameGraphic.showMessageDialog("Iniciando rodada",
                    "em " + startTime + " segndos",
                    startTime == 0 ? 1000 : 0);
        }
        if (packet instanceof RequestPlayerActionPacket) {
            SwingUtilities.invokeLater(() -> {
                gameGraphic.setActionPanel(true);
            });
            // gameGraphic.setActionPanel(true);

        }
        if (packet instanceof TitleGamePacket) {
            TitleGamePacket titleGamePacket = (TitleGamePacket) packet;
            SwingUtilities.invokeLater(() -> {
                gameGraphic.showMessageDialog(titleGamePacket.title, titleGamePacket.description, titleGamePacket.time);
            });
        }
        if (packet instanceof UpdatePlayerNameText) {
            UpdatePlayerNameText updatePlayerNameText = (UpdatePlayerNameText) packet;
            gameGraphic.setPlayerPositionText(updatePlayerNameText.position, updatePlayerNameText.name,
                    updatePlayerNameText.bet, updatePlayerNameText.status);
        }
        if (packet instanceof BetValuePacket) {
            BetValuePacket betValuePacket = (BetValuePacket) packet;
            SwingUtilities.invokeLater(() -> {

                BetDialog betDialog = new BetDialog(this.gameGraphic.gameScreen, betValuePacket.betValue);
                betDialog.setVisible(true);

                float finalBetValue = betDialog.getSelectedBet();

                BetValuePacket pk = new BetValuePacket(finalBetValue);
                DatagramClient.instance.sendPacketToServer(pk);
                System.out.println("Sending bet to server: $" + finalBetValue);

            });
        }
        if (packet instanceof PlayerDisconnectPacket) {
            PlayerDisconnectPacket playerDisconnectPacket = (PlayerDisconnectPacket) packet;
            gameGraphic.removePlayer(playerDisconnectPacket.position);
        }
        if(packet instanceof ResultGamePacket){
            
        }
    }

    public void sendPacketToServer(Packet packet) {
        sendPacketToServer(packet.write().getAvailableBytes());
    }

    public void sendPacketToServer(byte[] data) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverAddress, serverPort);
            socket.send(sendPacket);
            System.out.println("Sent packet to server");
        } catch (Exception e) {
            System.out.println("Error sending packet to server: " + e.getMessage());
        }
    }

    /**
     * Desconecta o cliente.
     */
    public void disconnect() {
        this.running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}