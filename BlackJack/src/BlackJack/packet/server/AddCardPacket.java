package BlackJack.packet.server;

import BlackJack.game.itens.Card;
import BlackJack.packet.Packet;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class AddCardPacket implements Packet {

    public String position;
    public Card card;

    public AddCardPacket() {

    }

    public AddCardPacket(String position, Card card) {
        this.position = position;
        this.card = card;
    }

    @Override
    public PacketBuffer write() {
        PacketBuffer buffer = new PacketBuffer();
        buffer.writeByte(PacketIdentifier.S_ADD_CARD_PACKET);
        buffer.writeString(position);
        buffer.writeString(card.getName());
        buffer.writeInt(card.getValue());
        buffer.writeInt(card.getNipe());
        buffer.writeBoolean(card.toCardGraphic().getShowFace());
        return buffer;
    }

    @Override
    public void read(PacketBuffer buffer) {
        position = buffer.readString();
        card = new Card(buffer.readString(), buffer.readInt(), buffer.readInt());
        card.toCardGraphic().setShowFace(buffer.readBoolean());
    }

    @Override
    public Packet clone() {
        return new AddCardPacket();
    }
}
