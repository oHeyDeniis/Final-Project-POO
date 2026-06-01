import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorBD {
    private static final String URL = "jdbc:sqlite:PergutasResposta/perguntas.db";
    private static final String JOGADOR_PADRAO = "jogador";
    private static final int SALDO_INICIAL = 50;
    private Connection conexao;
    // Fallback em memória quando o driver JDBC não estiver disponível
    private java.util.List<Pergunta> perguntasCache = new java.util.ArrayList<>();
    private Integer saldoCache = null;
    private String nomeJogadorAtual = null;
    private java.util.Map<String, Integer> saldosMemoria = new java.util.HashMap<>();

    public GerenciadorBD() {
        inicializarBD();
    }

    public void inicializarBD() {
        try {
            Class.forName("org.sqlite.JDBC");
            conexao = DriverManager.getConnection(URL);
            System.out.println("✓ Conectado ao SQLite com sucesso!");
            criarTabela();
        } catch (ClassNotFoundException e) {
            System.out.println(" Driver SQLite não encontrado! Usando fallback em memória para testes.");
            // popular dados em memória para permitir testes mesmo sem JDBC
            inserirPerguntasIniciais();
        } catch (SQLException e) {
            System.out.println(" Erro ao conectar ao banco de dados!");
            e.printStackTrace();
        }
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS perguntas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "enunciado TEXT NOT NULL," +
                "opcaoA TEXT NOT NULL," +
                "opcaoB TEXT NOT NULL," +
                "opcaoC TEXT NOT NULL," +
                "opcaoD TEXT NOT NULL," +
                "respostaCorreta TEXT NOT NULL," +
                "decada INTEGER NOT NULL" +
                ")";

        String sqlSaldo = "CREATE TABLE IF NOT EXISTS saldo_jogador (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome_jogador TEXT UNIQUE NOT NULL," +
                "pontos REAL DEFAULT " + SALDO_INICIAL +
                ")";

        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sql);
            stmt.execute(sqlSaldo);
            verificarEAtualizarTabelaSaldo();
            System.out.println("✓ Tabelas criadas/verificadas com sucesso!");

            // Verifica se há dados e insere se não houver
            if (!temDados()) {
                inserirPerguntasIniciais();
            } else {
                System.out.println("✓ Tabela já contém dados!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela!");
            e.printStackTrace();
        }
    }

    private boolean temDados() {
        String sql = "SELECT COUNT(*) FROM perguntas";
        try (Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public void resetarPerguntas() {
        if (conexao == null) {
            perguntasCache.clear();
            inserirPerguntasIniciais();
            System.out.println("✓ Dados de perguntas recarregados em memória.");
            return;
        }

        String sql = "DELETE FROM perguntas";
        try (Statement stmt = conexao.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("✓ Dados antigos excluídos da tabela perguntas.");
            inserirPerguntasIniciais();
        } catch (SQLException e) {
            System.out.println("Erro ao resetar perguntas!");
            e.printStackTrace();
        }
    }

    private boolean carregarPerguntasDoArquivoSQL() {
        if (conexao == null)
            return false;

        File arquivo = new File("PergutasResposta", "setup_perguntas.sql");
        if (!arquivo.exists()) {
            return false;
        }

        List<String[]> perguntas = new ArrayList<>();
        StringBuilder comando = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("--")) {
                    continue;
                }
                comando.append(linha).append(' ');
                if (linha.endsWith(";")) {
                    String stmt = comando.toString().trim();
                    comando.setLength(0);
                    if (stmt.toUpperCase().startsWith("INSERT INTO PERGUNTAS")) {
                        int idxValues = stmt.toUpperCase().indexOf("VALUES");
                        if (idxValues >= 0) {
                            String valores = stmt.substring(idxValues + 6).trim();
                            int inicio = valores.indexOf('(');
                            int fim = valores.lastIndexOf(')');
                            if (inicio >= 0 && fim > inicio) {
                                String trecho = valores.substring(inicio + 1, fim);
                                String[] campos = parseSqlValues(trecho);
                                if (campos.length == 7) {
                                    perguntas.add(campos);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler setup_perguntas.sql: " + e.getMessage());
            return false;
        }

        if (perguntas.isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO perguntas (enunciado, opcaoA, opcaoB, opcaoC, opcaoD, respostaCorreta, decada) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            for (String[] p : perguntas) {
                pstmt.setString(1, p[0]);
                pstmt.setString(2, p[1]);
                pstmt.setString(3, p[2]);
                pstmt.setString(4, p[3]);
                pstmt.setString(5, p[4]);
                pstmt.setString(6, p[5]);
                pstmt.setInt(7, Integer.parseInt(p[6]));
                pstmt.executeUpdate();
            }
            System.out.println("✓ Perguntas iniciais carregadas a partir de setup_perguntas.sql");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir perguntas a partir de setup_perguntas.sql");
            e.printStackTrace();
            return false;
        }
    }

    private String[] parseSqlValues(String texto) {
        List<String> valores = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean dentroString = false;

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '\'') {
                if (dentroString && i + 1 < texto.length() && texto.charAt(i + 1) == '\'') {
                    atual.append("'");
                    i++;
                } else {
                    dentroString = !dentroString;
                }
                continue;
            }
            if (c == ',' && !dentroString) {
                valores.add(atual.toString().trim());
                atual.setLength(0);
                continue;
            }
            atual.append(c);
        }
        if (atual.length() > 0) {
            valores.add(atual.toString().trim());
        }

        for (int i = 0; i < valores.size(); i++) {
            String valor = valores.get(i);
            if (valor.startsWith("'") && valor.endsWith("'")) {
                valor = valor.substring(1, valor.length() - 1).replace("''", "'");
            }
            valores.set(i, valor);
        }

        return valores.toArray(new String[0]);
    }

    private void verificarEAtualizarTabelaSaldo() {
        if (conexao == null)
            return;

        try (Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery("PRAGMA table_info(saldo_jogador)")) {
            boolean possuiNome = false;
            while (rs.next()) {
                if ("nome_jogador".equalsIgnoreCase(rs.getString("name"))) {
                    possuiNome = true;
                    break;
                }
            }
            if (!possuiNome) {
                stmt.execute("ALTER TABLE saldo_jogador ADD COLUMN nome_jogador TEXT");
                stmt.execute(
                        "UPDATE saldo_jogador SET nome_jogador='" + JOGADOR_PADRAO + "' WHERE nome_jogador IS NULL");
                stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_saldo_jogador_nome ON saldo_jogador(nome_jogador)");
                System.out.println("✓ Atualizada tabela de saldo para suportar vários jogadores.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar a tabela de saldo!");
            e.printStackTrace();
        }
    }

    private void inserirPerguntasIniciais() {
        String[][] perguntas = {
                // Década de 70
                { "Em que ano Pelé conquistou sua 3ª Copa do Mundo com o Brasil?",
                        "1970", "1962", "1958", "1978", "A", "1970" },

                { "Qual foi o programa de TV mais assistido no Brasil nos anos 70?",
                        "Telenovelas", "Globo de Ouro", "Plantão das Notícias", "Estúdio Aberto", "A",
                        "1970" },

                { "Em qual ano o Brasil sediou a Copa do Mundo?",
                        "1950", "1970", "1978", "1994", "A", "1950" },

                { "Qual time ganhou o Campeonato Carioca em 1978?",
                        "Flamengo", "Vasco da Gama", "Fluminense", "Botafogo", "A", "1978" },

                { "Qual filme brasileiro foi lançado em 1970 com grande sucesso?",
                        "Macunaíma", "São Bernardo", "Dona Flor e Seus Dois Maridos", "Pixote", "C", "1970" },

                // Década de 80
                { "Em que ano Sócrates liderou a Democracia Corinthiana levando-o ao bicampeonato?",
                        "1983", "1980", "1985", "1987", "A", "1980" },

                { "Qual filme brasileiro foi sucesso em 1980?",
                        "Pixote", "Dona Flor e Seus Dois Maridos", "O Cortiço", "Grande Sertão Veredas", "A", "1980" },

                { "Em que ano o Brasil ganhou a Libertadores nos anos 80?",
                        "1982", "1981", "1985", "1987", "B", "1981" },

                { "Qual programa infantil estreou na tv em 1986 e marcou a decada de 80?",
                        "Xou da Xuxa", "Balão Mágico", "TV Colosso", "Sítio do Picapau Amarelo", "A", "1980" },

                // Década de 90
                { "Qual time paulista ganhou a Copa Libertadores em 1992?",
                        "Corinthians", "Palmeiras", "São Paulo", "Santos", "C", "1990" },

                { "Em que ano o Brasil conquistou a 4ª Copa do Mundo(O Tetra)?",
                        "1994", "1990", "1998", "2002", "A", "1990" },

                { "Qual foi o programa de auditório do Brasil nos anos 90 que disputava a liderança de audiência?",
                        "Domingo do Faustão", "Programa do Silvio Santos", "Domingo Legal", "Globo de Ouro", "C",
                        "1990" },

                { "Em qual ano Ayrton Senna faleceu em um acidente?",
                        "1994", "1991", "1992", "1995", "A", "1990" },

                { "Qual filme brasileiro foi um fenômeno em 1995?",
                        "Cidade de Deus", "Carlota Joaquina", "Isadora Duncan", "O Cortiço", "B", "1990" },

                { " Qual time carioca ganhou a Copa Libertadores em 1998?",
                        "Botafogo", "Fluminense", "Flamengo", "Vasco da Gama", "D", "1990" }
        };

        String sql = "INSERT INTO perguntas (enunciado, opcaoA, opcaoB, opcaoC, opcaoD, respostaCorreta, decada) VALUES (?, ?, ?, ?, ?, ?, ?)";

        if (conexao == null) {
            // fallback: popular em memória
            for (String[] p : perguntas) {
                Pergunta pg = new Pergunta(
                        0,
                        p[0],
                        p[1],
                        p[2],
                        p[3],
                        p[4],
                        p[5].charAt(0),
                        Integer.parseInt(p[6]));
                perguntasCache.add(pg);
            }
            System.out.println("✓ Perguntas iniciais carregadas em memória para testes.");
            return;
        }

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            for (String[] p : perguntas) {
                pstmt.setString(1, p[0]);
                pstmt.setString(2, p[1]);
                pstmt.setString(3, p[2]);
                pstmt.setString(4, p[3]);
                pstmt.setString(5, p[4]);
                pstmt.setString(6, p[5]);
                pstmt.setString(7, p[6]);
                pstmt.executeUpdate();
            }
            System.out.println("✓ Perguntas iniciais inseridas com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir perguntas!");
            e.printStackTrace();
        }
    }

    public List<Pergunta> obterTodasPerguntas() {
        if (conexao == null) {
            return new ArrayList<>(perguntasCache);
        }

        List<Pergunta> perguntas = new ArrayList<>();
        String sql = "SELECT * FROM perguntas";

        try (Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pergunta p = new Pergunta(
                        rs.getInt("id"),
                        rs.getString("enunciado"),
                        rs.getString("opcaoA"),
                        rs.getString("opcaoB"),
                        rs.getString("opcaoC"),
                        rs.getString("opcaoD"),
                        rs.getString("respostaCorreta").charAt(0),
                        rs.getInt("decada"));
                perguntas.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar perguntas!");
            e.printStackTrace();
        }
        return perguntas;
    }

    public List<Pergunta> obterPerguntasPorDecada(int decada) {
        if (conexao == null) {
            java.util.List<Pergunta> resultado = new java.util.ArrayList<>();
            for (Pergunta p : perguntasCache) {
                if (p.getDecada() == decada)
                    resultado.add(p);
            }
            return resultado;
        }

        List<Pergunta> perguntas = new ArrayList<>();
        String sql = "SELECT * FROM perguntas WHERE decada = ?";

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, decada);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pergunta p = new Pergunta(
                        rs.getInt("id"),
                        rs.getString("enunciado"),
                        rs.getString("opcaoA"),
                        rs.getString("opcaoB"),
                        rs.getString("opcaoC"),
                        rs.getString("opcaoD"),
                        rs.getString("respostaCorreta").charAt(0),
                        rs.getInt("decada"));
                perguntas.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar perguntas por década!");
            e.printStackTrace();
        }
        return perguntas;
    }

    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar conexão!");
            e.printStackTrace();
        }
    }

    public void definirJogadorAtual(String nomeJogador) {
        this.nomeJogadorAtual = nomeJogador;
        verificarOuCriarJogador(nomeJogador);
    }

    public String obterNomeJogadorAtual() {
        return nomeJogadorAtual;
    }

    private void garantirJogadorPadrao() {
        if (nomeJogadorAtual == null || nomeJogadorAtual.isBlank()) {
            definirJogadorAtual(JOGADOR_PADRAO);
        }
    }

    private void verificarOuCriarJogador(String nomeJogador) {
        if (conexao == null) {
            saldosMemoria.putIfAbsent(nomeJogador, SALDO_INICIAL);
            return;
        }

        String sqlVerificar = "SELECT COUNT(*) FROM saldo_jogador WHERE nome_jogador = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sqlVerificar)) {
            pstmt.setString(1, nomeJogador);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String sqlInserir = "INSERT INTO saldo_jogador (nome_jogador, pontos) VALUES (?, ?)";
                try (PreparedStatement pstmtIns = conexao.prepareStatement(sqlInserir)) {
                    pstmtIns.setString(1, nomeJogador);
                    pstmtIns.setInt(2, SALDO_INICIAL);
                    pstmtIns.executeUpdate();
                    System.out.println("✓ Novo jogador criado: " + nomeJogador + " com saldo inicial de "
                            + SALDO_INICIAL + " pontos.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar/criar jogador!");
            e.printStackTrace();
        }
    }

    public int obterSaldoJogador() {
        garantirJogadorPadrao();

        if (conexao == null) {
            return saldosMemoria.getOrDefault(nomeJogadorAtual, SALDO_INICIAL);
        }

        String sql = "SELECT pontos FROM saldo_jogador WHERE nome_jogador = ?";

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, nomeJogadorAtual);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("pontos");
            }
            return SALDO_INICIAL;
        } catch (SQLException e) {
            System.out.println("Erro ao obter saldo!");
            e.printStackTrace();
            return 0;
        }
    }

    public void atualizarSaldoJogador(int novoSaldo) {
        garantirJogadorPadrao();

        if (conexao == null) {
            saldosMemoria.put(nomeJogadorAtual, novoSaldo);
            System.out.println("✓ Saldo atualizado (" + nomeJogadorAtual + "): " + novoSaldo + " pontos");
            return;
        }

        String sql = "UPDATE saldo_jogador SET pontos = ? WHERE nome_jogador = ?";

        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setDouble(1, novoSaldo);
            pstmt.setString(2, nomeJogadorAtual);
            int linhasAtualizadas = pstmt.executeUpdate();

            if (linhasAtualizadas > 0) {
                System.out.println("✓ Saldo atualizado (" + nomeJogadorAtual + "): " + novoSaldo + " pontos");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar saldo!");
            e.printStackTrace();
        }
    }

    public void recarregarSaldoJogador(int valor) {
        if (valor <= 0) {
            return;
        }
        atualizarSaldoJogador(obterSaldoJogador() + valor);
    }

}
