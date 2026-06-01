import java.util.Scanner;

public class SistemaAposta {
    private int saldoJogador;
    private int apostaInicial;
    private int apostaAtual;
    private int acertos;
    private int garantiaCinco;
    private int garantiaSete;
    private boolean jogadorPerdeu;
    private boolean apostaInterrompida;
    private GerenciadorBD bd;

    public SistemaAposta(GerenciadorBD bd) {
        this.bd = bd;
        this.apostaInicial = 0;
        this.apostaAtual = 0;
        this.acertos = 0;
        this.garantiaCinco = 0;
        this.garantiaSete = 0;
        this.jogadorPerdeu = false;
        this.apostaInterrompida = false;
        carregarSaldoJogador();
    }

    private void carregarSaldoJogador() {
        this.saldoJogador = bd.obterSaldoJogador();
    }

    public void definirJogadorAtual(String nomeJogador) {
        bd.definirJogadorAtual(nomeJogador);
        carregarSaldoJogador();
    }

    public boolean iniciarAposta() {
        return iniciarApostaValor(100);
    }

    public boolean iniciarAposta(Scanner scanner) {
        carregarSaldoJogador();
        exibirMenuAposta();

        System.out.print("\nDigite o valor que deseja apostar: ");
        apostaInicial = obterValorAposta(scanner);

        if (apostaInicial <= 0 || apostaInicial > saldoJogador) {
            System.out.println("\n✗ Valor inválido ou saldo insuficiente.");
            return iniciarAposta(scanner);
        }

        saldoJogador -= apostaInicial;
        apostaAtual = apostaInicial;
        acertos = 0;
        garantiaCinco = 0;
        garantiaSete = 0;
        jogadorPerdeu = false;
        apostaInterrompida = false;

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   APOSTA INICIADA COM SUCESSO!         ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Aposta: " + apostaAtual + " pontos");
        System.out.println("║ Saldo restante: " + saldoJogador + " pontos");
        System.out.println("║ Recupere a aposta acertando 5 perguntas");
        System.out.println("║ Se errar antes de 5 acertos, perde tudo");
        System.out.println("╚════════════════════════════════════════╝");

        return true;
    }

    private int obterValorAposta(Scanner scanner) {
        try {
            int valor = Integer.parseInt(scanner.nextLine().trim());
            if (valor <= 0 || valor > saldoJogador) {
                System.out.print("Valor inválido. Digite um valor entre 1 e " + saldoJogador + ": ");
                return obterValorAposta(scanner);
            }
            return valor;
        } catch (NumberFormatException e) {
            System.out.print("Entrada inválida. Digite um número inteiro válido: ");
            return obterValorAposta(scanner);
        }
    }

    private void exibirMenuAposta() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        SISTEMA DE APOSTAS              ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Saldo atual: " + saldoJogador + " pontos");
        System.out.println("║ Aposta mínima: 1 ponto");
        System.out.println("║");
        System.out.println("║ REGRAS:");
        System.out.println("║ • Se errar antes de 5 acertos, perde tudo");
        System.out.println("║ • Ao atingir 5 acertos, aposta x1,5 e pode parar");
        System.out.println("║ • Ao atingir 7 acertos, aposta x2,25 e pode parar");
        System.out.println("║ • Erro após checkpoint garante o último valor seguro");
        System.out.println("╚════════════════════════════════════════╝");
    }

    public void processarResposta(boolean acertou) {
        if (acertou) {
            acertos++;
            System.out.println("\n✓ ACERTO! Acertos: " + acertos + "/10");

            if (acertos == 5) {
                garantiaCinco = (int) Math.round(apostaInicial * 1.5);
                apostaAtual = garantiaCinco;
                System.out.println("★ Checkpoint alcançado! Valor garantido: " + garantiaCinco + " pontos");
            }

            if (acertos == 7) {
                garantiaSete = (int) Math.round(apostaInicial * 2.25);
                apostaAtual = garantiaSete;
                System.out.println("★ Novo checkpoint! Valor garantido: " + garantiaSete + " pontos");
            }

            if (acertos == 10) {
                apostaAtual = (int) Math.round(apostaInicial * 3.0);
                System.out.println("\n🎉 VITÓRIA TOTAL! Você conquistou o valor máximo: " + apostaAtual + " pontos");
            }
        } else {
            if (acertos < 5) {
                jogadorPerdeu = true;
                apostaAtual = 0;
                System.out.println("\n✗ ERROU! Perdeu toda a aposta. Acertos: " + acertos + ".");
            } else if (acertos < 7) {
                jogadorPerdeu = false;
                apostaAtual = garantiaCinco;
                System.out.println("\n✗ ERROU! Você garante o valor de 5 acertos: " + garantiaCinco + " pontos.");
            } else {
                jogadorPerdeu = false;
                apostaAtual = garantiaSete > 0 ? garantiaSete : garantiaCinco;
                System.out.println("\n✗ ERROU! Você garante o valor de 7 acertos: " + apostaAtual + " pontos.");
            }
        }
    }

    public boolean podeParar() {
        return acertos >= 5 && !jogadorPerdeu && !apostaInterrompida;
    }

    /**
     * Inicia a aposta informando o valor diretamente (usado pela GUI)
     */
    public boolean iniciarApostaValor(int valor) {
        carregarSaldoJogador();
        if (valor <= 0 || valor > saldoJogador) {
            return false;
        }
        this.apostaInicial = valor;
        saldoJogador -= apostaInicial;
        this.apostaAtual = apostaInicial;
        this.acertos = 0;
        this.garantiaCinco = 0;
        this.garantiaSete = 0;
        this.jogadorPerdeu = false;
        this.apostaInterrompida = false;
        return true;
    }

    public boolean isCheckpoint() {
        return acertos == 5 || acertos == 7;
    }

    public int getValorGarantiaCheckpoint() {
        if (acertos == 7) {
            return garantiaSete;
        }
        if (acertos == 5) {
            return garantiaCinco;
        }
        return 0;
    }

    public void interromperAposta() {
        apostaInterrompida = true;
    }

    public boolean isApostaInterrompida() {
        return apostaInterrompida;
    }

    public int getAcertos() {
        return acertos;
    }

    public int getApostaAtual() {
        return apostaAtual;
    }

    public int getSaldoJogador() {
        return saldoJogador;
    }

    public void finalizarAposta() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        RESULTADO FINAL DA APOSTA       ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Acertos: " + acertos + "/10");
        System.out.println("║ Valor garantido: " + apostaAtual + " pontos");

        if (acertos >= 5 && !jogadorPerdeu) {
            System.out.println("║ Status: ✓ GANHOU!");
            System.out.println("║ Prêmio: +" + apostaAtual + " pontos");
            saldoJogador += apostaAtual;
        } else if (jogadorPerdeu) {
            System.out.println("║ Status: ✗ PERDEU!");
            System.out.println("║ Prêmio: 0 pontos");
        } else {
            System.out.println("║ Status: ✗ NÃO ATINGIU 5 ACERTOS!");
            System.out.println("║ Prêmio: 0 pontos");
        }

        System.out.println("║ Saldo total: " + saldoJogador + " pontos");
        System.out.println("╚════════════════════════════════════════╝");
    }

    public void salvarPontos() {
        bd.atualizarSaldoJogador(saldoJogador);
        System.out.println("✓ Pontos salvos no banco de dados!");
    }
}
