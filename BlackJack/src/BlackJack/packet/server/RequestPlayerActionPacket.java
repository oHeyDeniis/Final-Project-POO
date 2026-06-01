package BlackJack.packet.server;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class RequestPlayerActionPacket implements Packet {

    public RequestPlayerActionPacket() {
    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_REQUEST_PLAYER_ACTION_PACKET);
        return buffer;
    }

    @Override
    public void read(PacketBuffer buffer) {
    }

    @Override
    public Packet clone() {
        return new RequestPlayerActionPacket();
    }
}
