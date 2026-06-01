public class Banca {
    private float saldo;

    public Banca() {
        this.saldo = 0;
    }

    public void adicionarCredito(float valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
    }

    public boolean fazerAposta(float aposta) {
        if (aposta > 0 && aposta <= saldo) {
            this.saldo -= aposta;
            return true;
        } else {
            return false;
        }
    }

    public void adicionarGanhos(float ganho) {
        this.saldo += ganho;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        if (saldo >= 0) {
            this.saldo = saldo;
        }
    }

    public boolean temSaldo() {
        return saldo >= 0.5;
    }

    public void exibirSaldo() {
        System.out.println("Saldo atual da Banca: R$ " + String.format("%.2f", saldo));
    }
}
