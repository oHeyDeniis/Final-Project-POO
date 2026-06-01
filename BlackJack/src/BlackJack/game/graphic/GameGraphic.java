package BlackJack.game.graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import BlackJack.game.graphic.card.CardGraphic;
import BlackJack.game.itens.Card;
import BlackJack.game.player.DealerPlayer;
import BlackJack.game.player.Player;
import BlackJack.packet.client.DatagramClient;
import BlackJack.packet.client.PlayerActionPacket;
import BlackJack.packet.server.AddPlayerPacket;
import BlackJack.packet.server.PlayerDisconnectPacket;

public class GameGraphic {

    public static void main(String[] args) {
        new GameGraphic();
    }

    public JFrame gameScreen;
    protected JPanel mainTablePanel;

    // Player panels storage
    private JPanel dealerPanel;
    private JPanel northPlayerPanel;
    private JPanel southPlayerPanel;
    private JPanel westPlayerPanel;
    private JPanel eastPlayerPanel;
    public JLabel info;
    // Atributo global para controlar o painel de botões
    public JPanel actionControlPanel;

    public GameGraphic() {

    }

    public boolean isRendered = false;

    public void render() {

        gameScreen = new JFrame("BlackJack");
        gameScreen.setSize(800, 600);
        gameScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameScreen.setLocationRelativeTo(null);
        /// event on close
        gameScreen.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (DatagramClient.selfPosition != null)
                    DatagramClient.instance.sendPacketToServer(new PlayerDisconnectPacket(DatagramClient.selfPosition));
            }
        });

        JPanel mesaPrincipal = new JPanel(new BorderLayout());
        this.mainTablePanel = mesaPrincipal;
        mesaPrincipal.setBackground(new Color(7, 94, 23)); // Verde Cassino

        JPanel centroDaMesa = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        centroDaMesa.setOpaque(false);

        JPanel centroWrapper = new JPanel(new GridBagLayout());
        centroWrapper.setOpaque(false);

        JPanel areaCartasBanca = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        areaCartasBanca.setOpaque(false);
        this.dealerPanel = areaCartasBanca;

        JPanel monteBaralho = new JPanel(new BorderLayout());
        monteBaralho.setPreferredSize(new Dimension(80, 120));
        monteBaralho.setBackground(new Color(150, 0, 0)); // Verso Vermelho
        monteBaralho.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        JLabel labelMonte = new JLabel("monte", SwingConstants.CENTER);
        labelMonte.setForeground(Color.WHITE);
        monteBaralho.add(labelMonte, BorderLayout.CENTER);

        centroDaMesa.add(areaCartasBanca);
        centroDaMesa.add(monteBaralho);

        centroWrapper.add(centroDaMesa);
        mesaPrincipal.add(centroWrapper, BorderLayout.CENTER);

        JPanel southContainer = new JPanel();
        southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));
        southContainer.setOpaque(false);
        southContainer.setPreferredSize(new Dimension(800, 100));
        actionControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionControlPanel.setOpaque(false);

        JButton btnHit = new JButton("PEDIR");
        btnHit.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                this.actionControlPanel.setVisible(false);
            });
            PlayerActionPacket packet = new PlayerActionPacket(PlayerActionPacket.ACTION_HIT);
            DatagramClient.instance.sendPacketToServer(packet);

        });
        JButton btnSkip = new JButton("PASSAR");
        btnSkip.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                this.actionControlPanel.setVisible(false);
            });
            PlayerActionPacket packet = new PlayerActionPacket(PlayerActionPacket.ACTION_STAND);
            DatagramClient.instance.sendPacketToServer(packet);

        });
        JButton btnRaise = new JButton("AUMENTAR");
        btnRaise.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                this.actionControlPanel.setVisible(false);
            });
            PlayerActionPacket packet = new PlayerActionPacket(PlayerActionPacket.ACTION_DOUBLE);
            DatagramClient.instance.sendPacketToServer(packet);

        });

        actionControlPanel.add(btnHit);
        actionControlPanel.add(btnSkip);
        actionControlPanel.add(btnRaise);

        southContainer.add(actionControlPanel);
        mesaPrincipal.add(southContainer, BorderLayout.SOUTH);
        actionControlPanel.setVisible(false);

        /**
         * this.info = new JLabel("BlackJack", SwingConstants.CENTER);
         * info.setForeground(Color.WHITE);
         * info.setFont(new Font("Arial", Font.BOLD, 16));
         * mesaPrincipal.add(info, BorderLayout.CENTER);
         **/

        gameScreen.add(mesaPrincipal);

        


        painelAviso = new JPanel(new BorderLayout());
        painelAviso.setBackground(new Color(0, 0, 0, 180)); // Preto com transparência (RGBA)
        painelAviso.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));
        painelAviso.setPreferredSize(new Dimension(400, 60)); // Tamanho do aviso

        // Lables para Título e Descrição
        lblTitulo = new JLabel("NOTIFICAÇÃO DO JOGO", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.YELLOW);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));

        lblDesc = new JLabel("Aguardando outros jogadores...", SwingConstants.CENTER);
        lblDesc.setForeground(Color.WHITE);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 14));

        painelAviso.add(lblTitulo, BorderLayout.NORTH);
        painelAviso.add(lblDesc, BorderLayout.CENTER);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        centroWrapper.add(painelAviso, gbc);

        painelAviso.setVisible(false);
        gameScreen.setVisible(true);
        isRendered = true;

        PlayerPositionPanels playerPositionPanels = new PlayerPositionPanels();
        playerPositionPanels.maiPanel = dealerPanel;

        setPlayerPositionPanels(AddPlayerPacket.POSITION_DEALER, playerPositionPanels);
    }

    public JLabel lblTitulo;
    public JLabel lblDesc;
    public JPanel painelAviso;

    public void setPlayerPositionText(String position, String name, String bet, String status) {
        PlayerPositionPanels playerPositionPanels = getPlayerPositionPanels(position);
        if (playerPositionPanels != null) {
            SwingUtilities.invokeLater(() -> {
                playerPositionPanels.namePanel.setText(name);
                playerPositionPanels.moneyPanel.setText(bet);
                playerPositionPanels.statusPanel.setText(status);
            });
        } else {
            System.out.println("Error: Player position '" + position + "' not found.");
        }
    }

    // Atributo global para rastrear o timer ativo e evitar concorrência
    private javax.swing.Timer activeDialogTimer = null;
    public boolean isRenderedDialog = false;

    public void showMessageDialog(String titulo, String descricao, int time) {
        SwingUtilities.invokeLater(() -> {
            // 1. Se já existir um timer rodando da mensagem anterior, cancela ele
            // imediatamente!
            if (activeDialogTimer != null && activeDialogTimer.isRunning()) {
                activeDialogTimer.stop();
            }

            // 2. Atualiza os textos (Não importa se é novo ou reuso, o texto deve mudar)
            lblTitulo.setText(titulo.toUpperCase());
            lblDesc.setText(descricao);

            // 3. Garante que o painel está visível
            if (!painelAviso.isVisible()) {
                painelAviso.setVisible(true);
                painelAviso.getParent().repaint();
            }
            isRenderedDialog = true;

            // 4. Se o tempo for zero ou negativo, o aviso fica na tela indefinidamente até
            // a próxima atualização
            if (time <= 0) {
                activeDialogTimer = null; // Limpa a referência do timer já que não há contagem
                return;
            }

            // 5. Cria e inicia o novo Timer, salvando a referência globalmente
            activeDialogTimer = new javax.swing.Timer(time, e -> {
                SwingUtilities.invokeLater(() -> {
                    painelAviso.setVisible(false);
                    isRenderedDialog = false;
                    activeDialogTimer = null; // Reseta a referência quando termina naturalmente
                });
            });

            activeDialogTimer.setRepeats(false);
            activeDialogTimer.start();
        });
    }

    public void setActionPanel(boolean visible) {
        // 1. Muda a visibilidade do painel
        this.actionControlPanel.setVisible(visible);

        // 2. Revalida o container pai (o sul), pois ele controla o layout vertical
        if (this.actionControlPanel.getParent() != null) {
            this.actionControlPanel.getParent().revalidate();
        }

        // 3. Repinta a tela toda
        this.gameScreen.repaint();
    }

    public void addPlayer(String pos, String name) {
        Font fonteLabels = new Font("Arial", Font.BOLD, 14);
        PlayerPositionPanels playerPositionPanels = new PlayerPositionPanels();
        setPlayerPositionPanels(pos, playerPositionPanels);
        JPanel panel = createPlayerPanel(pos, name, fonteLabels, Color.WHITE,
                pos.equalsIgnoreCase(AddPlayerPacket.POSITION_WEST) ||
                        pos.equalsIgnoreCase(AddPlayerPacket.POSITION_EAST));

        mainTablePanel.add(panel, pos);

        playerPositionPanels.maiPanel = panel;

        // deprecated
        switch (pos.toUpperCase()) {
            case "NORTH":
                northPlayerPanel = panel;
                break;
            case "SOUTH":
                southPlayerPanel = panel;

                break;
            case "EAST":
                eastPlayerPanel = panel;
                break;
            case "WEST":
                westPlayerPanel = panel;
                break;
            case "DEALER":

                break;
        }
    }

    public void removePlayer(String pos) {
        PlayerPositionPanels playerPositionPanels = getPlayerPositionPanels(pos);
        mainTablePanel.remove(playerPositionPanels.maiPanel);
        setPlayerPositionPanels(pos, null);
        mainTablePanel.revalidate();
        mainTablePanel.repaint();
    }

    public void renderCards(Player player) {
        for (Card card : player.getHand()) {
            addCardToPlayer(player.getPosition(), card.toCardGraphic());
        }
    }

    public void renderCard(Player player, Card card) {
        addCardToPlayer(player.getPosition(), card.toCardGraphic());
    }

    public HashMap<String, PlayerPositionPanels> playerPositionPanels = new HashMap<>();

    public PlayerPositionPanels getPlayerPositionPanels(String position) {
        return playerPositionPanels.get(position);
    }

    public void setPlayerPositionPanels(String position, PlayerPositionPanels playerPositionPanels) {
        this.playerPositionPanels.put(position, playerPositionPanels);
    }

    private JPanel createPlayerPanel(String pos, String name, Font font, Color textColor, boolean isRotated) {
        JPanel panel;
        PlayerPositionPanels playerPositionPanels = getPlayerPositionPanels(pos);

        if (isRotated) {
            panel = new JPanel() {
                @Override
                public java.awt.Component add(java.awt.Component comp) {
                    if (comp instanceof javax.swing.JComponent) {
                        ((javax.swing.JComponent) comp).setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
                    }
                    return super.add(comp);
                }
            };
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setPreferredSize(new Dimension(140, 240));
        } else {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            panel.setPreferredSize(new Dimension(240, 140));
        }
        panel.setOpaque(false);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);

        // 1. Label do Nome
        JLabel labelNome = new JLabel(name);
        labelNome.setFont(font);
        labelNome.setForeground(textColor);
        labelNome.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        infoPanel.add(labelNome);
        playerPositionPanels.namePanel = labelNome; // Mantém a sua referência
        infoPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 2)));
        JLabel labelValor = new JLabel("Pontos: 0"); // Mudei o texto para fazer sentido
        labelValor.setFont(new Font("Arial", Font.BOLD, 11));
        labelValor.setForeground(textColor);
        labelValor.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        infoPanel.add(labelValor);
        playerPositionPanels.moneyPanel = labelValor;
        infoPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 2)));
        JLabel labelStatus = new JLabel("Aguardando");
        labelStatus.setFont(new Font("Arial", Font.BOLD, 11));
        labelStatus.setForeground(textColor);
        labelStatus.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        infoPanel.add(labelStatus);
        playerPositionPanels.statusPanel = labelStatus;
        // Adiciona o bloco de textos completo dentro do painel principal do jogador
        panel.add(infoPanel);

        // Adiciona o espaço invisível entre as informações e onde as cartas vão entrar
        panel.add(javax.swing.Box.createRigidArea(new Dimension(5, 5)));

        return panel;
    }

    private JPanel createPlayerPanel2(String pos, String name, Font font, Color textColor, boolean isRotated) {
        JPanel panel;
        PlayerPositionPanels playerPositionPanels = getPlayerPositionPanels(pos);
        if (isRotated) {
            // Rotated players (West/East) stack cards vertically.
            // Since the cards themselves draw horizontally, vertical stacking makes them
            // look side-by-side!
            panel = new JPanel() {
                @Override
                public java.awt.Component add(java.awt.Component comp) {
                    if (comp instanceof javax.swing.JComponent) {
                        ((javax.swing.JComponent) comp).setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
                    }
                    return super.add(comp);
                }
            };
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setPreferredSize(new Dimension(140, 240)); // Adjusted vertical area for side-by-side rotated cards
        } else {
            // Regular players (North/South) use standard horizontal flow layout
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            panel.setPreferredSize(new Dimension(240, 140)); // Standard horizontal area
        }

        panel.setOpaque(false);

        JLabel label = new JLabel(name);
        label.setFont(font);
        label.setForeground(textColor);
        label.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        panel.add(label);
        playerPositionPanels.namePanel = label;
        label = new JLabel("Aaaaa");
        label.setFont(font);
        label.setForeground(textColor);
        label.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        panel.add(label);
        label = new JLabel("bbbb");
        label.setFont(font);
        label.setForeground(textColor);
        label.setAlignmentX(javax.swing.JComponent.CENTER_ALIGNMENT);
        panel.add(label);

        // Adds a tiny invisible space between the name and the cards
        panel.add(javax.swing.Box.createRigidArea(new Dimension(5, 5)));

        return panel;
    }

    public void addCardToPlayer(String position, CardGraphic cardGraphic) {

        PlayerPositionPanels playerPositionPanels = getPlayerPositionPanels(position);
        JPanel targetPanel = playerPositionPanels.maiPanel;
        if (targetPanel == null) {
            System.out.println("Error: Player position '" + position + "' not found.");
            return;
        }
        if (position.equalsIgnoreCase(AddPlayerPacket.POSITION_WEST)
                || position.equalsIgnoreCase(AddPlayerPacket.POSITION_EAST)) {
            cardGraphic.setRotacionada(true);
        } else {
            cardGraphic.setRotacionada(false);
        }
        JPanel card = cardGraphic.getPanel();
        playerPositionPanels.cardPanels.add(card);
        targetPanel.add(card);
        targetPanel.revalidate();
        targetPanel.repaint();
    }

    public void clearCards(String position) {
        System.out.println("limpando cards: " + position);
        PlayerPositionPanels playerPositionPanels = getPlayerPositionPanels(position);
        if (playerPositionPanels == null) {
            System.out.println("Error: Player clear cards position '" + position + "' not found.");
            return;
        }
        JPanel targetPanel = playerPositionPanels.maiPanel;
        if (targetPanel == null) {
            System.out.println("Error: Player clear cards position '" + position + "' not found.");
            return;
        }
        SwingUtilities.invokeLater(() -> {
            for (JPanel card : playerPositionPanels.cardPanels) {
                System.out.println("removendo de " + position + " card: " + card.getName());
                targetPanel.remove(card);
            }
            playerPositionPanels.cardPanels.clear();
            targetPanel.revalidate();
            targetPanel.repaint();
        });

    }

    private JPanel getPlayerPanelByPosition(String position) {
        System.out.println("pegando player panel: " + position);
        switch (position.toUpperCase()) {
            case "NORTH":
                return northPlayerPanel;
            case "SOUTH":
                return southPlayerPanel;
            case "WEST":
                return westPlayerPanel;
            case "EAST":
                return eastPlayerPanel;
            case "DEALER":
                return dealerPanel;
            default:
                return null;
        }
    }

}