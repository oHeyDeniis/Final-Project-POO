package BlackJack.packet.server;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class PingPongPacket implements Packet {

    public PingPongPacket() {

    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_PING_PONG_PACKET);
        return buffer;
    }

    public void read(PacketBuffer buffer) {

    }

    @Override
    public Packet clone() {
        return new PingPongPacket();
    }
}
