package BlackJack.packet.server;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class ResponseJoinGamePacket implements Packet {

    public boolean value;
    public String message;

    public ResponseJoinGamePacket() {

    }

    public ResponseJoinGamePacket(boolean value, String message) {
        this.value = value;
        this.message = message;

    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_RESPONSE_JOIN_GAME);
        buffer.writeBoolean(value);
        buffer.writeString(message);
        return buffer;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.value = buffer.readBoolean();
        this.message = buffer.readString();
    }

    @Override
    public Packet clone() {
        return new ResponseJoinGamePacket();
    }
}
