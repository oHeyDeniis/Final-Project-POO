/**
 * Jogo — Classe abstrata que define o contrato de qualquer jogo do FlipperOld.
 * Todo jogo deve implementar iniciar(), jogar() e encerrar().
 * Atributos nome e pontuacao são privados e acessados via getters.
 */
public abstract class Jogo {

    // ─── Atributos privados ───────────────────────────────
    private String nome;
    private int pontuacao;

    /**
     * Construtor — define o nome do jogo ao instanciar a subclasse.
     * @param nome Nome do jogo.
     */
    public Jogo(String nome) {
        this.nome = nome;
        this.pontuacao = 0;
    }

    // ─── Getters ──────────────────────────────────────────

    public String getNome() {
        return nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    /**
     * Permite que as subclasses definam a pontuação ao encerrar a partida.
     * Acesso protegido — apenas subclasses podem chamar.
     * @param pontuacao Valor calculado pela lógica do jogo.
     */
    protected void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    // ─── Métodos abstratos (contrato de cada jogo) ────────

    /**
     * Prepara o estado inicial do jogo: exibe regras, zera variáveis.
     */
    public abstract void iniciar();

    /**
     * Executa a lógica completa da partida, incluindo o comportamento
     * da máquina adversária quando necessário.
     * @param menu Menu para capturar entradas do jogador.
     */
    public abstract void jogar(Menu menu);

    /**
     * Finaliza a partida, calcula e define a pontuação final.
     */
    public abstract void encerrar();
}
