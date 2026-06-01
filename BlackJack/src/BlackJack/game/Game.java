package BlackJack.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import BlackJack.game.graphic.GameGraphic;
import BlackJack.game.itens.Card;
import BlackJack.game.player.DealerPlayer;
import BlackJack.game.player.Player;
import BlackJack.packet.server.AddPlayerPacket;
import BlackJack.packet.server.DatagramServer;
import BlackJack.packet.server.PingPongPacket;
import BlackJack.packet.server.TitleGamePacket;

import java.util.concurrent.CopyOnWriteArrayList;

public class Game {

    public static void main(String[] args) {
        new Game();
    }

    public static Game instance;

    private GameGraphic graphic;
    private List<Card> deck = new CopyOnWriteArrayList<>();
    private List<Player> players = new CopyOnWriteArrayList<>();

    public GameInstance gameInstance;

    public Game() {
        Game.instance = this;
        initDeck(1);
        // initPlayers();
        Game.instance = this;
        this.gameInstance = new GameInstance(this);
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {

                List<Player> toDisconnect = new ArrayList<>();

                for (Player player : players) {

                    synchronized (player.getPlayerConnection()) {

                        if (player.getPlayerConnection().pendingPing >= 3) {
                            System.out.println("Player: " + player.name + " timed out (Connection lost).");
                            toDisconnect.add(player);
                            continue;
                        }

                        player.getPlayerConnection().pendingPing++;
                        player.getPlayerConnection().sendPacket(new PingPongPacket());
                    }
                }
                for (Player deadPlayer : toDisconnect) {
                    disconnectPlayer(deadPlayer);
                }
            }
        };

        timer.scheduleAtFixedRate(tarefa, 0, 5000);

    }

    public static GameInstance getGameInstance() {
        return Game.getInstance().gameInstance;
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
            System.out.println("Game instance created");
        }
        return instance;
    }

    public void initPlayers() {
        addPlayer(new DealerPlayer("dealer"));
        addPlayer(new Player("Player NORTH"));
        addPlayer(new Player("Player SOUTH"));
        addPlayer(new Player("Player EAST"));
        addPlayer(new Player("Player WEST"));
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameGraphic getGraphic() {
        return graphic;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void initDealer() {
        if (hasDealer())
            return;
        DealerPlayer dealer = new DealerPlayer("Dealer");
        dealer.setPosition(AddPlayerPacket.POSITION_DEALER);
        players.add(dealer);
    }

    public boolean hasDealer() {
        for (Player player : players) {
            if (player instanceof DealerPlayer) {
                return true;
            }
        }
        return false;
    }

    public void disconnectPlayer(Player player) {
        player.isConnected = false;
        DatagramServer.instance.broadcastPacket(new TitleGamePacket(
                "" + player.getName() + " saiu",
                "Jogador desconectou do servidor"));
    }

    public void removePlayer(Player player) {

        players.remove(player);

    }

    public boolean addPlayer(Player player) {
        if (players.size() < 5) {
            players.add(player);
            switch (players.size()) {
                case 1:
                    player.setPosition(AddPlayerPacket.POSITION_NORTH);
                    break;
                case 2:
                    player.setPosition(AddPlayerPacket.POSITION_SOUTH);
                    break;
                case 3:
                    player.setPosition(AddPlayerPacket.POSITION_EAST);
                    break;
                case 4:
                    player.setPosition(AddPlayerPacket.POSITION_WEST);
                    break;

                default:
                    break;
            }
            // graphic.renderPlayer(player);
            return true;
        }
        return false;
    }

    public boolean hasPlayer(String name) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void initDeck(int numberOfDecks) {
        for (int d = 0; d < numberOfDecks; d++) {
            for (int nipe = 1; nipe <= 4; nipe++) {
                for (int value = 1; value <= 13; value++) {
                    String name;
                    if (value == 1) {
                        name = "A";
                    } else if (value == 11) {
                        name = "J";
                    } else if (value == 12) {
                        name = "Q";
                    } else if (value == 13) {
                        name = "K";
                    } else {
                        name = String.valueOf(value);
                    }
                    deck.add(new Card(name, value, nipe));
                }
            }
        }
    }
}
