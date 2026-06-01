package BlackJack.game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import BlackJack.ServerStarter;
import BlackJack.game.itens.Card;
import BlackJack.game.player.DealerPlayer;
import BlackJack.game.player.Player;
import BlackJack.packet.client.PlayerActionPacket;
import BlackJack.packet.server.AddCardPacket;
import BlackJack.packet.server.AddPlayerPacket;
import BlackJack.packet.server.BetValuePacket;
import BlackJack.packet.server.ClearCardsPacket;
import BlackJack.packet.server.DatagramServer;
import BlackJack.packet.server.RequestPlayerActionPacket;
import BlackJack.packet.server.ResultGamePacket;
import BlackJack.packet.server.ServerPackets;
import BlackJack.packet.server.StartTimePacket;
import BlackJack.packet.server.TitleGamePacket;
import BlackJack.packet.server.UpdatePlayerNameText;

public class GameInstance {

    public static final int START_TIME = 05;
    public static final int END_GAME_TIME = 10;

    public int stage;
    private Game game;

    private List<Card> deck = new ArrayList<>();
    private int gameTime = 0;

    private boolean isStarted = false;
    private boolean isFinished = false;
    private DealerPlayer dealer;

    public int currentActionPlayerIndex = -1;
    public int startTime = 30;
    public int endGameTime = -1;

    public GameInstance(Game game) {
        this.stage = 0;
        this.game = game;
        this.deck.addAll(game.getDeck());

        this.dealer = new DealerPlayer("Dealer");
        dealer.setPosition(AddPlayerPacket.POSITION_DEALER);

        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            @Override
            public void run() {

                runGame();
            }
        };
        timer.scheduleAtFixedRate(tarefa, 0, 1000);

    }

    public List<Player> getAvailablePlayers(boolean withDealer) {
        List<Player> availablePlayers = new ArrayList<>();

        for (Player player : game.getPlayers()) {

            if (player.getCurrentBet() <= 0) {
                continue;
            }
            availablePlayers.add(player);
        }
        if (withDealer) {
            availablePlayers.add(dealer);
        }
        return availablePlayers;
    }

    public void reset() {
        sendDatabaseTest();
        DatagramServer.instance.broadcastPacket(new TitleGamePacket(
                "Nova Rodada",
                "Escolhendo valor de aposta"));
        dealer.getHand().clear();
        DatagramServer.instance.broadcastPacket(new ClearCardsPacket(dealer.getPosition()));
        for (Player player : game.getPlayers()) {
            if (player instanceof DealerPlayer) {
                continue;
            }
            if (!player.isConnected) {
                game.removePlayer(player);
                continue;
            }

            DatagramServer.instance.broadcastPacket(new ClearCardsPacket(player.getPosition()));
            player.getHand().clear();
            player.setCurrentBet(0);
            DatagramServer.instance.broadcastPacket(new UpdatePlayerNameText(
                    player.getPosition(),
                    player.getName(),
                    "$" + player.getBalance(),
                    "Escolhendo valor de aposta"));
            player.getPlayerConnection().sendPacket(new BetValuePacket(player.getBalance()));
        }
        startTime = START_TIME;
        currentActionPlayerIndex = -1;
        isStarted = false;
        isFinished = false;

    }

    public void runGame() {
        if (!isStarted && !isFinished) {
            if (this.getAvailablePlayers(false).size() >= 1) {
                if (this.getAvailablePlayers(false).size() == 4) {
                    start();
                } else {
                    startTime--;
                    DatagramServer.instance.broadcastPacket(new StartTimePacket(startTime));
                    if (startTime == 0) {
                        start();
                    }
                }
            } else {
                startTime = START_TIME;

            }
        }
        if (endGameTime > -1) {
            endGameTime--;
            DatagramServer.instance.broadcastPacket(new TitleGamePacket(
                    "Nova Rodada",
                    "Começando em " + endGameTime,
                    1000));
            if (endGameTime == 0) {
                reset();
            }
        }
        if (!isFinished) {
            if (!canFinish()) {

            }
        }

    }

    public void sendDatabaseTest() {
        String sql2 = "SELECT * FROM players";
        try {
            ResultSet res = ServerStarter.instance.databaseConnection.createStatement().executeQuery(sql2);
            while (res.next()) {
                System.out.println("----------------------");
                System.out.println("Nome: " + res.getString("name"));
                System.out.println("Money: " + res.getFloat("money"));
                System.out.println("----------------------");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    public void start() {
        if (this.getAvailablePlayers(false).size() == 0) {
            startTime = 30;
            for (Player player : game.getPlayers()) {
                BetValuePacket betValuePacket = new BetValuePacket(player.getBalance());
                player.getPlayerConnection().sendPacket(betValuePacket);
            }

            return;
        }
        isStarted = true;
        startTime = 30;
        for (Player player : this.getAvailablePlayers(true)) {
            System.out.println(" player playing game: " + player.getName());
        }
        sendPrimaryCards();
    }

    public String generatePlayerName() {
        return "Player " + game.getPlayers().size();
    }

    public void sendPrimaryCards() {
        for (int i = 0; i < 2; i++) {

            for (Player player : this.getAvailablePlayers(true)) {
                System.out.println("Adding card to " + player.getName());
                boolean showFace = true;
                if (player instanceof DealerPlayer && player.getHand().size() == 1) {
                    showFace = false;
                }
                player.addCardToHand(getRandomCardOfDeck(), showFace);
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
        nextActionPlayer();

    }

    public void nextActionPlayer() {

        if (currentActionPlayerIndex == -1) {

            System.out.println("Comecando rodada de açoes");
        }
        currentActionPlayerIndex++;
        Player player = getCurrentActionPlayer();
        if (player instanceof DealerPlayer) {
            ClearCardsPacket clearCardsPacket = new ClearCardsPacket(AddPlayerPacket.POSITION_DEALER);
            DatagramServer.instance.broadcastPacket(clearCardsPacket);
            for (Card card : player.getHand()) {
                card.toCardGraphic().setShowFace(true);
                AddCardPacket addCardPacket = new AddCardPacket(AddPlayerPacket.POSITION_DEALER, card);
                DatagramServer.instance.broadcastPacket(addCardPacket);
            }
            DatagramServer.instance.broadcastPacket(new TitleGamePacket(
                    "Vez do Dealer", "Dealer esta escolhendo", 4000));
            try {
                Thread.sleep(4000);
                dealerLoop((DealerPlayer) player);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (player.getTotalCardValue() == 21) {
                DatagramServer.instance.broadcastPacket(new TitleGamePacket(
                        "BLACK JACK!",
                        player.getName() + " Chegou a 21", 2000));
                updatePlayerText(player, "BLACK JACK!");
                nextActionPlayer();
                return;
            }
            updatePlayerText(player, "Escolhendo uma ação");
            player.getPlayerConnection().sendPacket(new RequestPlayerActionPacket());
        }
    }

    public void updatePlayerText(Player player, String status) {

        DatagramServer.instance.broadcastPacket(new UpdatePlayerNameText(
                player.getPosition(),
                player.getName() + " [" + player.getTotalCardValue() + "]",
                "$" + player.getCurrentBet() + " (" + player.getBalance() + ")",
                status));

    }

    public void dealerLoop(DealerPlayer dealerPlayer) {
        System.err.println("Dealer loop");
        if (dealerPlayer.getTotalCardValue() < 17) {
            try {
                DatagramServer.instance.broadcastPacket(new TitleGamePacket(
                        "Vez do Dealer", "Dealer irá receber uma carta", 2000));
                Thread.sleep(1000);
                dealerPlayer.addCardToHand(getRandomCardOfDeck(), true);
                Thread.sleep(1000);
                dealerLoop(dealerPlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }
        try {
            DatagramServer.instance.broadcastPacket(new TitleGamePacket(
                    "Aguardando resultados", "Dealer parou em " + dealerPlayer.getTotalCardValue(), 4000));
            Thread.sleep(4000);
        } catch (Exception e) {

        }
        endGameTime = END_GAME_TIME;
        if (dealerPlayer.getTotalCardValue() > 21) {
            DatagramServer.instance.broadcastPacket(new TitleGamePacket(
                    "Dealer estourou", "Dealer passou de 21", 2000));

            for (Player player : game.getPlayers()) {
                if (player.getTotalCardValue() <= 21) {
                    player.getPlayerConnection().sendPacket(new ResultGamePacket(ResultGamePacket.RESULT_WIN));
                    player.addBalance(player.getCurrentBet());
                    ServerPackets.instance.updateDatabasePlayer(player, player.getCurrentBet(), true);
                    player.setCurrentBet(0);
                    updatePlayerText(player, "Venceu");

                } else {
                    player.getPlayerConnection().sendPacket(new ResultGamePacket(ResultGamePacket.RESULT_LOSE));
                    player.removeBalance(player.getCurrentBet());
                    ServerPackets.instance.updateDatabasePlayer(player, player.getCurrentBet(), false);
                    player.setCurrentBet(0);
                    updatePlayerText(player, "Perdeu");
                }
            }
            return;
        }
        for (Player player : game.getPlayers()) {
            if (dealerPlayer != player) {
                if (player.getTotalCardValue() > 21) {
                    player.getPlayerConnection().sendPacket(new ResultGamePacket(ResultGamePacket.RESULT_LOSE));
                    player.removeBalance(player.getCurrentBet());
                    updatePlayerText(player, "Perdeu");
                    ServerPackets.instance.updateDatabasePlayer(player, player.getCurrentBet(), false);
                    player.setCurrentBet(0);
                } else if (player.getTotalCardValue() == dealerPlayer.getTotalCardValue()) {
                    player.getPlayerConnection().sendPacket(new ResultGamePacket(ResultGamePacket.RESULT_DRAW));
                    player.setCurrentBet(0);
                    updatePlayerText(dealerPlayer, "Empate");
                } else if (player.getTotalCardValue() > dealerPlayer.getTotalCardValue()) {
                    player.getPlayerConnection().sendPacket(new ResultGamePacket(ResultGamePacket.RESULT_WIN));
                    player.addBalance(player.getCurrentBet());
                    updatePlayerText(player, "Venceu");
                    ServerPackets.instance.updateDatabasePlayer(player, player.getCurrentBet(), true);
                    player.setCurrentBet(0);
                }
            }
        }

    }

    public void processAction(Player player, int action) {
        try {
            if (player != getCurrentActionPlayer()) {
                return;
            }
            switch (action) {
                case PlayerActionPacket.ACTION_HIT:
                    player.addCardToHand(getRandomCardOfDeck(), true);

                    if (player.getTotalCardValue() > 21) {
                        TitleGamePacket titleGamePacket = new TitleGamePacket(player.getName() + " Perdeu",
                                "Valor: " + player.getTotalCardValue(), 3000);

                        DatagramServer.instance.broadcastPacket(titleGamePacket);
                        updatePlayerText(player, "(PERDEU)");
                        Thread.sleep(3000);
                        nextActionPlayer();
                    } else {
                        if (player.getTotalCardValue() == 21) {
                            TitleGamePacket titleGamePacket = new TitleGamePacket(
                                    "BLACK JACK!",
                                    player.getName() + " Chegou a 21", 2000);
                            DatagramServer.instance.broadcastPacket(titleGamePacket);
                            updatePlayerText(player, "BLACK JACK!");
                            Thread.sleep(3000);
                            nextActionPlayer();
                            return;
                        }
                        TitleGamePacket titleGamePacket = new TitleGamePacket("Vez de " + player.getName(),
                                "Escolhendo uma ação", 3000);
                        DatagramServer.instance.broadcastPacket(titleGamePacket);
                        updatePlayerText(player, "Escolhendo uma ação");
                        Thread.sleep(1000);
                        player.getPlayerConnection().sendPacket(new RequestPlayerActionPacket());
                    }
                    break;

                case PlayerActionPacket.ACTION_STAND:
                    TitleGamePacket titleGamePacket = new TitleGamePacket(player.getName() + " Passou a vez",
                            "proximo jogador...", 3000);
                    updatePlayerText(player, "(PAROU)");
                    DatagramServer.instance.broadcastPacket(titleGamePacket);
                    Thread.sleep(1000);
                    nextActionPlayer();
                    break;
                case PlayerActionPacket.ACTION_DOUBLE:
                    if (player.getCurrentBet() * 2 > player.getBalance()) {
                        TitleGamePacket titleGamePacket1 = new TitleGamePacket(player.getName() + " Está sem dinheiro",
                                "[Tentou dobrar] Proximo jogador...", 1000);
                        DatagramServer.instance.broadcastPacket(titleGamePacket1);
                        Thread.sleep(1000);
                        processAction(player, PlayerActionPacket.ACTION_STAND);
                        return;
                    }
                    TitleGamePacket titleGamePacket1 = new TitleGamePacket(player.getName() + " Dobrou",
                            "Proximo jogador...", 3000);
                    DatagramServer.instance.broadcastPacket(titleGamePacket1);

                    updatePlayerText(player, "(DOBROU)");
                    player.setCurrentBet(player.getCurrentBet() * 2);
                    player.addCardToHand(getRandomCardOfDeck(), true);
                    if (player.getTotalCardValue() > 21) {
                        updatePlayerText(player, "(DOBROU E PERDEU)");
                        nextActionPlayer();
                    }
                    nextActionPlayer();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Player getCurrentActionPlayer() {
        return this.getAvailablePlayers(true).get(currentActionPlayerIndex);
    }

    public void runStage() {

    }

    public Card getRandomCardOfDeck() {
        int index = (int) (Math.random() * deck.size());
        Card card = deck.get(index);
        deck.remove(index);
        return card;
    }

    public boolean canJoin() {
        return !isStarted && !isFinished && game.getPlayers().size() < 5;
    }

    public boolean canFinish() {
        return false;
    }
}
