package BlackJack;

import java.util.Scanner;

import BlackJack.game.graphic.GameGraphic;
import BlackJack.game.graphic.GameLobby;
import BlackJack.packet.client.DatagramClient;
import BlackJack.packet.client.JoinGamePacket;

public class ClientStarter {

    public static void main(String[] args) {
        ClientStarter starter = new ClientStarter();

        starter.start();
    }

    public DatagramClient server = null;
    public GameGraphic gameGraphic;
    public GameLobby gameLobby;

    public ClientStarter() {
        this.gameGraphic = new GameGraphic();
        this.gameLobby = new GameLobby(this);

    }

    public void start() {
        gameLobby.renderLobby();
    }
}
