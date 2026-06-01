package BlackJack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import BlackJack.game.Game;
import BlackJack.packet.server.DatagramServer;

public class ServerStarter {

    public static void main(String[] args) {
        new ServerStarter();

    }

    public DatagramServer server;
    public Game game;
    public Connection databaseConnection;
    public static ServerStarter instance;

    public ServerStarter() {
        ServerStarter.instance = this;
        if (!initDatabase()) {
            System.out.println("Erro ao iniciar o banco de dados, inicie novamente.");
            return;
        }
        this.server = new DatagramServer(5000);
        server.run();
        this.game = new Game();
    }

    public boolean initDatabase() {
        String url = "jdbc:sqlite:meu_banco.db";

        System.out.println("Tentando conectar ao SQLite...");

        try {
            this.databaseConnection = DriverManager.getConnection(url);
            if (this.databaseConnection != null) {
                System.out.println("Sucesso! O SQLite está funcionando no seu VS Code.");
                this.createTables();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
        return false;
    }

    public void createTables() {
        String sql = "CREATE TABLE players (name VARCHAR(50) PRIMARY KEY, money INT, wins integer, losses integer, winMoney integer, loseMoney integer)";
        try {
            this.databaseConnection.createStatement().execute(sql);
            System.out.println("Tabela criada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
        // select * from players e mostrar
        String sql2 = "SELECT * FROM players";
        try {
            ResultSet res = this.databaseConnection.createStatement().executeQuery(sql2);
            while (res.next()) {
                System.out.println("----------------------");
                System.out.println("Nome: " + res.getString("name"));
                System.out.println("Money: " + res.getFloat("money"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }
}
