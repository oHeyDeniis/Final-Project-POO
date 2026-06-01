import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pergunta {
    private int id;
    private String enunciado;
    private String opcaoA;
    private String opcaoB;
    private String opcaoC;
    private String opcaoD;
    private char respostaCorreta;
    private int decada;

    public Pergunta(int id, String enunciado, String opcaoA, String opcaoB,
            String opcaoC, String opcaoD, char respostaCorreta, int decada) {
        this.id = id;
        this.enunciado = enunciado;
        this.opcaoA = opcaoA;
        this.opcaoB = opcaoB;
        this.opcaoC = opcaoC;
        this.opcaoD = opcaoD;
        this.respostaCorreta = Character.toUpperCase(respostaCorreta);
        this.decada = decada;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getOpcaoA() {
        return opcaoA;
    }

    public String getOpcaoB() {
        return opcaoB;
    }

    public String getOpcaoC() {
        return opcaoC;
    }

    public String getOpcaoD() {
        return opcaoD;
    }

    public char getRespostaCorreta() {
        return respostaCorreta;
    }

    public int getDecada() {
        return decada;
    }

    public void exibir() {
        exibir(id);
    }

    public void exibir(int numero) {
        System.out.println("\n=== PERGUNTA " + numero + " ===");
        System.out.println("Década: " + decada);
        System.out.println(enunciado);
        System.out.println("\nA) " + opcaoA);
        System.out.println("B) " + opcaoB);
        System.out.println("C) " + opcaoC);
        System.out.println("D) " + opcaoD);
    }

    public void embaralharOpcoes() {
        List<Opcao> lista = new ArrayList<>();
        lista.add(new Opcao('A', opcaoA));
        lista.add(new Opcao('B', opcaoB));
        lista.add(new Opcao('C', opcaoC));
        lista.add(new Opcao('D', opcaoD));

        String textoRespostaCorreta = getTextoDaResposta(respostaCorreta);
        Collections.shuffle(lista);

        opcaoA = lista.get(0).texto;
        opcaoB = lista.get(1).texto;
        opcaoC = lista.get(2).texto;
        opcaoD = lista.get(3).texto;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).texto.equals(textoRespostaCorreta)) {
                respostaCorreta = (char) ('A' + i);
                break;
            }
        }
    }

    private String getTextoDaResposta(char resposta) {
        return switch (Character.toUpperCase(resposta)) {
            case 'A' -> opcaoA;
            case 'B' -> opcaoB;
            case 'C' -> opcaoC;
            case 'D' -> opcaoD;
            default -> "";
        };
    }

    public boolean verificarResposta(char resposta) {
        return Character.toUpperCase(resposta) == this.respostaCorreta;
    }

    private static class Opcao {
        private final char letra;
        private final String texto;

        public Opcao(char letra, String texto) {
            this.letra = letra;
            this.texto = texto;
        }
    }
}
