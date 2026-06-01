package BlackJack.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import BlackJack.packet.client.JoinGamePacket;
import BlackJack.packet.client.PlayerActionPacket;
import BlackJack.packet.server.AddCardPacket;
import BlackJack.packet.server.AddPlayerPacket;
import BlackJack.packet.server.BetValuePacket;
import BlackJack.packet.server.ClearCardsPacket;
import BlackJack.packet.server.PingPongPacket;
import BlackJack.packet.server.PlayerDisconnectPacket;
import BlackJack.packet.server.RequestPlayerActionPacket;
import BlackJack.packet.server.ResponseJoinGamePacket;
import BlackJack.packet.server.ResultGamePacket;
import BlackJack.packet.server.StartTimePacket;
import BlackJack.packet.server.TitleGamePacket;
import BlackJack.packet.server.UpdatePlayerNameText;

public class PacketIdentifier {

    // Client to Server
    public static final byte C_JOIN_GAME = 0x01;
    public static final byte C_PLAYER_ACTION = 0x02;
    public static final byte C_PLAYER_ACTION_PACKET = 0x03;

    // Server to Client
    public static final byte S_ADD_PLAYER = 0x11;
    public static final byte S_RESPONSE_JOIN_GAME = 0x12;
    public static final byte S_ADD_CARD_PACKET = 0x13;
    public static final byte S_SHOW_CARD_PACKET = 0x14;
    public static final byte S_CLEAR_CARDS_PACKET = 0x15;
    public static final byte S_PLAYER_ACTION = 0x16;
    public static final byte S_START_TIME_PACKET = 0x17;
    public static final byte S_REQUEST_PLAYER_ACTION_PACKET = 0x18;
    public static final byte S_RESULT_GAME_PACKET = 0x19;
    public static final byte S_TITLE_GAME_PACKET = 0x20;
    public static final byte S_UPDATE_PLAYER_NAME_TEXT_PACKET = 0x21;
    public static final byte S_BET_VALUE_PACKET = 0x22;
    public static final byte S_PLAYER_DISCONNECT_PACKET = 0x23;
    public static final byte S_PING_PONG_PACKET = 0x24;

    public HashMap<Byte, Packet> packets = new HashMap<>();

    public static PacketIdentifier instance = null;

    public PacketIdentifier() {
        PacketIdentifier.instance = this;
        packets.put(PacketIdentifier.C_JOIN_GAME, new JoinGamePacket());
        packets.put(PacketIdentifier.C_PLAYER_ACTION_PACKET, new PlayerActionPacket());

        packets.put(PacketIdentifier.S_RESPONSE_JOIN_GAME, new ResponseJoinGamePacket());
        packets.put(PacketIdentifier.S_ADD_PLAYER, new AddPlayerPacket());
        packets.put(PacketIdentifier.S_REQUEST_PLAYER_ACTION_PACKET, new RequestPlayerActionPacket());
        packets.put(PacketIdentifier.S_ADD_CARD_PACKET, new AddCardPacket());
        packets.put(PacketIdentifier.S_CLEAR_CARDS_PACKET, new ClearCardsPacket());
        packets.put(PacketIdentifier.S_START_TIME_PACKET, new StartTimePacket());
        packets.put(PacketIdentifier.S_RESULT_GAME_PACKET, new ResultGamePacket());
        packets.put(PacketIdentifier.S_TITLE_GAME_PACKET, new TitleGamePacket());
        packets.put(PacketIdentifier.S_UPDATE_PLAYER_NAME_TEXT_PACKET, new UpdatePlayerNameText());
        packets.put(PacketIdentifier.S_BET_VALUE_PACKET, new BetValuePacket());
        packets.put(PacketIdentifier.S_PLAYER_DISCONNECT_PACKET, new PlayerDisconnectPacket());
        packets.put(PacketIdentifier.S_PING_PONG_PACKET, new PingPongPacket());
    }

    public static PacketIdentifier getInstance() {
        if (instance == null) {
            instance = new PacketIdentifier();
        }
        return instance;
    }

    public Packet getPacket(byte opcode) {
        // CLONE PACKET
        if (packets.containsKey(opcode)) {
            return packets.get(opcode).clone();
        }
        return null;
    }

    public static String getPacketName(byte opcode) {
        if (instance.packets.containsKey(opcode)) {
            return instance.packets.get(opcode).getClass().getSimpleName();
        }
        return null;
    }

    public static String getPacketName(Packet packet) {
        if (instance.packets.containsValue(packet)) {
            return packet.getClass().getSimpleName();
        }
        return null;
    }
}
