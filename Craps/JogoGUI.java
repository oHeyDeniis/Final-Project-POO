import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Random;

public class JogoGUI extends JFrame {
    private Jogo jogo;
    private JLabel saldoLabel;
    private JLabel resultado1Label;
    private JLabel resultado2Label;
    private JLabel somaLabel;
    private JLabel mensagemLabel;
    private JLabel dice1IconLabel;
    private JLabel dice2IconLabel;
    private ImageIcon[] diceIcons;
    private ImageIcon rollingIcon;
    private JButton lancarButton;
    private JButton adicionarCreditoButton;
    private JButton regrasButton;
    private JButton sairButton;
    private JPanel painelPrincipal;
    private JPanel painelDados;
    private JPanel painelApostas;
    private DecimalFormat formatoMoeda = new DecimalFormat("0.00");
    private Random random = new Random();
    private String[] provocacoesVitoria = {
            "Boa, mas não comemore demais ainda... quero ver se você repete.",
            "Você venceu dessa vez. Agora prova que consegue manter a sorte.",
            "Não foi fácil, hein? Vamos ver se a banca aguenta o seu jogo.",
            "Ganhou! Será que a próxima rodada é sua também?"
    };
    private String[] provocacoesPerda = {
            "E aí? Vai continuar tentando ou vai fugir agora?",
            "Mais sorte na próxima... se você tiver coragem pra isso.",
            "Aposta perdida. Quer tentar provar que não foi só azar?",
            "Perdeu dessa vez. A banca está de olho no seu próximo movimento."
    };
    private String[] provocacoesIntermediaria = {
            "Metade da aposta de volta, mas o desafio continua.",
            "Só metade retornou. Quer virar esse jogo?",
            "Não perdeu tudo, mas também não ganhou. Você vai encarar outra?",
            "A banca devolveu metade. Agora decide se continua ou recua."
    };

    public JogoGUI() {
        setTitle(" JOGO DE DADOS ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setFont(new Font("Arial", Font.PLAIN, 12));

        jogo = new Jogo();
        carregarIconesDados();

        // Painel Principal
        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBackground(new Color(45, 45, 48));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel de Saldo
        JPanel painelSaldo = criarPainelSaldo();
        painelPrincipal.add(painelSaldo);
        painelPrincipal.add(Box.createVerticalStrut(20));

        // Painel de Dados
        painelDados = criarPainelDados();
        painelPrincipal.add(painelDados);
        painelPrincipal.add(Box.createVerticalStrut(20));

        // Painel de Mensagens
        JPanel painelMsg = criarPainelMensagem();
        painelPrincipal.add(painelMsg);
        painelPrincipal.add(Box.createVerticalStrut(20));

        // Painel de Apostas
        painelApostas = criarPainelApostas();
        painelPrincipal.add(painelApostas);
        painelPrincipal.add(Box.createVerticalStrut(20));

        // Painel de Botões
        JPanel painelBotoes = criarPainelBotoes();
        painelPrincipal.add(painelBotoes);

        add(painelPrincipal);
        solicitarNomeJogador();
        setVisible(true);
    }

    private JPanel criarPainelSaldo() {
        JPanel painel = new JPanel();
        painel.setLayout(new FlowLayout(FlowLayout.CENTER));
        painel.setBackground(new Color(60, 60, 65));
        painel.setBorder(BorderFactory.createLineBorder(new Color(100, 200, 255), 2));

        saldoLabel = new JLabel("Saldo: R$ 0,00");
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        saldoLabel.setForeground(new Color(100, 200, 255));

        painel.add(saldoLabel);
        return painel;
    }

    private JPanel criarPainelDados() {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(1, 3, 15, 0));
        painel.setBackground(new Color(45, 45, 48));
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 200, 255), 2),
                "RESULTADO DOS DADOS",
                0, 0,
                new Font("Arial", Font.BOLD, 18),
                new Color(100, 200, 255)));

        // Dado 1
        JPanel painelDado1 = new JPanel();
        painelDado1.setBackground(new Color(60, 60, 65));
        painelDado1.setBorder(BorderFactory.createLineBorder(new Color(255, 150, 0), 2));
        painelDado1.setLayout(new BoxLayout(painelDado1, BoxLayout.Y_AXIS));
        JLabel labelDado1 = new JLabel("DADO 1");
        labelDado1.setForeground(new Color(255, 150, 0));
        labelDado1.setFont(new Font("Arial", Font.BOLD, 12));
        labelDado1.setAlignmentX(Component.CENTER_ALIGNMENT);
        dice1IconLabel = new JLabel(rollingIcon);
        dice1IconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultado1Label = new JLabel("—");
        resultado1Label.setForeground(new Color(255, 200, 100));
        resultado1Label.setFont(new Font("Arial", Font.BOLD, 24));
        resultado1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelDado1.add(Box.createVerticalGlue());
        painelDado1.add(labelDado1);
        painelDado1.add(Box.createVerticalStrut(10));
        painelDado1.add(dice1IconLabel);
        painelDado1.add(Box.createVerticalStrut(10));
        painelDado1.add(resultado1Label);
        painelDado1.add(Box.createVerticalGlue());

        // Soma
        JPanel painelSoma = new JPanel();
        painelSoma.setBackground(new Color(60, 60, 65));
        painelSoma.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 100), 2));
        painelSoma.setLayout(new BoxLayout(painelSoma, BoxLayout.Y_AXIS));
        JLabel labelSoma = new JLabel("SOMA");
        labelSoma.setForeground(new Color(0, 255, 100));
        labelSoma.setFont(new Font("Arial", Font.BOLD, 12));
        labelSoma.setAlignmentX(Component.CENTER_ALIGNMENT);
        somaLabel = new JLabel("—");
        somaLabel.setForeground(new Color(100, 255, 150));
        somaLabel.setFont(new Font("Arial", Font.BOLD, 48));
        somaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelSoma.add(Box.createVerticalGlue());
        painelSoma.add(labelSoma);
        painelSoma.add(somaLabel);
        painelSoma.add(Box.createVerticalGlue());

        // Dado 2
        JPanel painelDado2 = new JPanel();
        painelDado2.setBackground(new Color(60, 60, 65));
        painelDado2.setBorder(BorderFactory.createLineBorder(new Color(255, 150, 0), 2));
        painelDado2.setLayout(new BoxLayout(painelDado2, BoxLayout.Y_AXIS));
        JLabel labelDado2 = new JLabel("DADO 2");
        labelDado2.setForeground(new Color(255, 150, 0));
        labelDado2.setFont(new Font("Arial", Font.BOLD, 12));
        labelDado2.setAlignmentX(Component.CENTER_ALIGNMENT);
        dice2IconLabel = new JLabel(rollingIcon);
        dice2IconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultado2Label = new JLabel("—");
        resultado2Label.setForeground(new Color(255, 200, 100));
        resultado2Label.setFont(new Font("Arial", Font.BOLD, 24));
        resultado2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelDado2.add(Box.createVerticalGlue());
        painelDado2.add(labelDado2);
        painelDado2.add(Box.createVerticalStrut(10));
        painelDado2.add(dice2IconLabel);
        painelDado2.add(Box.createVerticalStrut(10));
        painelDado2.add(resultado2Label);
        painelDado2.add(Box.createVerticalGlue());

        painel.add(painelDado1);
        painel.add(painelSoma);
        painel.add(painelDado2);

        return painel;
    }

    private JPanel criarPainelMensagem() {
        JPanel painel = new JPanel();
        painel.setBackground(new Color(45, 45, 48));
        painel.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 255), 1));

        mensagemLabel = new JLabel("<html><center>Bem-vindo ao Jogo de Dados!</center></html>");
        mensagemLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        mensagemLabel.setForeground(new Color(200, 200, 255));
        mensagemLabel.setHorizontalAlignment(SwingConstants.CENTER);

        painel.add(mensagemLabel);
        return painel;
    }

    private JPanel criarPainelApostas() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(45, 45, 48));
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 100, 100), 2),
                "FAZER APOSTA",
                0, 0,
                new Font("Arial", Font.BOLD, 18),
                new Color(255, 100, 100)));

        JPanel painelInput = new JPanel();
        painelInput.setBackground(new Color(45, 45, 48));
        painelInput.setLayout(new BoxLayout(painelInput, BoxLayout.X_AXIS));

        JLabel labelAposta = new JLabel("Valor da Aposta (R$): ");
        labelAposta.setFont(new Font("Arial", Font.PLAIN, 20));
        labelAposta.setForeground(new Color(200, 200, 200));

        JTextField apostaTextField = new JTextField(10);
        apostaTextField.setFont(new Font("Arial", Font.PLAIN, 20));
        apostaTextField.setBackground(new Color(60, 60, 65));
        apostaTextField.setForeground(new Color(200, 200, 255));
        apostaTextField.setCaretColor(new Color(200, 200, 255));

        lancarButton = new JButton("LANÇAR DADOS");
        lancarButton.setFont(new Font("Arial", Font.BOLD, 20));
        lancarButton.setBackground(new Color(0, 150, 100));
        lancarButton.setForeground(Color.WHITE);
        lancarButton.setFocusPainted(false);
        lancarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lancarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    float aposta = Float.parseFloat(apostaTextField.getText());
                    if (aposta <= 0) {
                        JOptionPane.showMessageDialog(JogoGUI.this,
                                "A aposta deve ser maior que zero!",
                                "Aposta Inválida",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (!jogo.getBanca().fazerAposta(aposta)) {
                        int resposta = JOptionPane.showConfirmDialog(JogoGUI.this,
                                "Saldo insuficiente. Deseja adicionar créditos?",
                                "Saldo Insuficiente",
                                JOptionPane.YES_NO_OPTION);
                        if (resposta == JOptionPane.YES_OPTION) {
                            abrirDialogoRecarga();
                        }
                        return;
                    }

                    lancarDados(aposta);
                    apostaTextField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(JogoGUI.this,
                            "Digite um valor válido!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        painelInput.add(labelAposta);
        painelInput.add(apostaTextField);
        painelInput.add(Box.createHorizontalStrut(10));
        painelInput.add(lancarButton);

        painel.add(painelInput);
        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(1, 3, 10, 0));
        painel.setBackground(new Color(45, 45, 48));

        adicionarCreditoButton = new JButton("ADICIONAR CRÉDITOS");
        adicionarCreditoButton.setFont(new Font("Arial", Font.BOLD, 20));
        adicionarCreditoButton.setBackground(new Color(0, 100, 200));
        adicionarCreditoButton.setForeground(Color.WHITE);
        adicionarCreditoButton.setFocusPainted(false);
        adicionarCreditoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adicionarCreditoButton.addActionListener(e -> abrirDialogoRecarga());

        regrasButton = new JButton("REGRAS DO JOGO");
        regrasButton.setFont(new Font("Arial", Font.BOLD, 20));
        regrasButton.setBackground(new Color(200, 100, 0));
        regrasButton.setForeground(Color.WHITE);
        regrasButton.setFocusPainted(false);
        regrasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        regrasButton.addActionListener(e -> exibirRegras());

        sairButton = new JButton("SAIR");
        sairButton.setFont(new Font("Arial", Font.BOLD, 20));
        sairButton.setBackground(new Color(200, 50, 50));
        sairButton.setForeground(Color.WHITE);
        sairButton.setFocusPainted(false);
        sairButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sairButton.addActionListener(e -> System.exit(0));

        painel.add(adicionarCreditoButton);
        painel.add(regrasButton);
        painel.add(sairButton);

        return painel;
    }

    private void lancarDados(float aposta) {
        int soma = jogo.lancarDados();
        int dado1 = jogo.getDado1().getValor();
        int dado2 = jogo.getDado2().getValor();

        // Desabilita o botão durante a animação
        lancarButton.setEnabled(false);

        // Inicia a animação dos dados
        animarDados(dado1, dado2, soma, aposta);
    }

    private void animarDados(int dado1Final, int dado2Final, int somaFinal, float aposta) {
        final int[] contador = { 0 };
        final int duracao = 30; // Número de frames de animação

        javax.swing.Timer timer = new javax.swing.Timer(50, null);
        timer.addActionListener(e -> {
            contador[0]++;

            // Mostra imagens de dados aleatórias durante a animação
            int randomFace1 = random.nextInt(6) + 1;
            int randomFace2 = random.nextInt(6) + 1;
            dice1IconLabel.setIcon(diceIcons[randomFace1]);
            dice2IconLabel.setIcon(diceIcons[randomFace2]);
            resultado1Label.setText(String.valueOf(randomFace1));
            resultado2Label.setText(String.valueOf(randomFace2));
            somaLabel.setText(String.valueOf(random.nextInt(11) + 2));

            // Após a animação, mostra o resultado final
            if (contador[0] >= duracao) {
                ((javax.swing.Timer) e.getSource()).stop();

                dice1IconLabel.setIcon(diceIcons[dado1Final]);
                dice2IconLabel.setIcon(diceIcons[dado2Final]);
                resultado1Label.setText(String.valueOf(dado1Final));
                resultado2Label.setText(String.valueOf(dado2Final));
                somaLabel.setText(String.valueOf(somaFinal));

                String mensagem = jogo.processarResultado(somaFinal, aposta);
                mensagem = adicionarProvocacao(mensagem);
                mensagemLabel.setText(mensagem);
                if (mensagem.contains("GANHOU")) {
                    mensagemLabel.setForeground(new Color(100, 255, 150));
                } else {
                    mensagemLabel.setForeground(new Color(255, 100, 100));
                }

                jogo.salvarSaldo();
                atualizarSaldo();
                lancarButton.setEnabled(true);
            }
        });
        timer.start();
    }

    private String adicionarProvocacao(String mensagemBase) {
        String provocacao = "";
        if (mensagemBase.contains("GANHOU")) {
            provocacao = provocacoesVitoria[random.nextInt(provocacoesVitoria.length)];
        } else if (mensagemBase.contains("metade da aposta devolvida") || mensagemBase.contains("Metade da aposta")) {
            provocacao = provocacoesIntermediaria[random.nextInt(provocacoesIntermediaria.length)];
        } else {
            provocacao = provocacoesPerda[random.nextInt(provocacoesPerda.length)];
        }
        return "<html><center>" + mensagemBase + "<br><br>" + provocacao + "</center></html>";
    }

    private void abrirDialogoRecarga() {
        String valor = JOptionPane.showInputDialog(this,
                "Quanto você deseja adicionar? (R$)",
                "Adicionar Créditos",
                JOptionPane.QUESTION_MESSAGE);

        if (valor != null && !valor.isEmpty()) {
            try {
                float recarga = Float.parseFloat(valor);
                if (recarga > 0) {
                    jogo.getBanca().adicionarCredito(recarga);
                    jogo.salvarSaldo();
                    atualizarSaldo();
                    mensagemLabel.setText("✅ Créditos adicionados com sucesso!");
                    mensagemLabel.setForeground(new Color(100, 255, 150));
                } else {
                    JOptionPane.showMessageDialog(this,
                            "O valor deve ser maior que zero!",
                            "Valor Inválido",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Digite um valor válido!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void solicitarNomeJogador() {
        String nome = null;
        while (nome == null || nome.isBlank()) {
            nome = JOptionPane.showInputDialog(this,
                    "Digite o seu nome:",
                    "Bem-vindo",
                    JOptionPane.QUESTION_MESSAGE);
            if (nome == null) {
                System.exit(0);
            }
            nome = nome.trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, digite um nome válido.",
                        "Nome Inválido",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        jogo.definirJogador(nome);
        jogo.registrarSessaoInicial();
        atualizarSaldo();
    }

    private void exibirRegras() {
        String regras = "=== REGRAS DO JOGO ===\n\n" +
                "1. Faça uma aposta antes de lançar os dados\n" +
                "2. Clique em 'LANÇAR DADOS' para rolar os dados\n" +
                "3. Se a SOMA for 7 ou 11: VOCÊ GANHA o dobro da aposta\n" +
                "4. Se a SOMA for 2, 3 ou 12: VOCÊ PERDE a aposta\n" +
                "5. Se a SOMA for 4, 5, 6, 8, 9 ou 10: MAIS SORTE NA PRÓXIMA e\n" +
                "   metade da aposta é devolvida\n" +
                "6. Você pode adicionar créditos a qualquer momento\n" +
                "7. O saldo nunca pode ficar negativo\n\n" +
                "BOA SORTE! 🍀";

        JTextArea textArea = new JTextArea(regras);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setBackground(new Color(60, 60, 65));
        textArea.setForeground(new Color(200, 200, 255));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 250));

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Regras do Jogo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void atualizarSaldo() {
        saldoLabel.setText("Saldo: R$ " + formatoMoeda.format(jogo.getBanca().getSaldo()));
    }

    private void carregarIconesDados() {
        diceIcons = new ImageIcon[7];
        for (int i = 1; i <= 6; i++) {
            diceIcons[i] = new ImageIcon(criarImagemDado(i, 120, 120));
        }
        rollingIcon = new ImageIcon(criarImagemDado(0, 120, 120));
    }

    private Image criarImagemDado(int face, int largura, int altura) {
        BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagem.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(240, 240, 240));
        g.fillRoundRect(0, 0, largura, altura, 25, 25);
        g.setColor(new Color(40, 40, 40));
        g.setStroke(new BasicStroke(5f));
        g.drawRoundRect(4, 4, largura - 9, altura - 9, 25, 25);

        if (face == 0) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String texto = "...";
            FontMetrics fm = g.getFontMetrics();
            int x = (largura - fm.stringWidth(texto)) / 2;
            int y = (altura + fm.getAscent()) / 2 - fm.getDescent();
            g.drawString(texto, x, y);
        } else {
            int raio = 18;
            int offset = 28;
            int centerX = largura / 2;
            int centerY = altura / 2;
            int leftX = offset;
            int rightX = largura - offset;
            int topY = offset;
            int bottomY = altura - offset;

            g.setColor(new Color(40, 40, 40));
            switch (face) {
                case 1:
                    desenharPonto(g, centerX, centerY, raio);
                    break;
                case 2:
                    desenharPonto(g, leftX, topY, raio);
                    desenharPonto(g, rightX, bottomY, raio);
                    break;
                case 3:
                    desenharPonto(g, leftX, topY, raio);
                    desenharPonto(g, centerX, centerY, raio);
                    desenharPonto(g, rightX, bottomY, raio);
                    break;
                case 4:
                    desenharPonto(g, leftX, topY, raio);
                    desenharPonto(g, rightX, topY, raio);
                    desenharPonto(g, leftX, bottomY, raio);
                    desenharPonto(g, rightX, bottomY, raio);
                    break;
                case 5:
                    desenharPonto(g, leftX, topY, raio);
                    desenharPonto(g, rightX, topY, raio);
                    desenharPonto(g, centerX, centerY, raio);
                    desenharPonto(g, leftX, bottomY, raio);
                    desenharPonto(g, rightX, bottomY, raio);
                    break;
                case 6:
                    desenharPonto(g, leftX, topY, raio);
                    desenharPonto(g, rightX, topY, raio);
                    desenharPonto(g, leftX, centerY, raio);
                    desenharPonto(g, rightX, centerY, raio);
                    desenharPonto(g, leftX, bottomY, raio);
                    desenharPonto(g, rightX, bottomY, raio);
                    break;
            }
        }

        g.dispose();
        return imagem;
    }

    private void desenharPonto(Graphics2D g, int x, int y, int raio) {
        g.fillOval(x - raio / 2, y - raio / 2, raio, raio);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JogoGUI());
    }
}
