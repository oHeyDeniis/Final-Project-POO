package Sembanco;
public class PerguntaSimples {
    private int id;
    private String enunciado;
    private String opcaoA;
    private String opcaoB;
    private String opcaoC;
    private String opcaoD;
    private char respostaCorreta;
    private int decada;

    public PerguntaSimples(int id, String enunciado, String opcaoA, String opcaoB,
            String opcaoC, String opcaoD, char respostaCorreta, int decada) {
        this.id = id;
        this.enunciado = enunciado;
        this.opcaoA = opcaoA;
        this.opcaoB = opcaoB;
        this.opcaoC = opcaoC;
        this.opcaoD = opcaoD;
        this.respostaCorreta = respostaCorreta;
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
        System.out.println("\n=== PERGUNTA " + id + " ===");
        System.out.println("Década: " + decada);
        System.out.println(enunciado);
        System.out.println("\nA) " + opcaoA);
        System.out.println("B) " + opcaoB);
        System.out.println("C) " + opcaoC);
        System.out.println("D) " + opcaoD);
    }

    public boolean verificarResposta(char resposta) {
        return Character.toUpperCase(resposta) == this.respostaCorreta;
    }
}
