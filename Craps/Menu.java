import java.util.Scanner;

public class Menu {
    private Scanner sc;

    public Menu() {
        this.sc = new Scanner(System.in);
    }

    public int exibirMenuPrincipal() {
        System.out.println("\n\n=== JOGO DE LANÇAMENTO DE DADOS ===");
        System.out.println(" 1- LANÇAR DADOS");
        System.out.println(" 2- ADICIONAR CRÉDITOS NA BANCA");
        System.out.println(" 3- REGRAS DO JOGO");
        System.out.println(" 4- VERIFICAR HISTÓRICO");
        System.out.println(" 5- SAIR\n");
        System.out.print("Escolha uma opção: ");

        int opcao = sc.nextInt();
        sc.nextLine();
        return opcao;
    }

    public float solicitarAposta() {
        System.out.println("\nDigite o valor da APOSTA. 'VAMOS VER SE VOCÊ TEM CORAGEM!'");
        System.out.print("Valor da Aposta: R$ ");
        float aposta = sc.nextFloat();
        sc.nextLine();
        return aposta;
    }

    public float solicitarRecarga() {
        System.out.println("\nQuantos R$ deseja colocar na banca?");
        System.out.print("Valor: R$ ");
        float valor = sc.nextFloat();
        sc.nextLine();
        return valor;
    }

    public int menuRecargaInsuficiente() {
        System.out.println("\nSem saldo para aposta. Deseja recarregar?");
        System.out.println("1- Fazer recarga");
        System.out.println("2- Sair");
        System.out.print("Opção: ");
        int opcao = sc.nextInt();
        sc.nextLine();
        return opcao;
    }

    public void exibirRegrasJogo() {
        System.out.println("\n=== REGRAS DO JOGO ===");
        System.out.println("Você lança dois dados. Cada dado tem seis faces que contêm um, dois, três, quatro,");
        System.out.println("cinco e seis pontos, respectivamente. Depois que os dados param de rolar, a soma");
        System.out.println("dos pontos nas faces viradas para cima é calculada.\n");
        System.out.println("- Se a soma for 7 (SEVEN) ou 11 (YO LEVEN) no primeiro lance, você ganha.");
        System.out.println("- Se a soma for 2 (SNAKE EYES), 3 (TREY) ou 12 (BOX CARS) no primeiro lance");
        System.out.println("  (chamado \"craps\"), você perde (isto é, a \"casa\" ganha).");
        System.out.println(
                "- Se a soma for 4, 5, 6, 8, 9 ou 10 no primeiro lance, essa soma torna-se sua \"pontuação\".\n");
        System.out.println("Para ganhar, você deve continuar a rolar os dados até \"fazer sua pontuação\"");
        System.out.println("(isto é, obter um valor igual à sua pontuação).");
        System.out.println("Você perde se obtiver um 7 antes de fazer sua pontuação.\n");
    }

    public void fechar() {
        if (sc != null) {
            sc.close();
        }
    }
}
