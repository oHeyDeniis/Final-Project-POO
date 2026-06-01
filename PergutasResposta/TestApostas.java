public class TestApostas {
    public static void main(String[] args) {
        System.out.println("Iniciando testes automatizados do Sistema de Apostas...\n");

        GerenciadorBD bd = new GerenciadorBD();
        bd.definirJogadorAtual("Teste");

        // Test 1: Perder antes de 5 acertos
        System.out.println("--- Teste 1: Perda antes de 5 acertos ---");
        bd.atualizarSaldoJogador(1000);
        SistemaAposta s1 = new SistemaAposta(bd);
        if (!s1.iniciarAposta()) {
            System.out.println("Falha ao iniciar aposta no Teste 1.");
        } else {
            s1.processarResposta(false); // erra na primeira
            s1.finalizarAposta();
            System.out.println("Saldo após perda antes de 5 acertos: " + s1.getSaldoJogador());
            System.out.println();
        }

        // Test 2: Atingir 5 acertos e parar
        System.out.println("--- Teste 2: Atingir 5 acertos e parar ---");
        bd.atualizarSaldoJogador(1000);
        SistemaAposta s2 = new SistemaAposta(bd);
        if (!s2.iniciarAposta()) {
            System.out.println("Falha ao iniciar aposta no Teste 2.");
        } else {
            for (int i = 0; i < 5; i++)
                s2.processarResposta(true);
            // jogador opta por parar
            s2.finalizarAposta();
            System.out.println("Saldo após parar no 5º acerto: " + s2.getSaldoJogador());
            System.out.println();
        }

        // Test 3: Atingir 7 acertos e errar, garantia do 7
        System.out.println("--- Teste 3: Atingir 7 acertos e errar ---");
        bd.atualizarSaldoJogador(1000);
        SistemaAposta s3 = new SistemaAposta(bd);
        if (!s3.iniciarAposta()) {
            System.out.println("Falha ao iniciar aposta no Teste 3.");
        } else {
            for (int i = 0; i < 7; i++)
                s3.processarResposta(true);
            // agora erra
            s3.processarResposta(false);
            s3.finalizarAposta();
            System.out
                    .println("Saldo após errar depois do 7º acerto (deve refletir garantia): " + s3.getSaldoJogador());
            System.out.println();
        }

        // Test 4: Acertar todas as 10
        System.out.println("--- Teste 4: Acertar todas as 10 perguntas ---");
        bd.atualizarSaldoJogador(1000);
        SistemaAposta s4 = new SistemaAposta(bd);
        if (!s4.iniciarAposta()) {
            System.out.println("Falha ao iniciar aposta no Teste 4.");
        } else {
            for (int i = 0; i < 10; i++)
                s4.processarResposta(true);
            s4.finalizarAposta();
            System.out.println("Saldo após 10 acertos: " + s4.getSaldoJogador());
            System.out.println();
        }

        System.out.println(
                "Todos os testes terminaram. Você pode verificar o banco de dados 'perguntas.db' e o arquivo 'regras_apostas.html'.");
        bd.fecharConexao();
    }
}
