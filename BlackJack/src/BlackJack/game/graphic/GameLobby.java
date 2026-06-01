package BlackJack.game.graphic;

import javax.swing.*;

import BlackJack.ClientStarter;
import BlackJack.packet.client.DatagramClient;
import BlackJack.packet.client.JoinGamePacket;

import java.awt.*;
import java.util.TimerTask;
import java.util.Timer;

public class GameLobby {

    public static void main(String[] args) {

    }

    public JFrame lobbyScreen;

    // --- Campos de texto para capturar os dados (Globais para acesso no botão) ---
    private JTextField txtServerIp;
    private JTextField txtServerPort;
    private JTextField txtPlayerName;
    private JTextField txtPlayerMoney;
    private JButton btnConnect;
    public JLabel lblSubtitle;

    private ClientStarter clientStarter;

    public static GameLobby instance;

    public GameLobby(ClientStarter clientStarter) {
        this.clientStarter = clientStarter;
        instance = this;

    }

    public void renderLobby() {
        lobbyScreen = new JFrame("BlackJack - Copiladores");
        lobbyScreen.setSize(450, 550); // Formato mais verticalizado para formulário
        lobbyScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lobbyScreen.setLocationRelativeTo(null);
        lobbyScreen.setResizable(false);

        // --- Painel Principal (Fundo Verde Cassino) ---
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(7, 94, 23)); // Mesmo verde da sua mesa
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // Espaçamento entre os elementos
        gbc.gridx = 0;

        // --- 1. Título do Jogo ---
        JLabel lblTitle = new JLabel("CARD VELHOS CASSINO`S", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(new Color(212, 175, 55)); // Dourado clássico de cassino
        gbc.gridy = 0;
        mainPanel.add(lblTitle, gbc);

        lblSubtitle = new JLabel("BlackJack Multiplayer", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Arial", Font.ITALIC, 14));
        lblSubtitle.setForeground(Color.WHITE);
        gbc.gridy = 1;
        mainPanel.add(lblSubtitle, gbc);

        // Linha divisória estética
        gbc.gridy = 2;
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

        // --- 2. Grupo: Configurações de Rede (IP / Porta) ---
        JPanel networkPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        networkPanel.setOpaque(false);

        JLabel lblIp = createLobbyLabel("Server IP:");
        txtServerIp = createLobbyTextField("127.0.0.1"); // IP padrão local

        JLabel lblPort = createLobbyLabel("Port:");
        txtServerPort = createLobbyTextField("5000"); // Porta padrão baseada no seu log anterior

        networkPanel.add(lblIp);
        networkPanel.add(txtServerIp);
        networkPanel.add(lblPort);
        networkPanel.add(txtServerPort);

        gbc.gridy = 3;
        mainPanel.add(networkPanel, gbc);

        // --- 3. Grupo: Perfil do Jogador (Nome / Dinheiro) ---
        JPanel playerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        playerPanel.setOpaque(false);

        JLabel lblName = createLobbyLabel("Player Name:");
        txtPlayerName = createLobbyTextField("Player 1");

        JLabel lblMoney = createLobbyLabel("VOCE POSSUI $ 100:");
        // txtPlayerMoney = createLobbyTextField("1000"); // Dinheiro inicial padrão

        playerPanel.add(lblName);
        playerPanel.add(txtPlayerName);
        playerPanel.add(lblMoney);
        // playerPanel.add(txtPlayerMoney);

        gbc.gridy = 4;
        mainPanel.add(playerPanel, gbc);

        // Espaço antes do botão
        gbc.gridy = 5;
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        // --- 4. Botão de Conexão ---
        btnConnect = new JButton("ENTRAR");
        btnConnect.setFont(new Font("Arial", Font.BOLD, 16));
        btnConnect.setBackground(Color.WHITE);
        btnConnect.setForeground(new Color(7, 94, 23)); // Texto verde
        btnConnect.setFocusPainted(false);
        btnConnect.setPreferredSize(new Dimension(0, 45));

        // Evento de clique do botão conectar
        btnConnect.addActionListener(e -> handleConnectAction());

        gbc.gridy = 6;
        mainPanel.add(btnConnect, gbc);

        lobbyScreen.add(mainPanel);
        lobbyScreen.setVisible(true);
    }

    private JLabel createLobbyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createLobbyTextField(String defaultText) {
        JTextField textField = new JTextField(defaultText);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // Margem interna do texto
        ));
        return textField;
    }

    public boolean isConnected = false;

    private void handleConnectAction() {
        try {
            String ip = txtServerIp.getText().trim();
            String portStr = txtServerPort.getText().trim();
            String name = txtPlayerName.getText().trim();
            // String moneyStr = txtPlayerMoney.getText().trim();

            // Validações básicas de preenchimento
            if (ip.isEmpty() || portStr.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(lobbyScreen, "All fields must be filled!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int port = Integer.parseInt(portStr);
                // int money = Integer.parseInt(moneyStr);

                // Desabilita o botão para evitar cliques duplos enquanto conecta
                btnConnect.setEnabled(false);
                btnConnect.setText("CONNECTING...");

                if (clientStarter.server != null)
                    clientStarter.server.disconnect();
                clientStarter.server = new DatagramClient(ip, port, clientStarter.gameGraphic);
                Thread thread = new Thread(clientStarter.server);
                thread.start();
                JoinGamePacket joinGamePacket = new JoinGamePacket(name);
                clientStarter.server.sendPacketToServer(joinGamePacket);
                javax.swing.Timer activeDialogTimer = new javax.swing.Timer(10000, e -> {
                    SwingUtilities.invokeLater(() -> {
                        if (!this.isConnected) {
                            btnConnect.setEnabled(true);
                            btnConnect.setText("ENTRAR");
                            lblSubtitle.setText("Erro ao se conectar ao servidor. Tente novamente.");
                        }
                    });
                });

                activeDialogTimer.setRepeats(false);
                activeDialogTimer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(lobbyScreen, "Port and Chips fields must be valid numbers.", "Format Error",
                    JOptionPane.ERROR_MESSAGE);
            btnConnect.setEnabled(true);
            btnConnect.setText("JOIN TABLE");
        }
    }
}