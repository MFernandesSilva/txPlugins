package tx.rpg.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tx.rpg.runas.Runa;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;
import tx.rpg.utils.CalcularStatus;

import java.io.File;
import java.sql.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class RunasDatabaseManager {
    private static final String CRIAR_TABELA = "CREATE TABLE IF NOT EXISTS player_status (" +
            "uuid VARCHAR(36) PRIMARY KEY," +
            "nick VARCHAR(16) NOT NULL," +
            "runa_dano_nivel INTEGER DEFAULT 0," +
            "runa_dano_subnivel INTEGER DEFAULT 0," +
            "runa_defesa_nivel INTEGER DEFAULT 0," +
            "runa_defesa_subnivel INTEGER DEFAULT 0," +
            "runa_amplificacao_nivel INTEGER DEFAULT 0," +
            "runa_amplificacao_subnivel INTEGER DEFAULT 0" +
            ")";

    private static final String SALVAR_DADOS = "INSERT OR REPLACE INTO player_status (uuid, nick, runa_dano_nivel, runa_dano_subnivel, runa_defesa_nivel, runa_defesa_subnivel, runa_amplificacao_nivel, runa_amplificacao_subnivel) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String CARREGAR_DADOS = "SELECT nick, runa_dano_nivel, runa_dano_subnivel, runa_defesa_nivel, runa_defesa_subnivel, runa_amplificacao_nivel, runa_amplificacao_subnivel FROM player_status WHERE uuid = ?";

    private Connection connection;

    public void conectar() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            File arquivoDB = new File(txRPG.getInstance().getDataFolder(), "runas.db");
            String url = "jdbc:sqlite:" + arquivoDB.getAbsolutePath();

            connection = DriverManager.getConnection(url);
            Bukkit.getLogger().info("Conexão com o banco de dados SQLite estabelecida com sucesso!");

            criarTabela();
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().severe("Driver SQLite não encontrado: " + e.getMessage());
            throw new SQLException("Erro ao carregar o driver SQLite.", e);
        }
    }

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

    private void criarTabela() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CRIAR_TABELA);
        }
    }

    public void salvarDadosJogador(RunasPlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(txRPG.getInstance(), () -> {
            try (PreparedStatement pstmt = connection.prepareStatement(SALVAR_DADOS)) {
                pstmt.setString(1, playerData.getUuid().toString());
                pstmt.setString(2, playerData.getNick());

                int index = 3;
                for (Runa runa : playerData.getRunas().values()) {
                    pstmt.setInt(index++, runa.getNivel());
                    pstmt.setInt(index++, runa.getSubnivel());
                }

                pstmt.executeUpdate();
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Erro ao salvar dados do jogador: " + e.getMessage());
            }
        });
    }

    public void carregarDadosJogadorAsync(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(txRPG.getInstance(), () -> {
            RunasPlayerData playerData = carregarDadosJogador(player.getUniqueId());

            if (playerData == null) {
                Map<TipoRuna, Runa> runas = new EnumMap<>(TipoRuna.class);
                for (TipoRuna tipoRuna : TipoRuna.values()) {
                    runas.put(tipoRuna, new Runa(tipoRuna, 0, 0));
                }

                playerData = new RunasPlayerData(
                        player.getUniqueId(),
                        player.getName(),
                        runas
                );
            }

            carregarOuCriarRunas(player, playerData);

            final RunasPlayerData finalPlayerData = playerData;
            Bukkit.getScheduler().runTask(txRPG.getInstance(), () -> {
                txRPG.getInstance().getRunasPlayerData().put(player.getUniqueId(), finalPlayerData);

                if (player.getName().equalsIgnoreCase("Teux")) {
                    aplicarAtributosERunas(finalPlayerData);
                }
            });
        });
    }

    private void carregarOuCriarRunas(Player player, RunasPlayerData playerData) {
        Map<TipoRuna, Runa> runas = new EnumMap<>(TipoRuna.class);
        try (PreparedStatement pstmt = connection.prepareStatement(CARREGAR_DADOS)) {
            pstmt.setString(1, player.getUniqueId().toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    for (TipoRuna tipo : TipoRuna.values()) {
                        int nivel = rs.getInt("runa_" + tipo.toString().toLowerCase() + "_nivel");
                        int subnivel = rs.getInt("runa_" + tipo.toString().toLowerCase() + "_subnivel");
                        runas.put(tipo, new Runa(tipo, nivel, subnivel));
                    }
                }
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Erro ao carregar runas do jogador: " + e.getMessage());
            }

            for (TipoRuna tipo : TipoRuna.values()) {
                runas.put(tipo, runas.getOrDefault(tipo, new Runa(tipo, 0, 0)));
            }
            playerData.setRunas(runas);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void aplicarAtributosERunas(RunasPlayerData playerData) {
        Map<TipoRuna, Runa> runas = new EnumMap<>(TipoRuna.class);
        for (TipoRuna tipo : TipoRuna.values()) {
            runas.put(tipo, new Runa(tipo, 5, 3));
        }
        playerData.setRunas(runas);
    }

    private RunasPlayerData carregarDadosJogador(UUID uuid) {
        try (PreparedStatement pstmt = connection.prepareStatement(CARREGAR_DADOS)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nick = rs.getString("nick");

                    Map<TipoRuna, Runa> runas = new EnumMap<>(TipoRuna.class);
                    for (TipoRuna tipo : TipoRuna.values()) {
                        int nivel = rs.getInt("runa_" + tipo.toString().toLowerCase() + "_nivel");
                        int subnivel = rs.getInt("runa_" + tipo.toString().toLowerCase() + "_subnivel");
                        runas.put(tipo, new Runa(tipo, nivel, subnivel));
                    }

                    return new RunasPlayerData(uuid, nick, runas);
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao carregar dados do jogador: " + e.getMessage());
        }
        return null;
    }

    public void salvarDadosJogadorAsync(RunasPlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(txRPG.getInstance(), () -> salvarDadosJogador(playerData));
    }
}
