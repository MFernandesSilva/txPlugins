package tx.rpg.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tx.rpg.reinos.Reino;
import tx.rpg.reinos.TipoReino;
import tx.rpg.txRPG;

import java.io.File;
import java.sql.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class ReinosDatabaseManager {

    private static final String CRIAR_TABELA = "CREATE TABLE IF NOT EXISTS player_status (" +
            "uuid VARCHAR(36) PRIMARY KEY," +
            "nick VARCHAR(16) NOT NULL," +
            "reino_nivel INTEGER DEFAULT 0" +
            ")";

    private static final String SALVAR_DADOS = "INSERT OR REPLACE INTO player_status (uuid, nick, reino_nivel) VALUES (?, ?, ?)";
    private static final String CARREGAR_DADOS = "SELECT nick, reino_nivel FROM player_status WHERE uuid = ?";

    private Connection connection;

    // Método para conectar ao banco de dados
    public void conectar() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            File arquivoDB = new File(txRPG.getInstance().getDataFolder(), "reinos.db");
            String url = "jdbc:sqlite:" + arquivoDB.getAbsolutePath();

            connection = DriverManager.getConnection(url);
            Bukkit.getLogger().info("Conexão com o banco de dados SQLite estabelecida com sucesso!");

            criarTabela();
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().severe("Driver SQLite não encontrado: " + e.getMessage());
            throw new SQLException("Erro ao carregar o driver SQLite.", e);
        }
    }

    // Método para desconectar do banco de dados
    public void desconectar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Bukkit.getLogger().info("Conexão com o banco de dados SQLite fechada.");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao fechar a conexão com o banco de dados SQLite: " + e.getMessage());
        }
    }

    // Método para criar a tabela no banco de dados
    private void criarTabela() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CRIAR_TABELA);
        }
    }

    // Método para salvar dados do jogador
    public void salvarDadosJogador(ReinosPlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(txRPG.getInstance(), () -> {
            try (PreparedStatement pstmt = connection.prepareStatement(SALVAR_DADOS)) {
                pstmt.setString(1, playerData.getUuid().toString());
                pstmt.setString(2, playerData.getNick());
                int index = 3;
                for (Reino reino : playerData.getReinos().values()) {
                    pstmt.setInt(index++, reino.getNivel());
                }

                pstmt.executeUpdate();
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Erro ao salvar dados do jogador: " + e.getMessage());
            }
        });
    }

    // Método para carregar dados do jogador de forma assíncrona
    public void carregarDadosJogadorAsync(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(txRPG.getInstance(), () -> {
            ReinosPlayerData playerData = carregarDadosJogador(player.getUniqueId());

            if (playerData == null) {
                Map<TipoReino, Reino> reinos = criarReinosPadrao();
                playerData = new ReinosPlayerData(player.getUniqueId(), player.getName(), reinos);
            }

            carregarOuCriarReinos(player, playerData);

            final ReinosPlayerData finalPlayerData = playerData;
            Bukkit.getScheduler().runTask(txRPG.getInstance(), () -> {
                txRPG.getInstance().getReinosPlayerData().put(player.getUniqueId(), finalPlayerData);
            });
        });
    }

    // Método auxiliar para criar reinos padrão
    private Map<TipoReino, Reino> criarReinosPadrao() {
        Map<TipoReino, Reino> reinos = new EnumMap<>(TipoReino.class);
        for (TipoReino tipoReino : TipoReino.values()) {
            reinos.put(tipoReino, new Reino(tipoReino, 0));
        }
        return reinos;
    }

    // Método para carregar ou criar reinos
    private void carregarOuCriarReinos(Player player, ReinosPlayerData playerData) {
        Map<TipoReino, Reino> reinos = new EnumMap<>(TipoReino.class);
        try (PreparedStatement pstmt = connection.prepareStatement(CARREGAR_DADOS)) {
            pstmt.setString(1, player.getUniqueId().toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    for (TipoReino tipo : TipoReino.values()) {
                        int nivel = rs.getInt("reino_nivel");
                        reinos.put(tipo, new Reino(tipo, nivel));
                    }
                }
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Erro ao carregar reinos do jogador: " + e.getMessage());
            }

            for (TipoReino tipo : TipoReino.values()) {
                reinos.put(tipo, reinos.getOrDefault(tipo, new Reino(tipo, 0)));
            }
            playerData.setReinos(reinos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para carregar dados do jogador
    private ReinosPlayerData carregarDadosJogador(UUID uuid) {
        try (PreparedStatement pstmt = connection.prepareStatement(CARREGAR_DADOS)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nick = rs.getString("nick");
                    Map<TipoReino, Reino> reinos = new EnumMap<>(TipoReino.class);
                    for (TipoReino tipo : TipoReino.values()) {
                        int nivel = rs.getInt("reino_nivel");
                        reinos.put(tipo, new Reino(tipo, nivel));
                    }
                    return new ReinosPlayerData(uuid, nick, reinos);
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao carregar dados do jogador: " + e.getMessage());
        }
        return null;
    }

    // Método para salvar dados do jogador de forma assíncrona
    public void salvarDadosJogadorAsync(ReinosPlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(txRPG.getInstance(), () -> salvarDadosJogador(playerData));
    }
}
