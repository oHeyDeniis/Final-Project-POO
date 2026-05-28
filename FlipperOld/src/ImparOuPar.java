import java.util.Random;

/**
 * ImparOuPar — Jogo de disputa entre jogador e máquina.
 *
 * Regras:
 *  - O jogador escolhe "ímpar" ou "par" e informa um número de 1 a 10.
 *  - A máquina assume automaticamente o lado OPOSTO ao jogador.
 *  - A máquina sorteia seu número de 1 a 10.
 *  - A SOMA dos dois números determina o resultado: par ou ímpar.
 *  - Quem acertou a paridade vence a rodada.
 *  - São jogadas 5 rodadas. Pontuação = 100 pts por vitória.
 */
public class ImparOuPar extends Jogo {

    // ─── Constantes ───────────────────────────────────────
    private static final int TOTAL_RODADAS = 5;
    private static final int PONTOS_POR_VITORIA = 100;

    // ─── Atributos da partida ─────────────────────────────
    private String escolhaJogador;   // "impar" ou "par"
    private String escolhaMaquina;   // oposto do jogador
    private int numeroJogador;
    private int numeroMaquina;
    private int rodadasJogadas;
    private int vitoriasJogador;

    private Random random;

    /**
     * Construtor — inicializa o jogo com nome definido na superclasse.
     */
    public ImparOuPar() {
        super("Ímpar ou Par");
        this.random = new Random();
    }

    @Override
    public void iniciar() {
        rodadasJogadas = 0;
        vitoriasJogador = 0;
        setPontuacao(0);

        Console.titulo("ÍMPAR OU PAR");
        System.out.println("  Regras:");
        System.out.println("  - Escolha ÍMPAR ou PAR.");
        System.out.println("  - Digite um número de 1 a 10.");
        System.out.println("  - A máquina sorteia o lado oposto e seu número.");
        System.out.println("  - A SOMA define o vencedor da rodada.");
        System.out.println("  - São " + TOTAL_RODADAS + " rodadas. Boa sorte!\n");
    }

    @Override
    public void jogar(Menu menu) {
        while (rodadasJogadas < TOTAL_RODADAS) {
            rodadasJogadas++;
            Console.subtitulo("Rodada " + rodadasJogadas + " de " + TOTAL_RODADAS);

            // Captura escolha do jogador
            System.out.println("  Escolha: 1 - ÍMPAR  |  2 - PAR");
            int opcao = menu.capturarOpcao("  Sua escolha: ");
            while (opcao != 1 && opcao != 2) {
                Console.erro("Escolha 1 para ÍMPAR ou 2 para PAR.");
                opcao = menu.capturarOpcao("  Sua escolha: ");
            }
            escolhaJogador = (opcao == 1) ? "impar" : "par";
            // Máquina assume o oposto
            escolhaMaquina = escolhaJogador.equals("impar") ? "par" : "impar";

            // Captura número do jogador (1 a 10)
            numeroJogador = 0;
            while (numeroJogador < 1 || numeroJogador > 10) {
                numeroJogador = menu.capturarOpcao("  Seu número (1 a 10): ");
                if (numeroJogador < 1 || numeroJogador > 10) {
                    Console.erro("Digite um número entre 1 e 10.");
                }
            }

            // Máquina sorteia seu número
            numeroMaquina = random.nextInt(10) + 1;

            // Calcula soma e resultado
            int soma = numeroJogador + numeroMaquina;
            String resultadoSoma = (soma % 2 == 0) ? "par" : "impar";

            // Exibe jogadas
            Console.linhaSub();
            System.out.printf("  Você    → escolheu %-6s | número: %d%n", escolhaJogador.toUpperCase(), numeroJogador);
            System.out.printf("  Máquina → escolheu %-6s | número: %d%n", escolhaMaquina.toUpperCase(), numeroMaquina);
            System.out.printf("  Soma: %d + %d = %d (%s)%n", numeroJogador, numeroMaquina, soma, resultadoSoma.toUpperCase());
            Console.linhaSub();

            // Determina vencedor da rodada
            if (resultadoSoma.equals(escolhaJogador)) {
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
