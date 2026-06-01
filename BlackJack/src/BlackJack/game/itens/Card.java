package BlackJack.game.itens;

import BlackJack.game.graphic.card.CardGraphic;

public class Card {

    public static final int NIPE_HEARTS = 1;
    public static final int NIPE_DIAMONDS = 2;
    public static final int NIPE_CLUBS = 3;
    public static final int NIPE_SPADES = 4;

    private String name;
    private int value;
    private int nipe;

    public Card(String name, int value, int nipe) {
        this.name = name;
        this.value = value;
        this.nipe = nipe;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    // Card Class
    public int getGameValue() {
        String cardName = getName().toLowerCase();

        // Valetes, Damas e Reis valem sempre 10
        if (cardName.equals("j") || cardName.equals("q") || cardName.equals("k")) {
            return 10;
        }

        // O Ás começa valendo 11 por padrão na contagem inicial
        if (cardName.equals("a")) {
            return 11;
        }

        // Cartas numéricas (2 a 10) retornam seu próprio valor
        return value;
    }

    public int getNipe() {
        return nipe;
    }

    public String getNipeImagePath() {
        switch (nipe) {
            case NIPE_HEARTS:
                return "heart.png";
            case NIPE_DIAMONDS:
                return "diamond.png";
            case NIPE_CLUBS:
                return "clubs.png";
            case NIPE_SPADES:
                return "spade.png";
            default:
                return "";
        }
    }

    public CardGraphic cardGraphic;

    public CardGraphic toCardGraphic() {
        if (cardGraphic == null) {
            cardGraphic = new CardGraphic(this);
        }
        return cardGraphic;
    }
}
