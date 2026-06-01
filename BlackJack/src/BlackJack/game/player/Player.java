package BlackJack.game.player;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import BlackJack.game.Game;
import BlackJack.game.itens.Card;
import BlackJack.packet.server.AddCardPacket;
import BlackJack.packet.server.DatagramServer;

public class Player {

    private InetAddress address;
    private int port;

    public String name;
    public int score;

    public float balance = 100;
    public float currentBet = 0;

    public String position;

    public PlayerConnection playerConnection;

    public boolean isConnected = true;
    public List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.position = "";
        this.score = 0;
        this.hand = new ArrayList<>();
    }

    public Player(String name, String position) {
        this.name = name;
        this.position = position;
        this.score = 0;
        this.hand = new ArrayList<>();
    }

    public Player(String name, String position, InetAddress address, int port) {
        this.name = name;
        this.position = position;
        this.score = 0;
        this.hand = new ArrayList<>();
        this.address = address;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCardToHand(Card card, boolean showFace) {
        hand.add(card);
        card.toCardGraphic().setShowFace(showFace);
        AddCardPacket packet = new AddCardPacket(position, card);
        DatagramServer.instance.broadcastPacket(packet);

    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void addBalance(float value) {
        balance += value;
    }

    public void removeBalance(float value) {
        balance -= value;
    }

    public float getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(float currentBet) {
        this.currentBet = currentBet;
    }

    // Player class
    public int getTotalCardValue() {
        int totalValue = 0;
        int aceCount = 0;

        // Primeiro passo: Soma tudo, considerando o Ás como 11
        for (Card card : hand) {
            totalValue += card.getGameValue();

            // Guarda a quantidade de Áses que o jogador tem na mão
            if (card.getName().equalsIgnoreCase("a")) {
                aceCount++;
            }
        }

        // Segundo passo: Se estourou 21, transforma os Áses de 11 para 1
        // Cada Ás transformado reduz o total em 10 pontos (11 - 1 = 10)
        while (totalValue > 21 && aceCount > 0) {
            totalValue -= 10;
            aceCount--; // Esse Ás já foi convertido, passa para o próximo se houver
        }

        return totalValue;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void clearHand() {
        hand.clear();
    }

    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }

    public int getScore() {
        return score;
    }

    public String getPosition() {
        return position;
    }

    public String getBorderLayoutPosition() {
        switch (position.toUpperCase()) {
            case "NORTH":
                return BorderLayout.NORTH;
            case "SOUTH":
                return BorderLayout.SOUTH;
            case "EAST":
                return BorderLayout.EAST;
            case "WEST":
                return BorderLayout.WEST;
            default:
                return null; // ou lançar uma exceção
        }
    }

    public PlayerConnection getPlayerConnection() {
        return playerConnection;
    }

    public void setPlayerConnection(PlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
