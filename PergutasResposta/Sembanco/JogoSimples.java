package Sembanco;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class JogoSimples {
    private RepositorioPerguntas repositorio;
    private List<PerguntaSimples> perguntas;
    private int pontuacao;
    private int perguntaAtual;
    private Scanner scanner;

    public JogoSimples() {
        this.repositorio = new RepositorioPerguntas();
        this.perguntas = new ArrayList<>();
        this.pontuacao = 0;
        this.perguntaAtual = 0;
        this.scanner = new Scanner(System.in);
    }

    public void iniciarJogo() {
        exibirMenuPrincipal();
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   BEM-VINDO AO JOGO DE PERGUNTAS!      ║");
        System.out.println("║   Décadas 70, 80 e 90                  ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\nEscolha uma opção:");
        System.out.println("1 - Jogar com TODAS as perguntas");
        System.out.println("2 - Jogar apenas perguntas dos ANOS 70");
        System.out.println("3 - Jogar apenas perguntas dos ANOS 80");
        System.out.println("4 - Jogar apenas perguntas dos ANOS 90");
        System.out.println("0 - Sair");
        System.out.print("\nDigite sua escolha: ");

        int opcao = obterInteiro();

        switch (opcao) {
            case 1:
                selecionarPerguntas(0); // 0 = todas
                break;
            case 2:
                selecionarPerguntas(1970);
                break;
            case 3:
                selecionarPerguntas(1980);
                break;
            case 4:
                selecionarPerguntas(1990);
                break;
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

    private void selecionarPerguntas(int decada) {
        if (decada == 0) {
            perguntas = repositorio.obterTodasPerguntas();
            System.out.println("\n✓ Modo: TODAS AS PERGUNTAS");
        } else {
            perguntas = repositorio.obterPerguntasPorDecada(decada);
            System.out.println("\n✓ Modo: PERGUNTAS DOS ANOS " + decada);
        }

        Collections.shuffle(perguntas);
        System.out.println("✓ Total de perguntas: " + perguntas.size());

        if (perguntas.isEmpty()) {
            System.out.println("Nenhuma pergunta disponível para essa opção!");
            exibirMenuPrincipal();
        }
    }

    private void jogar() {
        pontuacao = 0;
        perguntaAtual = 0;

        System.out.println("\n" + "=".repeat(40));
        System.out.println("INICIANDO O JOGO!");
        System.out.println("=".repeat(40));

        while (perguntaAtual < perguntas.size()) {
            PerguntaSimples p = perguntas.get(perguntaAtual);
            p.exibir();

            System.out.println("\nProgresso: " + (perguntaAtual + 1) + "/" + perguntas.size());
            System.out.print("Digite sua resposta (A/B/C/D): ");

            String resposta = scanner.nextLine().trim().toUpperCase();

            if (resposta.length() != 1 || !resposta.matches("[A-D]")) {
                System.out.println("❌ Entrada inválida! Digite apenas A, B, C ou D.");
                continue;
            }

            if (p.verificarResposta(resposta.charAt(0))) {
                System.out.println("✓ CORRETO! +1 ponto");
                pontuacao++;
            } else {
                System.out.println("✗ INCORRETO! A resposta correta é: " + p.getRespostaCorreta());
            }

            perguntaAtual++;
            System.out.println("\nPontuação atual: " + pontuacao);

            if (perguntaAtual < perguntas.size()) {
                System.out.print("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }
        }

        exibirResultadoFinal();
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

    private void exibirDesempenho(int percentual) {
        if (percentual >= 90) {
            System.out.println("⭐ EXCELENTE! Você é um especialista!");
        } else if (percentual >= 70) {
            System.out.println("🌟 BOM! Muito bom conhecimento!");
        } else if (percentual >= 50) {
            System.out.println("👍 RAZOÁVEL! Continue estudando!");
        } else {
            System.out.println("📚 PRECISA ESTUDAR! Tente novamente!");
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
        System.out.println("\nObrigado por jogar! Até logo! 👋");
        System.exit(0);
    }

    public static void main(String[] args) {
        JogoSimples jogo = new JogoSimples();
        jogo.iniciarJogo();
    }
}
