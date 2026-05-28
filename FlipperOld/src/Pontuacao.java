/**
 * Pontuacao — Representa o registro de uma partida encerrada e salva.
 * Criada pelo Fliperama ao final de cada sessão que o jogador decide salvar.
 * Atributos somente leitura após a construção — sem setters.
 */
public class Pontuacao {

    private String nomeJogador;
    private String nomeJogo;
    private int valor;

    /**
     * Construtor — inicializa todos os atributos obrigatórios.
     * @param nomeJogador Nome informado pelo jogador.
     * @param nomeJogo    Nome do jogo que gerou a pontuação.
     * @param valor       Pontuação obtida na partida.
     */
    public Pontuacao(String nomeJogador, String nomeJogo, int valor) {
        this.nomeJogador = nomeJogador;
        this.nomeJogo = nomeJogo;
        this.valor = valor;
    }

    // ─── Getters ──────────────────────────────────────────

    public String getNomeJogador() {
        return nomeJogador;
    }

    public String getNomeJogo() {
        return nomeJogo;
    }

    public int getValor() {
        return valor;
    }

    /**
     * Representação formatada do registro para exibição no placar.
     * @param posicao Posição no ranking.
     * @return String formatada com posição, nome e pontuação.
     */
    public String exibirPlacador(int posicao) {
        return String.format("  %2d. %-20s %6d pts", posicao, nomeJogador, valor);
    }

    @Override
    public String toString() {
        return String.format("Pontuacao{jogador='%s', jogo='%s', valor=%d}",
                nomeJogador, nomeJogo, valor);
    }
}
