package BlackJack.game.player;

import BlackJack.packet.server.AddPlayerPacket;

public class DealerPlayer extends Player {

    public DealerPlayer(String name) {
        super(name);
        this.setPosition(AddPlayerPacket.POSITION_DEALER);
    }
}
