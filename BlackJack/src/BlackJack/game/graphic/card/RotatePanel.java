package BlackJack.game.graphic.card;
import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.RenderingHints;

import javax.swing.JPanel;

class RotatedPanel extends JPanel {
    private final boolean rotateClockwise;

    public RotatedPanel(boolean rotateClockwise) {
        this.rotateClockwise = rotateClockwise;
        setOpaque(false);
        // Inverte as dimensões preferidas para o gerenciador externo (GridBagLayout)
        // reservar o espaço perfeitamente em pé nas laterais da tela
        setPreferredSize(new Dimension(140, 200));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Configurações para suavizar fontes e bordas das cartas rotacionadas
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (rotateClockwise) {
            // Gira 90° horário (Leste)
            g2d.translate(getWidth(), 0);
            g2d.rotate(Math.toRadians(90));
        } else {
            // Gira -90° anti-horário (Oeste)
            g2d.translate(0, getHeight());
            g2d.rotate(Math.toRadians(-90));
        }

        // Desenha os componentes internos já transformados
        super.paintComponent(g2d);
        g2d.dispose();
    }
}
