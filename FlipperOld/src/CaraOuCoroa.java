import java.util.Random;

/**
 * CaraOuCoroa — Jogo de sorte entre jogador e máquina.
 *
 * Regras:
 * - O jogador escolhe CARA ou COROA.
 * - A máquina assume automaticamente o lado OPOSTO.
 * - O sistema sorteia o resultado da moeda.
 * - Quem acertou vence a rodada.
 * - São jogadas 5 rodadas. Pontuação = 100 pts por vitória.
 */
public class CaraOuCoroa extends Jogo {

    // ─── Constantes ───────────────────────────────────────
    private static final int TOTAL_RODADAS = 5;
    private static final int PONTOS_POR_VITORIA = 100;

    // ─── Atributos da partida ─────────────────────────────
    private String escolhaJogador; // "cara" ou "coroa"
    private String escolhaMaquina; // oposto do jogador
    private int rodadasJogadas;
    private int vitoriasJogador;

    private Random random;

    /**
     * Construtor — inicializa o jogo com nome definido na superclasse.
     */
    public CaraOuCoroa() {
        super("Cara ou Coroa");
        this.random = new Random();
    }

    @Override
    public void iniciar() {
        rodadasJogadas = 0;
        vitoriasJogador = 0;
        setPontuacao(0);

        Console.titulo("CARA OU COROA");
        System.out.println("  Regras:");
        System.out.println("  - Escolha CARA ou COROA.");
        System.out.println("  - A máquina assume o lado oposto.");
        System.out.println("  - O sistema lança a moeda e revela o resultado.");
        System.out.println("  - São " + TOTAL_RODADAS + " rodadas. Boa sorte!\n");
    }

    @Override
    public void jogar(Menu menu) {
        while (rodadasJogadas < TOTAL_RODADAS) {
            rodadasJogadas++;
            Console.subtitulo("Rodada " + rodadasJogadas + " de " + TOTAL_RODADAS);

            // Captura escolha do jogador
            System.out.println("  Escolha: 1 - CARA  |  2 - COROA");
            int opcao = menu.capturarOpcao("  Sua escolha: ");
            while (opcao != 1 && opcao != 2) {
                Console.erro("Escolha 1 para CARA ou 2 para COROA.");
                opcao = menu.capturarOpcao("  Sua escolha: ");
            }
            escolhaJogador = (opcao == 1) ? "cara" : "coroa";
            // Máquina assume o oposto
            escolhaMaquina = escolhaJogador.equals("cara") ? "coroa" : "cara";

            // Sistema lança a moeda
            String resultado = (random.nextInt(2) == 0) ? "cara" : "coroa";

            // Exibe jogadas
            Console.linhaSub();
            System.out.printf("  Você    → escolheu %s%n", escolhaJogador.toUpperCase());
            System.out.printf("  Máquina → escolheu %s%n", escolhaMaquina.toUpperCase());
            System.out.printf("  Moeda   → %s%n", resultado.toUpperCase());
            Console.linhaSub();

            // Determina vencedor
            if (resultado.equals(escolhaJogador)) {
                vitoriasJogador++;
                Console.sucesso("Você venceu esta rodada! (" + vitoriasJogador + " vitória(s))");
            } else {
                System.out.println("\n  A máquina venceu esta rodada.");
            }

            System.out.println();
        }
    }

    @Override
    public void encerrar() {
        int pontuacaoFinal = vitoriasJogador * PONTOS_POR_VITORIA;
        setPontuacao(pontuacaoFinal);

        Console.titulo("RESULTADO FINAL");
        System.out.printf("  Vitórias suas:    %d de %d rodadas%n", vitoriasJogador, TOTAL_RODADAS);
        System.out.printf("  Vitórias máquina: %d de %d rodadas%n", TOTAL_RODADAS - vitoriasJogador, TOTAL_RODADAS);
        Console.linhaSub();
        System.out.printf("  Pontuação final:  %d pts%n%n", pontuacaoFinal);

        if (vitoriasJogador > TOTAL_RODADAS / 2) {
            Console.sucesso("Você venceu o jogo!");
        } else if (vitoriasJogador == TOTAL_RODADAS / 2) {
            Console.alerta("Empate!");
        } else {
            System.out.println("\n  A máquina venceu. Tente novamente!");
        }
    }
}
