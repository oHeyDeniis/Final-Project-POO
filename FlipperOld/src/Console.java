/**
 * Console — Utilitário estático de formatação visual do terminal.
 * Responsável por limpar tela, desenhar separadores e exibir títulos.
 * Pode ser usado por qualquer classe sem instanciar.
 */
public class Console {

    // Largura padrão da interface
    private static final int LARGURA = 50;

    /**
     * Limpa o terminal (funciona em sistemas que suportam ANSI).
     */
    public static void limpar() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Imprime uma linha separadora de "=" no terminal.
     */
    public static void linha() {
        System.out.println("=".repeat(LARGURA));
    }

    /**
     * Imprime uma linha separadora de "-" no terminal.
     */
    public static void linhaSub() {
        System.out.println("-".repeat(LARGURA));
    }

    /**
     * Exibe um título centralizado entre linhas separadoras.
     * @param texto Texto a ser exibido como título.
     */
    public static void titulo(String texto) {
        linha();
        int espacos = (LARGURA - texto.length()) / 2;
        String pad = " ".repeat(Math.max(0, espacos));
        System.out.println(pad + texto);
        linha();
    }

    /**
     * Exibe um subtítulo com linha inferior.
     * @param texto Texto do subtítulo.
     */
    public static void subtitulo(String texto) {
        System.out.println("\n  >> " + texto);
        linhaSub();
    }

    /**
     * Pausa a execução e aguarda o usuário pressionar Enter.
     */
    public static void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        try {
            System.in.read();
            // Consumir possível '\n' residual
            while (System.in.available() > 0) System.in.read();
        } catch (Exception e) {
            // Ignora exceção de leitura
        }
    }

    /**
     * Exibe uma mensagem de sucesso formatada.
     * @param mensagem Texto da mensagem.
     */
    public static void sucesso(String mensagem) {
        System.out.println("\n  [OK] " + mensagem);
    }

    /**
     * Exibe uma mensagem de erro formatada.
     * @param mensagem Texto da mensagem.
     */
    public static void erro(String mensagem) {
        System.out.println("\n  [ERRO] " + mensagem);
    }

    /**
     * Exibe uma mensagem de alerta formatada.
     * @param mensagem Texto da mensagem.
     */
    public static void alerta(String mensagem) {
        System.out.println("\n  [!] " + mensagem);
    }
}
