import java.util.Random;

public class Dado {
    private static final Random gerador = new Random();
    private int valor;

    public Dado() {
        this.valor = 0;
    }

    public void lancar() {
        this.valor = gerador.nextInt(6) + 1;
    }

    public int getValor() {
        return valor;
    }
}
