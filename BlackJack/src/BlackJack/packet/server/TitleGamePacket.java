package BlackJack.packet.server;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class TitleGamePacket implements Packet {

    public String title;
    public String description;
    public int time = 1000;

    public TitleGamePacket(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public TitleGamePacket(String title, String description, int time) {
        this.title = title;
        this.description = description;
        this.time = time;
    }

    public TitleGamePacket() {
    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_TITLE_GAME_PACKET);
        buffer.writeString(title);
        buffer.writeString(description);
        buffer.writeInt(time);
        return buffer;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.title = buffer.readString();
        this.description = buffer.readString();
        this.time = buffer.readInt();
    }

    public Packet clone() {
        return new TitleGamePacket();
    }
}
