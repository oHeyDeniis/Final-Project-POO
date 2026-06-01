package BlackJack.packet;

public interface Packet {

    // static int PACKET_ID = 0x00;

    // void write(PacketBuffer buffer);

    PacketBuffer write();

    void read(PacketBuffer buffer);

    Packet clone();
}