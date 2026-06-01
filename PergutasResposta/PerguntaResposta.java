import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe Principal - Jogo de Perguntas e Respostas
 * Tema: História dos anos 70, 80 e 90
 * Perguntas armazenadas em banco de dados SQLite
 * 
 * @author Seu Nome
 * @version 1.0
 */
public class PerguntaResposta {
    private static final String URL = "jdbc:sqlite:PergutasResposta/perguntas.db";

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> new InterfaceGrafica().criarInterface());
        // inicializarBD();

    }

    public static void inicializarBD() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conexao = DriverManager.getConnection(URL);
            System.out.println("✓ Conectado ao SQLite com sucesso!");
            String consulta = "delete from perguntas";
            conexao.createStatement().executeUpdate(consulta);

            consulta = "select * from perguntas";
            var stmt = conexao.createStatement();
            var rs = stmt.executeQuery(consulta);
            System.out.println("Perguntas carregadas do banco de dados");
            while (rs.next()) {
                System.out.println("Pergunta: " + rs.getString("enunciado"));
                rs.getString("enunciado");
            }
        } catch (ClassNotFoundException e) {
            System.out.println(" Driver SQLite não encontrado! Usando fallback em memória para testes.");
            // popular dados em memória para permitir testes mesmo sem JDBC

        } catch (SQLException e) {
            System.out.println(" Erro ao conectar ao banco de dados!");
            e.printStackTrace();
        }
    }

}
