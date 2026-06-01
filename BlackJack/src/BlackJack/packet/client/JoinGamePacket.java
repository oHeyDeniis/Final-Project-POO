package BlackJack.packet.client;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class JoinGamePacket implements Packet {

    public String name;

    public JoinGamePacket() {}

    public JoinGamePacket(String name) {
        this.name = name;
    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.C_JOIN_GAME);
        buffer.writeString(name);
        return buffer;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.name = buffer.readString();
    }

    @Override
    public Packet clone() {
        return new JoinGamePacket();
    }
}