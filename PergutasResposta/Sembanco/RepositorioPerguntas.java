package Sembanco;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPerguntas {
    private static List<PerguntaSimples> perguntas;

    public RepositorioPerguntas() {
        carregarPerguntas();
    }

    private void carregarPerguntas() {
        perguntas = new ArrayList<>();

        // Perguntas dos Anos 70
        perguntas.add(new PerguntaSimples(1,
                "Qual foi o evento que marcou o fim da Guerra Fria em 1989?",
                "Queda do Muro de Berlim", "Fim da Guerra do Vietnã", "Revolução Cubana", "Crise dos Mísseis",
                'A', 1970));

        perguntas.add(new PerguntaSimples(2,
                "Que banda britânica se separa em 1970?",
                "The Beatles", "The Rolling Stones", "Pink Floyd", "Queen",
                'A', 1970));

        perguntas.add(new PerguntaSimples(3,
                "Em qual país começou a Revolução Verde?",
                "Índia", "Brasil", "China", "México",
                'A', 1970));

        perguntas.add(new PerguntaSimples(4,
                "Qual foi a maior catástrofe aérea do século XX em 1977?",
                "Colisão em Tenerife", "Voo 447 Air France", "Acidente do Japão", "Desastre do Concorde",
                'A', 1970));

        perguntas.add(new PerguntaSimples(5,
                "Que presidente dos EUA renunciou em 1974?",
                "Richard Nixon", "Gerald Ford", "Jimmy Carter", "Ronald Reagan",
                'A', 1970));

        // Perguntas dos Anos 80
        perguntas.add(new PerguntaSimples(6,
                "Em que ano Gorbachev se torna líder da URSS?",
                "1985", "1980", "1988", "1986",
                'A', 1980));

        perguntas.add(new PerguntaSimples(7,
                "Qual filme venceu o Oscar de Melhor Filme em 1982?",
                "Gandhi", "Chariots of Fire", "E.T.", "Blade Runner",
                'B', 1980));

        perguntas.add(new PerguntaSimples(8,
                "Que país foi invadido pelos EUA em 1983?",
                "Granada", "Nicarágua", "Panamá", "El Salvador",
                'A', 1980));

        perguntas.add(new PerguntaSimples(9,
                "Qual era a consola de jogo mais popular em 1985?",
                "Nintendo NES", "Atari 2600", "Commodore 64", "Sega Genesis",
                'A', 1980));

        perguntas.add(new PerguntaSimples(10,
                "Que catástrofe nuclear ocorreu em 1986?",
                "Chernobyl", "Three Mile Island", "Fukushima", "Sellafield",
                'A', 1980));

        // Perguntas dos Anos 90
        perguntas.add(new PerguntaSimples(11,
                "Em que ano terminou a Guerra Fria oficialmente?",
                "1991", "1989", "1993", "1995",
                'A', 1990));

        perguntas.add(new PerguntaSimples(12,
                "Qual evento marcou o fim do apartheid na África do Sul?",
                "Libertação de Nelson Mandela", "Morte de Mandela", "Golpe de Estado", "Eleições presidenciais",
                'A', 1990));

        perguntas.add(new PerguntaSimples(13,
                "Qual foi o maior atentado terrorista dos anos 90 nos EUA?",
                "Oklahoma City em 1995", "Ataque ao World Trade Center 2001", "Atentado em Boston",
                "Ataque ao Pentagon",
                'A', 1990));

        perguntas.add(new PerguntaSimples(14,
                "Qual foi o maior acidente industrial do século XX em Bhopal?",
                "1984", "1990", "1994", "1988",
                'A', 1980));

        perguntas.add(new PerguntaSimples(15,
                "Qual filme foi um fenômeno em 1997?",
                "Titanic", "Jurassic Park", "The Matrix", "Avatar",
                'A', 1990));
    }

    public List<PerguntaSimples> obterTodasPerguntas() {
        return new ArrayList<>(perguntas);
    }

    public List<PerguntaSimples> obterPerguntasPorDecada(int decada) {
        List<PerguntaSimples> resultado = new ArrayList<>();
        for (PerguntaSimples p : perguntas) {
            if (p.getDecada() == decada) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public int getTotalPerguntas() {
        return perguntas.size();
    }
}
