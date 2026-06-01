import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class JogoPerguntas {
    private GerenciadorBD bd;
    private List<Pergunta> perguntas;
    private int pontuacao;
    private int perguntaAtual;
    private Scanner scanner;
    private SistemaAposta aposta;
    private boolean emModoAposta;
    private String nomeJogador;

    public JogoPerguntas() {
        this.bd = new GerenciadorBD();
        this.perguntas = new ArrayList<>();
        this.pontuacao = 0;
        this.perguntaAtual = 0;
        this.scanner = new Scanner(System.in);
        this.aposta = new SistemaAposta(bd);
        this.emModoAposta = false;
        this.nomeJogador = null;
    }

    public void iniciarJogo() {
        selecionarJogador();
        exibirMenuPrincipal();
    }

    private void selecionarJogador() {
        System.out.print("Digite o nome do jogador: ");
        nomeJogador = scanner.nextLine().trim();
        if (nomeJogador.isEmpty()) {
            System.out.println("Nome inválido. Tente novamente.");
            selecionarJogador();
            return;
        }
        bd.definirJogadorAtual(nomeJogador);
        aposta.definirJogadorAtual(nomeJogador);
        System.out
                .println("Bem-vindo, " + nomeJogador + "! Seu saldo atual é: " + aposta.getSaldoJogador() + " pontos.");
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   BEM-VINDO AO JOGO DE PERGUNTAS!      ║");
        System.out.println("║   Jogador: " + nomeJogador + "");
        System.out.println("║   Saldo: " + aposta.getSaldoJogador() + " pontos");
        System.out.println("║   Décadas 70, 80 e 90                  ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\nEscolha uma opção:");
        System.out.println("1 - Jogar com TODAS as perguntas");
        System.out.println("2 - Jogar apenas perguntas dos ANOS 70");
        System.out.println("3 - Jogar apenas perguntas dos ANOS 80");
        System.out.println("4 - Jogar apenas perguntas dos ANOS 90");
        System.out.println("5 - Jogar COM APOSTAS");
        System.out.println("6 - Recarregar saldo");
        System.out.println("0 - Sair");
        System.out.print("\nDigite sua escolha: ");

        int opcao = obterInteiro();

        switch (opcao) {
            case 1:
                emModoAposta = false;
                selecionarPerguntas(0); // 0 = todas
                break;
            case 2:
                emModoAposta = false;
                selecionarPerguntas(1970);
                break;
            case 3:
                emModoAposta = false;
                selecionarPerguntas(1980);
                break;
            case 4:
                emModoAposta = false;
                selecionarPerguntas(1990);
                break;
            case 5:
                emModoAposta = true;
                selecionarPerguntas(0); // Apostas com todas as perguntas
                break;
            case 6:
                recarregarSaldo();
                exibirMenuPrincipal();
                return;
            case 0:
                sair();
                return;
            default:
                System.out.println("Opção inválida!");
                exibirMenuPrincipal();
                return;
        }

        if (!perguntas.isEmpty()) {
            jogar();
        }
    }

    private void recarregarSaldo() {
        System.out.print("\nDigite quanto deseja recarregar: ");
        int valor = obterInteiro();
        if (valor <= 0) {
            System.out.println("Valor inválido. Digite um valor maior que zero.");
            return;
        }
        bd.recarregarSaldoJogador(valor);
        aposta.definirJogadorAtual(nomeJogador);
        System.out.println("Saldo recarregado! Seu novo saldo é: " + aposta.getSaldoJogador() + " pontos.");
    }

    private void selecionarPerguntas(int decada) {
        if (decada == 0) {
            perguntas = bd.obterTodasPerguntas();
            System.out.println("\n✓ Modo: TODAS AS PERGUNTAS");
        } else {
            perguntas = bd.obterPerguntasPorDecada(decada);
            System.out.println("\n✓ Modo: PERGUNTAS DOS ANOS " + decada);
        }

        Collections.shuffle(perguntas);

        if (perguntas.size() > 10) {
            perguntas = new ArrayList<>(perguntas.subList(0, 10));
        }

        perguntas.forEach(Pergunta::embaralharOpcoes);
        System.out.println("✓ Total de perguntas: " + perguntas.size());

        if (perguntas.isEmpty()) {
            System.out.println("Nenhuma pergunta disponível para essa opção!");
            exibirMenuPrincipal();
        }
    }

    private void jogar() {
        pontuacao = 0;
        perguntaAtual = 0;

        // Se modo aposta, iniciar aposta e limitar a 10 perguntas
        if (emModoAposta) {
            if (!aposta.iniciarAposta(scanner)) {
                exibirMenuPrincipal();
                return;
            }
            // Limita a 10 perguntas no modo aposta
            if (perguntas.size() > 10) {
                perguntas = new ArrayList<>(perguntas.subList(0, 10));
            }
        }

        System.out.println("\n" + "=".repeat(40));
        System.out.println("INICIANDO O JOGO!");
        System.out.println("=".repeat(40));

        while (perguntaAtual < perguntas.size()) {
            Pergunta p = perguntas.get(perguntaAtual);
            p.exibir(perguntaAtual + 1);

            System.out.println("\nProgresso: " + (perguntaAtual + 1) + "/" + perguntas.size());
            System.out.print("Digite sua resposta (A/B/C/D): ");

            String resposta = scanner.nextLine().trim().toUpperCase();

            if (resposta.length() != 1 || !resposta.matches("[A-D]")) {
                System.out.println(" Entrada inválida! Digite apenas A, B, C ou D.");
                continue;
            }

            boolean acertou = p.verificarResposta(resposta.charAt(0));

            if (emModoAposta) {
                aposta.processarResposta(acertou);

                if (!acertou) {
                    System.out.println("\n★ JOGO ENCERRADO - ERRO DETECTADO!");
                    perguntaAtual = perguntas.size();
                } else {
                    pontuacao++;
                    perguntaAtual++;

                    if (aposta.isCheckpoint() && !aposta.isApostaInterrompida()) {
                        int garantia = aposta.getValorGarantiaCheckpoint();
                        System.out.print(
                                "\nCheckpoint alcançado! Deseja PARAR e garantir " + garantia + " pontos? (S/N): ");
                        String parar = scanner.nextLine().trim().toUpperCase();
                        if (parar.equals("S")) {
                            System.out.println("\nVocê optou por parar. Encerrando aposta...");
                            aposta.interromperAposta();
                            perguntaAtual = perguntas.size();
                        }
                    }
                }

                if (aposta.getAcertos() >= 10) {
                    perguntaAtual = perguntas.size();
                }
            } else {
                if (acertou) {
                    System.out.println("✓ CORRETO! +1 ponto");
                    pontuacao++;
                    perguntaAtual++;
                } else {
                    System.out.println("✗ INCORRETO! A resposta correta é: " + p.getRespostaCorreta());
                    perguntaAtual = perguntas.size();
                }
                System.out.println("\nPontuação atual: " + pontuacao);
            }

            if (perguntaAtual < perguntas.size()) {
                System.out.print("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }
        }

        if (emModoAposta) {
            exibirResultadoFinalComAposta();
        } else {
            exibirResultadoFinal();
        }
    }

    private void exibirResultadoFinal() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("FIM DO JOGO!");
        System.out.println("=".repeat(40));

        int percentual = (pontuacao * 100) / perguntas.size();

        System.out.println("\nResultado Final:");
        System.out.println("Pontuação: " + pontuacao + "/" + perguntas.size());
        System.out.println("Percentual: " + percentual + "%");

        exibirDesempenho(percentual);

        System.out.print("\nDeseja jogar novamente? (S/N): ");
        String resposta = scanner.nextLine().trim().toUpperCase();

        if (resposta.equals("S")) {
            exibirMenuPrincipal();
        } else {
            sair();
        }
    }

    private void exibirResultadoFinalComAposta() {
        aposta.finalizarAposta();

        System.out.print("\nDeseja SALVAR os pontos? (S/N): ");
        String salvarResposta = scanner.nextLine().trim().toUpperCase();

        if (salvarResposta.equals("S")) {
            aposta.salvarPontos();
        } else {
            System.out.println("✗ Pontos não foram salvos.");
        }

        System.out.print("\nDeseja CONTINUAR APOSTANDO? (S/N): ");
        String continuarResposta = scanner.nextLine().trim().toUpperCase();

        if (continuarResposta.equals("S")) {
            exibirMenuPrincipal();
        } else {
            sair();
        }
    }

    private void exibirDesempenho(int percentual) {
        if (percentual >= 90) {
            System.out.println(" EXCELENTE! Você é um especialista!");
        } else if (percentual >= 70) {
            System.out.println(" BOM! Muito bom conhecimento!");
        } else if (percentual >= 50) {
            System.out.println(" RAZOÁVEL! Continue estudando!");
        } else {
            System.out.println(" PRECISA ESTUDAR! Tente novamente!");
        }
    }

    private int obterInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.print("Entrada inválida! Digite um número: ");
            return obterInteiro();
        }
    }

    private void sair() {
        System.out.println("\nObrigado por jogar! Até logo!");
        bd.fecharConexao();
        System.exit(0);
    }

    public static void main(String[] args) {
        JogoPerguntas jogo = new JogoPerguntas();
        jogo.iniciarJogo();
    }
}
