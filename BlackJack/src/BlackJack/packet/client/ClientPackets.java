package BlackJack.packet.client;

import java.nio.ByteBuffer;
import javax.swing.SwingUtilities;

import BlackJack.game.graphic.GameGraphic;
import BlackJack.game.graphic.card.CardGraphic;
import BlackJack.game.itens.Card;
import BlackJack.packet.PacketBuffer;
import BlackJack.packet.PacketIdentifier;

public class ClientPackets {

    private final GameGraphic gameGraphic;

    // O construtor recebe a GUI para conseguir atualizar a mesa em tempo real
    public ClientPackets(GameGraphic gameGraphic) {
        this.gameGraphic = gameGraphic;
    }

    /**
     * Handles incoming packets sent from the Server to this Client.
     */
    public void handleIncomingPacket(byte[] data) {
        if (data == null || data.length == 0)
            return;

        // 1. Envolve os bytes no nosso wrapper utilitário
        PacketBuffer packetBuffer = new PacketBuffer(data);

        // 2. Lê o primeiro byte (Identificador do pacote vindo do servidor)
        byte opcode = packetBuffer.readByte();

        switch (opcode) {
            case PacketIdentifier.S_ADD_PLAYER:
                // Instancia o pacote de carta distribuída vazio
                // CardDealtPacket cardPacket = new CardDealtPacket();

                // O próprio pacote lê o restante dos bytes do buffer
                // cardPacket.read(packetBuffer);

                SwingUtilities.invokeLater(() -> {
                    // gameGraphic.addCardToPlayer(cardPacket.getPosition(), cg);
                });
                break;

            case PacketIdentifier.S_RESPONSE_JOIN_GAME:
                // Exemplo de outro pacote vindo do servidor (Aviso de Turno)
                // UpdateTurnPacket turnPacket = new UpdateTurnPacket();
                // turnPacket.read(packetBuffer);
                // boolean myTurn = turnPacket.isMyTurn();

                // SwingUtilities.invokeLater(() -> {
                // if (myTurn) gameGraphic.showActionPanel();
                // else gameGraphic.hideActionPanel();
                // });
                break;

            default:
                System.out.println("Client received unknown packet Opcode: " + opcode);
                break;
        }
    }
}
