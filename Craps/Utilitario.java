public class Utilitario {

    public static void pausa() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Erro na pausa: " + e.getMessage());
        }
    }

    public static void pausa(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            System.out.println("Erro na pausa: " + e.getMessage());
        }
    }
}
