package BlackJack.packet.server;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class AddPlayerPacket implements Packet {

    public static final String POSITION_NORTH = "North";
    public static final String POSITION_SOUTH = "South";
    public static final String POSITION_EAST = "East";
    public static final String POSITION_WEST = "West";
    public static final String POSITION_DEALER = "DEALER";

    public String name;
    public String position;

    public boolean isSelf = false;

    public AddPlayerPacket() {

    }

    public AddPlayerPacket(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public AddPlayerPacket(String name, String position, boolean isSelf) {
        this.name = name;
        this.position = position;
        this.isSelf = isSelf;
    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_ADD_PLAYER);
        buffer.writeString(name);
        buffer.writeString(position);
        buffer.writeBoolean(isSelf);
        return buffer;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.name = buffer.readString();
        this.position = buffer.readString();
        this.isSelf = buffer.readBoolean();
    }

    @Override
    public Packet clone() {
        return new AddPlayerPacket();
    }
}
