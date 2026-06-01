package BlackJack.packet.client;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class PlayerActionPacket implements Packet {

    public static final int ACTION_STAND = 0;
    public static final int ACTION_HIT = 1;
    public static final int ACTION_DOUBLE = 2;

    public int action;

    public PlayerActionPacket() {
    }

    public PlayerActionPacket(int action) {
        this.action = action;
    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.C_PLAYER_ACTION_PACKET);
        buffer.writeInt(action);
        return buffer;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.action = buffer.readInt();
    }

    @Override
    public Packet clone() {
        return new PlayerActionPacket();
    }
}
