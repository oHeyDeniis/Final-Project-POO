import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ranking — Responsável por salvar, buscar, listar e remover pontuações.
 * Armazena dados em memória (List) e persiste no banco SQLite via JDBC.
 *
 * Funcionalidades cobertas (Entrega 2):
 *  - Item 6: cadastro (salvar) e listagem (exibir) em memória
 *  - Item 7: buscar por jogo, remover por posição
 *  - Item 8: persistência com banco de dados SQLite
 */
public class Ranking {

    // ─── Constantes ───────────────────────────────────────
    private static final int LIMITE = 10;
    private static final String DB_PATH = System.getProperty("user.dir") + "/flipperold.db";

    // ─── Atributos ────────────────────────────────────────
    private List<Pontuacao> pontuacoes; // armazenamento em memória
    private Connection conexao;          // conexão com o SQLite

    /**
     * Construtor — inicializa a lista em memória e o banco de dados.
     */
    public Ranking() {
        this.pontuacoes = new ArrayList<>();
        inicializarBanco();
        carregarDoBanco();
    }

    // ─── Banco de dados ───────────────────────────────────

    /**
     * Cria a tabela no SQLite se ainda não existir.
     */
    private void inicializarBanco() {
        String sql = "CREATE TABLE IF NOT EXISTS pontuacoes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome_jogador TEXT NOT NULL," +
                "nome_jogo TEXT NOT NULL," +
                "pontuacao INTEGER NOT NULL" +
                ");";
        try {
            Class.forName("org.sqlite.JDBC");
            conexao = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            Statement stmt = conexao.createStatement();
            stmt.execute(sql);
            stmt.close();
        } catch (Exception e) {
            System.out.println("[AVISO] Banco de dados indisponível. Usando apenas memória.");
            conexao = null;
        }
    }

    /**
     * Carrega todas as pontuações do banco para a lista em memória ao iniciar.
     */
    private void carregarDoBanco() {
        if (conexao == null) return;
        String sql = "SELECT nome_jogador, nome_jogo, pontuacao FROM pontuacoes ORDER BY pontuacao DESC;";
        try {
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                pontuacoes.add(new Pontuacao(
                        rs.getString("nome_jogador"),
                        rs.getString("nome_jogo"),
                        rs.getInt("pontuacao")
                ));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("[AVISO] Erro ao carregar pontuações do banco.");
        }
    }

    /**
     * Persiste uma pontuação no banco de dados.
     * @param p Objeto Pontuacao a ser salvo.
     */
    private void salvarNoBanco(Pontuacao p) {
        if (conexao == null) return;
        String sql = "INSERT INTO pontuacoes (nome_jogador, nome_jogo, pontuacao) VALUES (?, ?, ?);";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, p.getNomeJogador());
            pstmt.setString(2, p.getNomeJogo());
            pstmt.setInt(3, p.getValor());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("[AVISO] Erro ao salvar no banco. Dado mantido apenas em memória.");
        }
    }

    /**
     * Remove uma pontuação do banco pelo nome do jogador, jogo e valor.
     * @param p Pontuacao a ser removida.
     */
    private void removerDoBanco(Pontuacao p) {
        if (conexao == null) return;
        String sql = "DELETE FROM pontuacoes WHERE nome_jogador = ? AND nome_jogo = ? AND pontuacao = ? LIMIT 1;";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, p.getNomeJogador());
            pstmt.setString(2, p.getNomeJogo());
            pstmt.setInt(3, p.getValor());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("[AVISO] Erro ao remover do banco.");
        }
    }

    // ─── Operações principais ─────────────────────────────

    /**
     * CADASTRAR — Salva uma pontuação em memória e no banco.
     * @param p Objeto Pontuacao a ser registrado.
     */
    public void salvar(Pontuacao p) {
        pontuacoes.add(p);
        salvarNoBanco(p);
    }

    /**
     * LISTAR — Exibe o placar de um jogo específico no console.
     * Mostra os melhores resultados em ordem decrescente.
     * @param nomeJogo Nome do jogo a filtrar.
     */
    public void exibir(String nomeJogo) {
        List<Pontuacao> filtradas = buscarPorJogo(nomeJogo);

        Console.titulo("RANKING — " + nomeJogo.toUpperCase());

        if (filtradas.isEmpty()) {
            System.out.println("\n  Nenhuma pontuação registrada para este jogo ainda.\n");
            return;
        }

        System.out.printf("  %-4s %-20s %s%n", "Pos", "Jogador", "Pontuação");
        Console.linhaSub();
        int pos = 1;
        for (Pontuacao p : filtradas) {
            System.out.println(p.exibirPlacador(pos++));
        }
        Console.linhaSub();
    }

    /**
     * BUSCAR — Retorna lista filtrada por jogo, ordenada por pontuação.
     * Limitada ao número máximo definido em LIMITE.
     * @param nomeJogo Nome do jogo a filtrar.
     * @return Lista de Pontuacao ordenada decrescentemente.
     */
    public List<Pontuacao> buscarPorJogo(String nomeJogo) {
        return pontuacoes.stream()
                .filter(p -> p.getNomeJogo().equalsIgnoreCase(nomeJogo))
                .sorted(Comparator.comparingInt(Pontuacao::getValor).reversed())
                .limit(LIMITE)
                .collect(Collectors.toList());
    }

    /**
     * BUSCAR — Retorna lista de todos os jogos com pontuações registradas.
     * @return Lista de nomes de jogos únicos.
     */
    public List<String> buscarJogosComRegistro() {
        return pontuacoes.stream()
                .map(Pontuacao::getNomeJogo)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * REMOVER — Remove uma pontuação da memória e do banco pelo índice no ranking.
     * @param nomeJogo Nome do jogo.
     * @param posicao  Posição no ranking (1-based).
     * @return true se removido com sucesso, false se posição inválida.
     */
    public boolean remover(String nomeJogo, int posicao) {
        List<Pontuacao> filtradas = buscarPorJogo(nomeJogo);
        if (posicao < 1 || posicao > filtradas.size()) return false;

        Pontuacao alvo = filtradas.get(posicao - 1);
        pontuacoes.remove(alvo);
        removerDoBanco(alvo);
        return true;
    }

    /**
     * ATUALIZAR — Substitui o nome de um jogador em um registro existente.
     * Remove o registro antigo e insere um novo com o nome atualizado.
     * @param nomeJogo   Nome do jogo.
     * @param posicao    Posição no ranking (1-based).
     * @param novoNome   Novo nome do jogador.
     * @return true se atualizado com sucesso, false se posição inválida.
     */
    public boolean atualizarNome(String nomeJogo, int posicao, String novoNome) {
        List<Pontuacao> filtradas = buscarPorJogo(nomeJogo);
        if (posicao < 1 || posicao > filtradas.size()) return false;

        Pontuacao antiga = filtradas.get(posicao - 1);
        Pontuacao nova = new Pontuacao(novoNome, antiga.getNomeJogo(), antiga.getValor());

        removerDoBanco(antiga);
        pontuacoes.remove(antiga);

        pontuacoes.add(nova);
        salvarNoBanco(nova);
        return true;
    }

    /**
     * Retorna os N melhores registros de um jogo.
     * @param nomeJogo   Nome do jogo.
     * @param quantidade Número de registros a retornar.
     * @return Lista com os melhores registros.
     */
    public List<Pontuacao> top(String nomeJogo, int quantidade) {
        return pontuacoes.stream()
                .filter(p -> p.getNomeJogo().equalsIgnoreCase(nomeJogo))
                .sorted(Comparator.comparingInt(Pontuacao::getValor).reversed())
                .limit(quantidade)
                .collect(Collectors.toList());
    }

    /**
     * Fecha a conexão com o banco ao encerrar o programa.
     */
    public void fechar() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            // Ignora erro ao fechar
        }
    }
}
