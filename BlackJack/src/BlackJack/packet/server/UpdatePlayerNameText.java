package BlackJack.packet.server;

import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class UpdatePlayerNameText implements Packet {

    public String position;

    public String name;
    public String bet;
    public String status;

    public UpdatePlayerNameText(String pos, String name, String bet, String status) {
        this.position = pos;
        this.name = name;
        this.bet = bet;
        this.status = status;
    }

    public UpdatePlayerNameText() {
    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_UPDATE_PLAYER_NAME_TEXT_PACKET);
        buffer.writeString(name);
        buffer.writeString(bet);
        buffer.writeString(status);
        buffer.writeString(position);
        return buffer;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.name = buffer.readString();
        this.bet = buffer.readString();
        this.status = buffer.readString();
        this.position = buffer.readString();
    }

    public UpdatePlayerNameText clone() {
        return new UpdatePlayerNameText();
    }
}
