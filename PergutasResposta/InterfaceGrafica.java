import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InterfaceGrafica {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cards;

    private GerenciadorBD bd;
    private SistemaAposta aposta;
    private List<Pergunta> perguntas;
    private int index;
    private int pontos;
    private boolean emModoAposta;
    private String nomeJogador;

    // componentes de pergunta
    private JTextArea areaEnunciado;
    private JButton btnA, btnB, btnC, btnD, btnStop;
    private JLabel lblProgresso, lblSaldo, lblJogador;

    public InterfaceGrafica() {
        bd = new GerenciadorBD();
        aposta = new SistemaAposta(bd);
        nomeJogador = null;
    }

    public void criarInterface() {
        frame = new JFrame("Jogo de Perguntas - GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(telaSelecaoJogador(), "selecaoJogador");
        cards.add(menuPanel(), "menu");
        cards.add(jogoPanel(), "jogo");
        cards.add(resultadoPanel(), "resultado");

        frame.setContentPane(cards);
        frame.setVisible(true);
        cardLayout.show(cards, "selecaoJogador");
    }

    private JPanel telaSelecaoJogador() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        JPanel center = new JPanel(new GridLayout(0, 1, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));

        JLabel titulo = new JLabel("Bem-vindo ao Jogo", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        p.add(titulo, BorderLayout.NORTH);

        JLabel lblNomeLabel = new JLabel("Digite o seu nome:", SwingConstants.CENTER);
        JTextField tfNome = new JTextField(20);
        tfNome.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tfNome.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnEntrar = new JButton("Entrar no Jogo");
        btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEntrar.addActionListener(e -> {
            String nome = tfNome.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Por favor, digite um nome!");
                return;
            }
            nomeJogador = nome;
            bd.definirJogadorAtual(nomeJogador);
            aposta.definirJogadorAtual(nomeJogador);
            atualizarSaldo();
            cardLayout.show(cards, "menu");
        });

        center.add(lblNomeLabel);
        center.add(tfNome);
        center.add(Box.createVerticalStrut(20));
        center.add(btnEntrar);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> {
            bd.fecharConexao();
            System.exit(0);
        });

        p.add(center, BorderLayout.CENTER);
        p.add(btnSair, BorderLayout.SOUTH);

        return p;
    }

    private JPanel menuPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        JPanel center = new JPanel(new GridLayout(0, 1, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));

        JLabel titulo = new JLabel("Jogo de Perguntas", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        p.add(titulo, BorderLayout.NORTH);

        JButton btnJogar = new JButton("Jogar (Modo Normal)");
        btnJogar.addActionListener(e -> iniciarJogoNormal());
        JButton btnApostar = new JButton("Jogar (Modo com Apostas)");
        btnApostar.addActionListener(e -> iniciarJogoAposta());
        JButton btnRegras = new JButton("Ver Regras");
        btnRegras.addActionListener(e -> abrirRegras());
        JButton btnRecarregarSaldo = new JButton("Recarregar Saldo");
        btnRecarregarSaldo.addActionListener(e -> recarregarSaldo());
        JButton btnTrocarJogador = new JButton("Trocar Jogador");
        btnTrocarJogador.addActionListener(e -> cardLayout.show(cards, "selecaoJogador"));
        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> {
            bd.fecharConexao();
            System.exit(0);
        });

        center.add(btnJogar);
        center.add(btnApostar);
        center.add(btnRegras);
        center.add(btnRecarregarSaldo);
        center.add(btnTrocarJogador);
        center.add(btnSair);

        JPanel painelSaldo = new JPanel();
        painelSaldo.setLayout(new BoxLayout(painelSaldo, BoxLayout.Y_AXIS));
        lblJogador = new JLabel();
        lblJogador.setHorizontalAlignment(SwingConstants.CENTER);
        lblJogador.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSaldo = new JLabel();
        lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSaldo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        painelSaldo.add(lblJogador);
        painelSaldo.add(lblSaldo);
        p.add(painelSaldo, BorderLayout.SOUTH);

        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private JPanel jogoPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        areaEnunciado = new JTextArea();
        areaEnunciado.setWrapStyleWord(true);
        areaEnunciado.setLineWrap(true);
        areaEnunciado.setEditable(false);
        areaEnunciado.setFont(new Font("Serif", Font.PLAIN, 18));

        JScrollPane sc = new JScrollPane(areaEnunciado);
        p.add(sc, BorderLayout.CENTER);

        JPanel respostas = new JPanel(new GridLayout(2, 2, 8, 8));
        btnA = new JButton("A");
        btnB = new JButton("B");
        btnC = new JButton("C");
        btnD = new JButton("D");

        ActionListener responder = e -> {
            JButton b = (JButton) e.getSource();
            responderPergunta(b.getText().charAt(0));
        };

        btnA.addActionListener(responder);
        btnB.addActionListener(responder);
        btnC.addActionListener(responder);
        btnD.addActionListener(responder);

        respostas.add(btnA);
        respostas.add(btnB);
        respostas.add(btnC);
        respostas.add(btnD);

        p.add(respostas, BorderLayout.SOUTH);

        JPanel topo = new JPanel(new BorderLayout());
        lblProgresso = new JLabel("");
        topo.add(lblProgresso, BorderLayout.WEST);
        btnStop = new JButton("Parar e garantir");
        btnStop.setEnabled(false);
        btnStop.addActionListener(e -> { // garantir e encerrar
            if (aposta != null) {
                JOptionPane.showMessageDialog(frame, "Você garantiu: " + aposta.getApostaAtual() + " pontos.");
                aposta.finalizarAposta();
                aposta.salvarPontos();
            }
            cardLayout.show(cards, "resultado");
        });
        topo.add(btnStop, BorderLayout.EAST);
        p.add(topo, BorderLayout.NORTH);

        return p;
    }

    private JPanel resultadoPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea res = new JTextArea();
        res.setEditable(false);
        res.setFont(new Font("SansSerif", Font.PLAIN, 16));
        p.add(res, BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(e -> {
            atualizarSaldo();
            cardLayout.show(cards, "menu");
        });
        p.add(btnVoltar, BorderLayout.SOUTH);
        return p;
    }

    private void iniciarJogoNormal() {
        perguntas = bd.obterTodasPerguntas();
        Collections.shuffle(perguntas);
        if (perguntas.size() > 10)
            perguntas = new ArrayList<>(perguntas.subList(0, 10));
        perguntas.forEach(Pergunta::embaralharOpcoes);
        index = 0;
        pontos = 0;
        emModoAposta = false;
        cardLayout.show(cards, "jogo");
        mostrarPerguntaNormal();
    }

    private void iniciarJogoAposta() {
        // pedir valor de aposta via diálogo
        String s = JOptionPane.showInputDialog(frame, "Digite o valor que deseja apostar:", "Aposta",
                JOptionPane.PLAIN_MESSAGE);
        if (s == null) // cancelou
            return;
        int valor;
        try {
            valor = Integer.parseInt(s.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Valor inválido.");
            return;
        }
        if (!aposta.iniciarApostaValor(valor)) {
            JOptionPane.showMessageDialog(frame, "Saldo insuficiente ou valor inválido.");
            return;
        }
        perguntas = bd.obterTodasPerguntas();
        Collections.shuffle(perguntas);
        if (perguntas.size() > 10)
            perguntas = new ArrayList<>(perguntas.subList(0, 10));
        perguntas.forEach(Pergunta::embaralharOpcoes);
        index = 0;
        pontos = 0;
        btnStop.setEnabled(false);
        emModoAposta = true;
        cardLayout.show(cards, "jogo");
        mostrarPerguntaAposta();
    }

    private void mostrarPerguntaNormal() {
        if (index >= perguntas.size()) {
            mostrarResultadoNormal();
            return;
        }
        Pergunta p = perguntas.get(index);
        areaEnunciado.setText(p.getEnunciado() + "\n\nA) " + p.getOpcaoA() + "\nB) " + p.getOpcaoB() + "\nC) "
                + p.getOpcaoC() + "\nD) " + p.getOpcaoD());
        lblProgresso.setText("Pergunta " + (index + 1) + "/" + perguntas.size());
    }

    private void mostrarPerguntaAposta() {
        if (index >= perguntas.size()) {
            mostrarResultadoAposta();
            return;
        }
        Pergunta p = perguntas.get(index);
        areaEnunciado.setText(p.getEnunciado() + "\n\nA) " + p.getOpcaoA() + "\nB) " + p.getOpcaoB() + "\nC) "
                + p.getOpcaoC() + "\nD) " + p.getOpcaoD());
        lblProgresso.setText(
                "Pergunta " + (index + 1) + "/" + perguntas.size() + "  |  Aposta atual: " + aposta.getApostaAtual());
        // habilitar parar a partir de 5 acertos
        btnStop.setEnabled(aposta.podeParar());
    }

    private void responderPergunta(char alternativa) {
        Pergunta p = perguntas.get(index);
        boolean acertou = p.verificarResposta(alternativa);
        if (emModoAposta) {
            aposta.processarResposta(acertou);
            if (!acertou) {
                JOptionPane.showMessageDialog(frame,
                        "Você errou! Resultado final da aposta: " + aposta.getApostaAtual() + " pontos.");
                aposta.finalizarAposta();
                int opt = JOptionPane.showConfirmDialog(frame, "Deseja salvar os pontos no banco?", "Salvar",
                        JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION)
                    aposta.salvarPontos();
                cardLayout.show(cards, "resultado");
                return;
            }

            // acerto
            pontos++;
            index++;

            // se checkpoint (5 ou 7), oferecer parada
            if (aposta.isCheckpoint() && !aposta.isApostaInterrompida()) {
                int escolha = JOptionPane.showConfirmDialog(frame,
                        "Checkpoint alcançado! Deseja PARAR e garantir " + aposta.getValorGarantiaCheckpoint()
                                + " pontos?",
                        "Checkpoint", JOptionPane.YES_NO_OPTION);
                if (escolha == JOptionPane.YES_OPTION) {
                    aposta.interromperAposta();
                    aposta.finalizarAposta();
                    int opt = JOptionPane.showConfirmDialog(frame, "Deseja salvar os pontos no banco?", "Salvar",
                            JOptionPane.YES_NO_OPTION);
                    if (opt == JOptionPane.YES_OPTION)
                        aposta.salvarPontos();
                    cardLayout.show(cards, "resultado");
                    return;
                }
            }

            if (aposta.getAcertos() >= 10) {
                JOptionPane.showMessageDialog(frame, "Parabéns! Você completou 10 acertos.");
                aposta.finalizarAposta();
                int opt = JOptionPane.showConfirmDialog(frame, "Deseja salvar os pontos no banco?", "Salvar",
                        JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION)
                    aposta.salvarPontos();
                cardLayout.show(cards, "resultado");
                return;
            }

            mostrarPerguntaAposta();
            return;
        }

        // modo normal: erro encerra o jogo
        if (!acertou) {
            JOptionPane.showMessageDialog(frame, "Incorreto! Resposta correta: " + p.getRespostaCorreta());
            mostrarResultadoNormal();
            return;
        }

        // acerto no modo normal
        JOptionPane.showMessageDialog(frame, "Correto!");
        pontos++;
        index++;
        mostrarPerguntaNormal();
    }

    private void mostrarResultadoNormal() {
        JOptionPane.showMessageDialog(frame, "Fim do jogo! Pontuação: " + pontos + "/" + perguntas.size());
        cardLayout.show(cards, "menu");
    }

    private void mostrarResultadoAposta() {
        aposta.finalizarAposta();
        int opt = JOptionPane.showConfirmDialog(frame, "Deseja salvar os pontos no banco?", "Salvar",
                JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION)
            aposta.salvarPontos();
        cardLayout.show(cards, "resultado");
    }

    private void atualizarSaldo() {
        if (lblJogador != null && nomeJogador != null) {
            lblJogador.setText("Jogador: " + nomeJogador);
        }
        if (lblSaldo != null) {
            lblSaldo.setText("Saldo: " + aposta.getSaldoJogador() + " pontos");
        }
    }

    private void recarregarSaldo() {
        String s = JOptionPane.showInputDialog(frame, "Digite quanto deseja recarregar:", "Recarregar Saldo",
                JOptionPane.PLAIN_MESSAGE);
        if (s == null) {
            return;
        }
        try {
            int valor = Integer.parseInt(s.trim());
            if (valor <= 0) {
                JOptionPane.showMessageDialog(frame, "Valor inválido. Digite um número maior que zero.");
                return;
            }
            bd.recarregarSaldoJogador(valor);
            aposta.definirJogadorAtual(nomeJogador);
            atualizarSaldo();
            JOptionPane.showMessageDialog(frame,
                    "Saldo recarregado com sucesso! Novo saldo: " + aposta.getSaldoJogador() + " pontos.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Valor inválido. Digite um número inteiro.");
        }
    }

    private void abrirRegras() {
        try {
            File f = new File("regras_apostas.html");
            if (f.exists())
                Desktop.getDesktop().browse(f.toURI());
            else
                JOptionPane.showMessageDialog(frame, "Arquivo de regras não encontrado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Não foi possível abrir as regras: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceGrafica().criarInterface());
    }
}
