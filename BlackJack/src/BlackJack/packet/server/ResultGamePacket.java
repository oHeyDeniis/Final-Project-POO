package BlackJack.packet.server;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class ResultGamePacket implements Packet{
    
    public static final int RESULT_WIN = 0;
    public static final int RESULT_LOSE = 1;
    public static final int RESULT_DRAW = 2;

    public int result;

    public ResultGamePacket(){}

    public ResultGamePacket(int result) {
        this.result = result;
    }
    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_RESULT_GAME_PACKET);
        buffer.writeInt(result);
        return buffer;
    }
    @Override
    public void read(PacketBuffer buffer) {
        this.result = buffer.readInt();
    }
    public Packet clone() {
        return new ResultGamePacket();
    }
}
