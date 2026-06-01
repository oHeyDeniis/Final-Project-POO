package BlackJack.packet.server;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class ClearCardsPacket implements Packet {

    public String position;

    public ClearCardsPacket() {}

    public ClearCardsPacket(String position) {
        this.position = position;
    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_CLEAR_CARDS_PACKET);
        buffer.writeString(position);
        return buffer;
    }

    @Override
    public void read(PacketBuffer buffer) {
        position = buffer.readString();
    }

    @Override
    public Packet clone() {
        return new ClearCardsPacket();
    }
}
