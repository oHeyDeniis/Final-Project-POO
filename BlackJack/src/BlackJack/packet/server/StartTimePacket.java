package BlackJack.packet.server;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class StartTimePacket implements Packet {

    public int time;

    public StartTimePacket(int time) {
        this.time = time;
    }

    public StartTimePacket() {
    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_START_TIME_PACKET);
        buffer.writeInt(time);
        return buffer;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.time = buffer.readInt();
    }

    @Override
    public Packet clone() {
        return new StartTimePacket();
    }

}
