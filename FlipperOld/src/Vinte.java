import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Vinte — Jogo de cartas 21 (Blackjack simplificado) contra a máquina.
 *
 * Regras:
 *  - O sistema distribui 2 cartas para o jogador e 2 para a máquina.
 *  - Cartas valem seu número (1-10). J, Q, K valem 10. Ás vale 11.
 *  - O jogador decide pedir mais cartas ou parar.
 *  - Ultrapassar 21 é derrota imediata (estouro).
 *  - A máquina joga após o jogador: pede carta se total < 17, para se >= 17.
 *  - Quem chegar mais perto de 21 sem ultrapassar vence.
 *  - Pontuação = 21 - |21 - totalJogador| * 10 (se vencer), ou 0 (se perder).
 */
public class Vinte extends Jogo {

    // ─── Atributos da partida ─────────────────────────────
    private List<Integer> cartasJogador;
    private List<Integer> cartasMaquina;
    private int totalJogador;
    private int totalMaquina;

    private Random random;

    /**
     * Construtor — inicializa o jogo com nome definido na superclasse.
     */
    public Vinte() {
        super("21");
        this.random = new Random();
        this.cartasJogador = new ArrayList<>();
        this.cartasMaquina = new ArrayList<>();
    }

    /**
     * Sorteia uma carta (valor de 1 a 11).
     * @return Valor da carta sorteada.
     */
    private int sortearCarta() {
        int carta = random.nextInt(13) + 1; // 1 a 13
        if (carta > 10) return 10;           // J, Q, K valem 10
        if (carta == 1) return 11;            // Ás vale 11
        return carta;
    }

    /**
     * Calcula e retorna a soma das cartas de uma mão.
     * @param cartas Lista de cartas.
     * @return Total da mão.
     */
    private int calcularTotal(List<Integer> cartas) {
        return cartas.stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Formata a lista de cartas para exibição.
     * @param cartas Lista de cartas.
     * @return String formatada.
     */
    private String exibirCartas(List<Integer> cartas) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cartas.size(); i++) {
            sb.append(cartas.get(i));
            if (i < cartas.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void iniciar() {
        cartasJogador.clear();
        cartasMaquina.clear();
        totalJogador = 0;
        totalMaquina = 0;
        setPontuacao(0);

        Console.titulo("21 — BLACKJACK");
        System.out.println("  Regras:");
        System.out.println("  - Tente chegar o mais perto possível de 21.");
        System.out.println("  - Ultrapassar 21 é derrota imediata (estouro).");
        System.out.println("  - A máquina para quando seu total for >= 17.");
        System.out.println("  - J, Q, K valem 10. Ás vale 11.\n");

        // Distribui cartas iniciais
        cartasJogador.add(sortearCarta());
        cartasJogador.add(sortearCarta());
        cartasMaquina.add(sortearCarta());
        cartasMaquina.add(sortearCarta());
    }

    @Override
    public void jogar(Menu menu) {
        // ── Turno do Jogador ──────────────────────────────
        boolean jogoAtivo = true;

        while (jogoAtivo) {
            totalJogador = calcularTotal(cartasJogador);
            Console.subtitulo("Suas cartas");
            System.out.printf("  Cartas: %-20s Total: %d%n", exibirCartas(cartasJogador), totalJogador);
            System.out.printf("  Máquina mostra: [%d, ?]%n%n", cartasMaquina.get(0));

            // Verifica estouro
            if (totalJogador > 21) {
                Console.erro("Estouro! Você ultrapassou 21.");
                jogoAtivo = false;
                break;
            }

            // Verifica blackjack
            if (totalJogador == 21) {
                Console.sucesso("21! Blackjack!");
                jogoAtivo = false;
                break;
            }

            // Pergunta se quer mais carta
            boolean pedirCarta = menu.capturarSimNao("  Deseja pedir mais uma carta?");
            if (!pedirCarta) {
                jogoAtivo = false;
            } else {
                int novaCarta = sortearCarta();
                cartasJogador.add(novaCarta);
                System.out.printf("%n  Carta recebida: %d%n", novaCarta);
            }
        }

        totalJogador = calcularTotal(cartasJogador);

        // ── Turno da Máquina (somente se jogador não estourou) ──
        if (totalJogador <= 21) {
            Console.subtitulo("Vez da máquina");
            System.out.printf("  Cartas da máquina: %s  Total: %d%n",
                    exibirCartas(cartasMaquina), calcularTotal(cartasMaquina));

            // Máquina pede cartas enquanto total < 17
            while (calcularTotal(cartasMaquina) < 17) {
                int novaCarta = sortearCarta();
                cartasMaquina.add(novaCarta);
                System.out.printf("  Máquina pediu carta: %d → Total: %d%n",
                        novaCarta, calcularTotal(cartasMaquina));
            }

            totalMaquina = calcularTotal(cartasMaquina);
            System.out.printf("%n  Máquina parou com total: %d%n", totalMaquina);
        }
    }

    @Override
    public void encerrar() {
        Console.titulo("RESULTADO FINAL");
        System.out.printf("  Suas cartas:    %-20s Total: %d%n", exibirCartas(cartasJogador), totalJogador);

        int pontuacaoFinal = 0;
        String mensagem;

        if (totalJogador > 21) {
            mensagem = "Estouro! Você perdeu.";
            pontuacaoFinal = 0;
        } else if (totalMaquina > 21) {
            System.out.printf("  Cartas máquina: %-20s Total: %d (ESTOURO)%n",
                    exibirCartas(cartasMaquina), totalMaquina);
            mensagem = "A máquina estourou! Você venceu!";
            pontuacaoFinal = (21 - Math.abs(21 - totalJogador)) * 10;
        } else {
            System.out.printf("  Cartas máquina: %-20s Total: %d%n",
                    exibirCartas(cartasMaquina), totalMaquina);
            if (totalJogador > totalMaquina) {
                mensagem = "Você venceu!";
                pontuacaoFinal = (21 - Math.abs(21 - totalJogador)) * 10;
            } else if (totalJogador == totalMaquina) {
                mensagem = "Empate!";
                pontuacaoFinal = 50;
            } else {
                mensagem = "A máquina venceu!";
                pontuacaoFinal = 0;
            }
        }

        setPontuacao(pontuacaoFinal);
        Console.linhaSub();
        System.out.printf("  Pontuação final: %d pts%n%n", pontuacaoFinal);
        if (pontuacaoFinal > 0 || mensagem.contains("Empate")) {
            Console.sucesso(mensagem);
        } else {
            System.out.println("  " + mensagem);
        }
    }
}
