package BlackJack.packet.server;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class PlayerDisconnectPacket implements Packet{
    
    public String position;

    public PlayerDisconnectPacket(){
        
    }
    public PlayerDisconnectPacket(String position){
        this.position = position;
    }
    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_PLAYER_DISCONNECT_PACKET);
        buffer.writeString(position);
        return buffer;
    }
    public void read(PacketBuffer buffer){
        this.position = buffer.readString();
    }
    public Packet clone(){
        return new PlayerDisconnectPacket();
    }
}
