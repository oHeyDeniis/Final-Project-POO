import java.util.Scanner;

public class Jogo {
    private Dado dado1;
    private Dado dado2;
    private Banca banca;
    private Menu menu;
    private BancoDadosJogador bancoDadosJogador;
    static Scanner sc = new Scanner(System.in);

    public Jogo() {
        this.dado1 = new Dado();
        this.dado2 = new Dado();
        this.banca = new Banca();
        this.menu = new Menu();
        this.bancoDadosJogador = new BancoDadosJogador();
    }

    public int lancarDados() {
        dado1.lancar();
        dado2.lancar();
        int resultado = dado1.getValor() + dado2.getValor();
        return resultado;
    }

    public Dado getDado1() {
        return dado1;
    }

    public Dado getDado2() {
        return dado2;
    }

    public Banca getBanca() {
        return banca;
    }

    public void iniciar() {
        System.out.println("=== BEM-VINDO AO JOGO DE DADOS ===");
        System.out.println("Vamos jogar?\n");
        inicializarJogador();

        int menuOpcao;

        do {
            menuOpcao = menu.exibirMenuPrincipal();

            switch (menuOpcao) {
                case 1:
                    executarJogo();
                    break;
                case 2:
                    adicionarCreditos();
                    break;
                case 3:
                    menu.exibirRegrasJogo();
                    break;
                case 4:
                    exibirHistorico();
                    break;
                case 5:
                    System.out.println("\n=== FIM DO JOGO ===");
                    System.out.println("Obrigado por jogar!");
                    bancoDadosJogador.fecharConexao();
                    menu.fechar();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (menuOpcao != 5);
    }

    private void adicionarCreditos() {
        float valor = menu.solicitarRecarga();
        banca.adicionarCredito(valor);
        bancoDadosJogador.atualizarSaldoJogador(Math.round(banca.getSaldo()));
        banca.exibirSaldo();
        Utilitario.pausa();
    }

    private void executarJogo() {
        boolean continuarJogando = true;

        while (continuarJogando) {
            if (!banca.temSaldo()) {
                int opcao = menu.menuRecargaInsuficiente();
                if (opcao == 1) {
                    adicionarCreditos();
                } else {
                    continuarJogando = false;
                    break;
                }
            }

            float aposta = menu.solicitarAposta();

            if (!banca.fazerAposta(aposta)) {
                continue;
            }

            Utilitario.pausa();
            System.out.println("\nSerá que você está com sorte???");
            Utilitario.pausa();

            int resultado = lancarDados();

            processarResultado(resultado, aposta);
            bancoDadosJogador.atualizarSaldoJogador(Math.round(banca.getSaldo()));
            banca.exibirSaldo();

            // Perguntar se deseja continuar
            System.out.println("\nDeseja continuar apostando? (s/n): ");

            String resposta = sc.nextLine().toLowerCase();
            if (!resposta.equals("s")) {
                continuarJogando = false;
            }
        }
    }

    private void inicializarJogador() {
        System.out.print("Digite o nome do jogador: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty()) {
            System.out.println("Nome inválido. Tente novamente.");
            inicializarJogador();
            return;
        }
        definirJogador(nome);
        bancoDadosJogador.salvarHistoricoSessao(Math.round(banca.getSaldo()));
        System.out.println("Bem-vindo, " + nome + "! Saldo inicial: R$ " + String.format("%.2f", banca.getSaldo()));
    }

    private void exibirHistorico() {
        System.out.println("\n=== HISTÓRICO DE SESSÕES ===");
        var historico = bancoDadosJogador.obterHistoricoJogador();
        if (historico.isEmpty()) {
            System.out.println("Nenhum histórico encontrado para este jogador.");
        } else {
            for (String linha : historico) {
                System.out.println(linha);
            }
        }
        Utilitario.pausa();
    }

    public void definirJogador(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        bancoDadosJogador.definirJogadorAtual(nome);
        banca.setSaldo(bancoDadosJogador.obterSaldoJogador());
    }

    public void salvarSaldo() {
        bancoDadosJogador.atualizarSaldoJogador(Math.round(banca.getSaldo()));
    }

    public void registrarSessaoInicial() {
        bancoDadosJogador.salvarHistoricoSessao(Math.round(banca.getSaldo()));
    }

    public String processarResultado(int resultado, float aposta) {
        String mensagem = "";

        // Vitória imediata
        if (resultado == 7 || resultado == 11) {
            mensagem = "VOCÊ GANHOU!!! Meus parabéns";
            aposta = aposta * 2;
            banca.adicionarGanhos(aposta);
            System.out.println("\n" + mensagem);
        }
        // Derrota imediata
        else if (resultado == 2 || resultado == 3 || resultado == 12) {
            mensagem = "VOCÊ PERDEU!!! Mais sorte na próxima vez...";
            System.out.println("\n" + mensagem);
            Utilitario.pausa();
            System.out.println("Se tiver coragem, continue apostando!");
        }
        // Retorno parcial da aposta e fim da pontuação para resultados intermediários
        else if (resultado == 4 || resultado == 5 || resultado == 6 ||
                resultado == 8 || resultado == 9 || resultado == 10) {
            mensagem = "Mais sorte na próxima vez... ";
            System.out.println("\n" + mensagem);
            banca.adicionarGanhos(aposta / 2);
        }

        return mensagem;
    }
}
