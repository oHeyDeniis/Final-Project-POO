package BlackJack.packet.server;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class BetValuePacket implements Packet {

    public float betValue;

    public BetValuePacket(float betValue) {
        this.betValue = betValue;
    }

    public BetValuePacket() {
    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_BET_VALUE_PACKET);
        buffer.writeFloat(betValue);
        return buffer;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.betValue = buffer.readFloat();
    }

    public Packet clone() {
        return new BetValuePacket();
    }

}
