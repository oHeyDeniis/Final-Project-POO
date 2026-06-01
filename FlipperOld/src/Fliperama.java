import java.util.Arrays;
import java.util.List;

/**
 * Fliperama — Núcleo do sistema FlipperOld.
 * Controla o loop principal, exibe o menu e direciona para jogos e ranking.
 *
 * Composição:
 *  - List(Jogo): os três jogos disponíveis
 *  - Ranking: gerencia pontuações em memória e no banco
 *  - Menu: captura entradas do usuário
 */
public class Fliperama {

    // ─── Constantes de menu ───────────────────────────────
    private static final int OPCAO_SAIR = 0;
    private static final int OPCAO_JOGOS = 1;
    private static final int OPCAO_RANKING = 2;

    // ─── Composição ───────────────────────────────────────
    private List<Jogo> jogos;
    private Ranking ranking;
    private Menu menu;

    /**
     * Construtor — inicializa todos os jogos e dependências.
     */
    public Fliperama() {
        this.menu = new Menu();
        this.ranking = new Ranking();
        this.jogos = Arrays.asList(
                new ImparOuPar(),
                new CaraOuCoroa(),
                new Vinte()
        );
    }

    /**
     * Inicia o loop principal do sistema.
     * Exibe o menu e direciona conforme a opção escolhida.
     */
    public void iniciar() {
        Console.limpar();
        exibirBoasVindas();

        int opcao = -1;
        while (opcao != OPCAO_SAIR) {
            exibirMenuPrincipal();
            opcao = menu.capturarOpcao("  Escolha: ");

            Console.limpar();

            switch (opcao) {
                case OPCAO_JOGOS:
                    menuJogos();
                    break;

                case OPCAO_RANKING:
                    menuRanking();
                    break;

                case OPCAO_SAIR:
                    encerrar();
                    break;

                default:
                    Console.erro("Opção inválida. Escolha entre 0 e 2.");
                    menu.pausar();
            }
        }
    }

    // ─── Telas ────────────────────────────────────────────

    /**
     * Exibe a tela de boas-vindas ao abrir o programa.
     */
    private void exibirBoasVindas() {
        Console.linha();
        System.out.println("  ______ _                          ____  _     _ ");
        System.out.println(" |  ____| (_)                      / __ \\| |   | |");
        System.out.println(" | |__  | |_ _ __  _ __   ___ _ __| |  | | | __| |");
        System.out.println(" |  __| | | | '_ \\| '_ \\ / _ \\ '__| |  | | |/ _` |");
        System.out.println(" | |    | | | |_) | |_) |  __/ |  | |__| | | (_| |");
        System.out.println(" |_|    |_|_| .__/| .__/ \\___|_|   \\____/|_|\\__,_|");
        System.out.println("            | |   | |                             ");
        System.out.println("            |_|   |_|    Fliperama Digital         ");                           
        Console.linha();
        System.out.println("  Bem-vindo ao FlipperOld!");
        System.out.println("  Os clássicos de sempre, no seu terminal.\n");
        menu.pausar();
        Console.limpar();
    }

    /**
     * Exibe o menu principal.
     */
    private void exibirMenuPrincipal() {
        Console.titulo("FLIPPEROLD - MENU PRINCIPAL");

        System.out.println("  1. Jogos");
        System.out.println("  2. Rankings");
        System.out.println("  0. Sair");

        Console.linhaSub();
    }

    // ─── Menu de Jogos ────────────────────────────────────

    /**
     * Exibe o submenu de jogos.
     */
    private void menuJogos() {
        int opcao = -1;

        while (opcao != OPCAO_SAIR) {

            Console.titulo("JOGOS");

            for (int i = 0; i < jogos.size(); i++) {
                System.out.printf("  %d. %s%n", i + 1, jogos.get(i).getNome());
            }

            System.out.println("  0. Voltar");
            Console.linhaSub();

            opcao = menu.capturarOpcao("  Escolha o jogo: ");

            Console.limpar();

            if (opcao >= 1 && opcao <= jogos.size()) {
                executarJogo(opcao - 1);
            } else if (opcao != OPCAO_SAIR) {
                Console.erro("Opção inválida.");
                menu.pausar();
                Console.limpar();
            }
        }

        Console.limpar();
    }

    // ─── Execução de jogo ─────────────────────────────────

    /**
     * Executa o jogo selecionado pelo índice da lista.
     * Após encerrar, pergunta se o jogador quer salvar a pontuação.
     * @param indice Índice do jogo na lista (0-based).
     */
    private void executarJogo(int indice) {
        Jogo jogo = jogos.get(indice);
        Console.limpar();

        jogo.iniciar();
        jogo.jogar(menu);
        jogo.encerrar();

        System.out.printf("%n  Pontuação obtida: %d pts%n", jogo.getPontuacao());
        Console.linhaSub();

        // Pergunta se quer salvar
        if (menu.capturarSimNao("  Deseja salvar sua pontuação no ranking?")) {
            String nome = menu.capturarTexto("  Seu nome: ");
            Pontuacao p = new Pontuacao(nome, jogo.getNome(), jogo.getPontuacao());
            ranking.salvar(p);
            Console.sucesso("Pontuação salva! Boa sorte no ranking, " + nome + "!");
        } else {
            Console.alerta("Pontuação não salva.");
        }

        menu.pausar();
        Console.limpar();
    }

    // ─── Menu de Rankings ─────────────────────────────────

    /**
     * Exibe o menu de consulta de rankings por jogo.
     */
    private void menuRanking() {
        int opcao = -1;

        while (opcao != OPCAO_SAIR) {

            Console.titulo("RANKINGS");

            for (int i = 0; i < jogos.size(); i++) {
                System.out.printf("  %d. %s%n", i + 1, jogos.get(i).getNome());
            }

            System.out.println("  0. Voltar");
            Console.linhaSub();

            opcao = menu.capturarOpcao("  Escolha o Ranking que deseja ver: ");

            if (opcao >= 1 && opcao <= jogos.size()) {
                Console.limpar();
                ranking.exibir(jogos.get(opcao - 1).getNome());
                menu.pausar();
                Console.limpar();
            } else if (opcao != OPCAO_SAIR) {
                Console.erro("Opção inválida.");
                menu.pausar();
                Console.limpar();
            }
        }

        Console.limpar();
    }

    // ─── Encerramento ─────────────────────────────────────

    /**
     * Encerra o programa liberando recursos.
     */
    private void encerrar() {
        Console.titulo("ATÉ LOGO!");
        System.out.println("  Obrigado por jogar no FlipperOld.");
        System.out.println("  Volte sempre!\n");

        ranking.fechar();
        menu.fechar();
    }
}