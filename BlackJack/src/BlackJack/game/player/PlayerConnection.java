package BlackJack.game.player;

import java.net.InetAddress;

import BlackJack.packet.Packet;
import BlackJack.packet.server.DatagramServer;

public class PlayerConnection {

    private InetAddress address;
    private int port;
    private Player player = null;

    public int pendingPing = 0;

    public PlayerConnection(InetAddress address, int port, Player player) {
        this.address = address;
        this.port = port;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        player.setPlayerConnection(this);
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void sendPacket(Packet packet) {
        System.out.println("Sending packet to: " + getPlayer().getName() + ": " + address + ":" + port + ": "
                + packet.getClass().getSimpleName());
        DatagramServer.instance.sendPacket(packet.write().getAvailableBytes(), address, port);

    }
}
