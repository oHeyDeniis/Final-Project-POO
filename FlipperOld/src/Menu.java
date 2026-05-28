import java.util.List;
import java.util.Scanner;

/**
 * Menu — Responsável por toda a interação de exibição e captura
 * de entradas do usuário via console.
 * Centraliza o uso do Scanner para evitar múltiplas instâncias.
 */
public class Menu {

    private Scanner scanner;

    /**
     * Construtor — inicializa o Scanner com System.in.
     */
    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Exibe uma lista de opções numeradas com título.
     * @param titulo  Título do menu.
     * @param opcoes  Lista de opções a exibir.
     */
    public void exibirOpcoes(String titulo, List<String> opcoes) {
        Console.titulo(titulo);
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, opcoes.get(i));
        }
        System.out.println("  0. Sair / Voltar");
        Console.linhaSub();
    }

    /**
     * Lê e retorna um número inteiro digitado pelo usuário.
     * Continua pedindo até receber um inteiro válido.
     * @param mensagem Mensagem exibida antes da entrada.
     * @return Inteiro digitado.
     */
    public int capturarOpcao(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            scanner.next();
            Console.erro("Opção inválida. Digite um número.");
            System.out.print(mensagem);
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // consumir '\n' residual
        return valor;
    }

    /**
     * Lê e retorna um texto digitado pelo usuário.
     * Rejeita entradas vazias.
     * @param mensagem Mensagem exibida antes da entrada.
     * @return Texto não vazio digitado pelo usuário.
     */
    public String capturarTexto(String mensagem) {
        String valor = "";
        while (valor.trim().isEmpty()) {
            System.out.print(mensagem);
            valor = scanner.nextLine();
            if (valor.trim().isEmpty()) {
                Console.erro("Entrada não pode ser vazia.");
            }
        }
        return valor.trim();
    }

    /**
     * Lê e retorna uma resposta S/N do usuário.
     * @param mensagem Mensagem exibida antes da entrada.
     * @return true se "S", false se "N".
     */
    public boolean capturarSimNao(String mensagem) {
        while (true) {
            System.out.print(mensagem + " (S/N): ");
            String resp = scanner.nextLine().trim().toUpperCase();
            if (resp.equals("S")) return true;
            if (resp.equals("N")) return false;
            Console.erro("Digite S para Sim ou N para Não.");
        }
    }

    /**
     * Aguarda o usuário pressionar Enter para continuar.
     */
    public void pausar() {
        Console.pausar();
    }

    /**
     * Fecha o Scanner ao encerrar o programa.
     */
    public void fechar() {
        scanner.close();
    }
}
