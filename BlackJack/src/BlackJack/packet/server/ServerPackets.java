package BlackJack.packet.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BlackJack.ServerStarter;
import BlackJack.game.Game;
import BlackJack.game.player.Player;
import BlackJack.game.player.PlayerConnection;
import BlackJack.packet.Packet;
import BlackJack.packet.client.JoinGamePacket;
import BlackJack.packet.client.PlayerActionPacket;

public class ServerPackets {

    Connection databaseConnection;
    public static ServerPackets instance;

    public ServerPackets() {
        databaseConnection = ServerStarter.instance.databaseConnection;
        ServerPackets.instance = this;
    }

    public void handleIncomingPacket(Packet packet, PlayerConnection client) {
        Game game = Game.getInstance();

        if (packet instanceof PlayerDisconnectPacket) {
            if (client.getPlayer() != null) {
                game.disconnectPlayer(client.getPlayer());
            }
        }
        if (packet instanceof PingPongPacket) {
            synchronized (client) {
                client.pendingPing = 0;
            }
        }
        if (packet instanceof BetValuePacket) {
            Player player = client.getPlayer();
            float betValue = ((BetValuePacket) packet).betValue;
            if (betValue > player.getBalance()) {
                betValue = player.getBalance();
            }
            if (player != null) {
                player.setCurrentBet(betValue);
            }
            UpdatePlayerNameText updatePlayerNameText = new UpdatePlayerNameText(
                    player.getPosition(),
                    player.getName(),
                    player.getCurrentBet() + " (" + player.getBalance() + ")",
                    betValue > 0 ? "Apostou $" + betValue : "Fora da rodada");
            DatagramServer.instance.broadcastPacket(updatePlayerNameText);
        }
        if (packet instanceof PlayerActionPacket) {
            game.gameInstance.processAction(client.getPlayer(), ((PlayerActionPacket) packet).action);
        }
        if (packet instanceof JoinGamePacket) {

            JoinGamePacket joinGamePacket = (JoinGamePacket) packet;
            System.out.println("New player: " + joinGamePacket.name);
            if (game.hasPlayer(joinGamePacket.name)) {
                Packet response = new ResponseJoinGamePacket(false, "Nome ja em uso!");
                client.sendPacket(response);
                return;
            }
            if (!game.gameInstance.canJoin()) {
                Packet response = new ResponseJoinGamePacket(false, "Jogo cheio!");
                client.sendPacket(response);
                return;
            }

            Player player = new Player(joinGamePacket.name);
            client.setPlayer(player);
            initPlayerDatabase(player);

            game.addPlayer(player);
            Packet response = new ResponseJoinGamePacket(true, "Sucesso!");
            client.sendPacket(response);

            Packet AddPlayerPacket = new AddPlayerPacket(player.getName(), player.getPosition(), true);
            client.sendPacket(AddPlayerPacket);

            try {
                BetValuePacket bpk = new BetValuePacket(player.getBalance());
                client.sendPacket(bpk);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // .setCurrentBet(0);

            UpdatePlayerNameText upk = new UpdatePlayerNameText(
                    player.getPosition(), player.getName(),
                    " $" + player.getBalance() + "", "Escolhendo Valor");
            client.sendPacket(upk);

            for (Player otherClient : game.getPlayers()) {
                if (otherClient != player) {
                    AddPlayerPacket pk = new AddPlayerPacket(player.getName(), player.getPosition());
                    otherClient.getPlayerConnection().sendPacket(pk);

                    AddPlayerPacket pk2 = new AddPlayerPacket(otherClient.getName(), otherClient.getPosition());
                    player.getPlayerConnection().sendPacket(pk2);
                }
            }
        }
    }

    public void initPlayerDatabase(Player player) {
        String sqlVerificar = "SELECT name FROM players WHERE name = ?";
        try {
            PreparedStatement stmtCheck = ServerStarter.instance.databaseConnection.prepareStatement(sqlVerificar);
            stmtCheck.setString(1, player.getName());
            ResultSet rs = stmtCheck.executeQuery();
            if (rs.next()) {
                System.out.println("O jogador " + player.getName() + " já está cadastrado no banco.");
                player.balance = rs.getFloat("money");
                return;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao verificar jogador: " + e.getMessage());
            return;
        }
        String sqlInsert = "INSERT INTO players (name, money, wins, losses, winMoney, loseMoney) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = ServerStarter.instance.databaseConnection.prepareStatement(sqlInsert);
            statement.setString(1, player.getName());
            statement.setFloat(2, player.getBalance());
            statement.setInt(3, 0);
            statement.setInt(4, 0);
            statement.setInt(5, 0);
            statement.setInt(6, 0);

            statement.execute();
            System.out.println("Jogador " + player.getName() + " inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir jogador: " + e.getMessage());
        }
    }

    public void updateDatabasePlayer(Player player, float money, boolean isWin) {
        String sqlUpdate = "UPDATE players SET money = ?, " +
                (isWin ? "wins = wins + 1, winMoney = winMoney + ?"
                        : "losses = losses + 1, loseMoney = loseMoney + ?")
                +
                " WHERE name = ?";

        try {
            PreparedStatement stmt = ServerStarter.instance.databaseConnection.prepareStatement(sqlUpdate);

            stmt.setFloat(1, player.getBalance());
            stmt.setFloat(2, money);
            stmt.setString(3, player.getName());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Banco de dados atualizado para o jogador: " + player.getName());
            } else {
                System.out.println("Jogador não encontrado para atualizar: " + player.getName());
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar dados do jogador: " + e.getMessage());
        }
    }
}