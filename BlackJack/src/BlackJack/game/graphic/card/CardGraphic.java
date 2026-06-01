package BlackJack.game.graphic.card;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import BlackJack.game.itens.Card;

public class CardGraphic {

    public static final int CARD_WIDTH = 50;
    public static final int CARD_HEIGHT = 80;

    public Card card;
    public JPanel panel = null;
    public boolean showFace = false;

    // Nova propriedade para controlar a orientação
    private boolean rotacionada = false;

    public CardGraphic(Card card) {
        this.card = card;
    }

    // Método para definir se a carta deve ser desenhada deitada
    public void setRotacionada(boolean rotacionada) {
        this.rotacionada = rotacionada;
    }

    public Card getCard() {
        return card;
    }

    public void setShowFace(boolean showFace) {
        this.showFace = showFace;
        // Se o painel já existir, força ele a se redesenhar com a nova face
        if (panel != null) {
            panel.repaint();
        }
    }

    public boolean getShowFace() {
        return showFace;
    }

    public JPanel getPanel() {
        if (panel == null) {
            panel = new CardPanel();

            // Se estiver rotacionada, invertemos largura e altura no tamanho do painel
            int w = rotacionada ? CARD_HEIGHT : CARD_WIDTH;
            int h = rotacionada ? CARD_WIDTH : CARD_HEIGHT;
            Dimension dim = new Dimension(w, h);

            panel.setPreferredSize(dim);
            panel.setMinimumSize(dim);
            panel.setMaximumSize(dim);
        }
        return panel;
    }

    private class CardPanel extends JPanel {
        private BufferedImage nipeImage;
        private BufferedImage backImage; // Guarda a imagem do verso

        public CardPanel() {
            setOpaque(false); // Permite ver o fundo verde nos cantos arredondados

            // Carrega a imagem do Nipe (Face)
            try {
                String path = card.getNipeImagePath();
                if (!path.isEmpty()) {
                    nipeImage = ImageIO.read(new File("src/resources/" + path));
                }
            } catch (Exception e) {
                // Fallback em texto caso falte o nipe
            }

            // Carrega a imagem do Verso (Back)
            try {
                backImage = ImageIO.read(new File("src/resources/back.png"));
            } catch (Exception e) {
                System.out.println("Erro ao carregar o verso da carta (back.png): " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // SE TIVER ROTACIONADA, GIRA O DESENHO INTERNO DA CARTA EM 90°
            if (rotacionada) {
                g2d.translate(getWidth(), 0);
                g2d.rotate(Math.toRadians(90));
            }

            int w = CARD_WIDTH;
            int h = CARD_HEIGHT;

            // ============================================================
            // SE CONDICIONAL: SE FACE ESTIVER OCULTA (showFace == false)
            // ============================================================
            if (!showFace) {
                if (backImage != null) {
                    // Desenha a imagem completa do verso ajustada ao tamanho da carta
                    g2d.drawImage(backImage, 0, 0, w, h, null);
                } else {
                    // Fallback visual caso a imagem back.png dê erro/não seja encontrada
                    g2d.setColor(new Color(150, 0, 0)); // Vermelho escuro padrão
                    g2d.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.drawRoundRect(2, 2, w - 5, h - 5, 8, 8);
                }

                g2d.dispose();
                return; // Interrompe aqui para não desenhar os textos da face por cima
            }

            // ============================================================
            // SE SHOWFACE FOR TRUE: DESENHA A FACE NORMAL DA CARTA
            // ============================================================

            // 1. Fundo da carta
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);

            // Cor do nipe
            if (card.getNipe() == Card.NIPE_HEARTS || card.getNipe() == Card.NIPE_DIAMONDS) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.BLACK);
            }

            // 2. Nome da Carta (A)
            int fontSize = h / 7;
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
            g2d.drawString(card.getName(), 6, fontSize + 4);

            // 3. Nipe Pequeno (B)
            int smallNipe = w / 6;
            int smallX = w - smallNipe - 6;
            int smallY = 6;
            if (nipeImage != null) {
                g2d.drawImage(nipeImage, smallX, smallY, smallNipe, smallNipe, null);
            } else {
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                g2d.drawString(String.valueOf(card.getNipeImagePath().charAt(0)), smallX, smallY + 10);
            }

            // 4. Nipe Grande Central (C)
            int largeNipe = w / 2;
            int largeX = (w - largeNipe) / 2;
            int largeY = (h - largeNipe) / 2;
            if (nipeImage != null) {
                g2d.drawImage(nipeImage, largeX, largeY, largeNipe, largeNipe, null);
            } else {
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.drawString("?", (w / 2) - 5, (h / 2) + 7);
            }

            g2d.dispose();
        }
    }
}