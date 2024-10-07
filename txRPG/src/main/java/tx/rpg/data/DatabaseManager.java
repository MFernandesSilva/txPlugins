package tx.rpg.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tx.rpg.txRPG;

import java.io.File;
import java.sql.*;
import java.util.UUID;

// Classe responsável pela gestão do banco de dados
public class DatabaseManager {
    public static Plugin plugin = txRPG.getInstance();

    // Comandos SQL
    private static final String CRIAR_TABELA = "CREATE TABLE IF NOT EXISTS player_status (" +
            "uuid VARCHAR(36) PRIMARY KEY," +
            "nick VARCHAR(16) NOT NULL," +
            "dano DOUBLE," +
            "defesa DOUBLE," +
            "intel INTEGER," +  // Corrigido o tipo INTERGER para INTEGER
            "ampCombate INTEGER," +
            "alcance INTEGER," +
            "penDefesa DOUBLE," +
            "bloqueio INTEGER," +
            "rouboVida INTEGER," +
            "regenVida INTEGER," +
            "regenMana INTEGER," +
            "sorte INTEGER," +
            "danoFinal DOUBLE," +
            "defesaFinal DOUBLE" +
            ")";

    private static final String SALVAR_DADOS = "INSERT OR REPLACE INTO player_status (uuid, nick, dano, defesa, intel, ampCombate, alcance, penDefesa, bloqueio, rouboVida, regenVida, regenMana, sorte, danoFinal, defesaFinal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String CARREGAR_DADOS = "SELECT nick, dano, defesa, intel, ampCombate, alcance, penDefesa, bloqueio, rouboVida, regenVida, regenMana, sorte, danoFinal, defesaFinal FROM player_status WHERE uuid = ?";

    private Connection conexao;

    // Método para conectar ao banco de dados
    public void conectar() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            File arquivoDB = new File(txRPG.getInstance().getDataFolder(), "player_status.db");
            String url = "jdbc:sqlite:" + arquivoDB.getAbsolutePath();

            conexao = DriverManager.getConnection(url);
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
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                Bukkit.getLogger().info("Conexão com o banco de dados SQLite fechada.");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao fechar a conexão com o banco de dados SQLite: " + e.getMessage());
        }
    }

    // Método para criar a tabela no banco de dados
    private void criarTabela() throws SQLException {
        try (Statement stmt = conexao.createStatement()) {
            stmt.executeUpdate(CRIAR_TABELA);
        }
    }

    // Método para salvar os dados do jogador
    public void salvarDadosJogador(PlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(txRPG.getInstance(), () -> {
            try (PreparedStatement pstmt = conexao.prepareStatement(SALVAR_DADOS)) {
                pstmt.setString(1, playerData.getUuid().toString());
                pstmt.setString(2, playerData.getNick());
                pstmt.setDouble(3, playerData.getDano());
                pstmt.setDouble(4, playerData.getDefesa());
                pstmt.setInt(5, playerData.getIntel());
                pstmt.setInt(6, playerData.getAmpCombate());
                pstmt.setInt(7, playerData.getAlcance());
                pstmt.setDouble(8, playerData.getPenDefesa());
                pstmt.setInt(9, playerData.getBloqueio());
                pstmt.setInt(10, playerData.getRouboVida());
                pstmt.setInt(11, playerData.getRegenVida());
                pstmt.setInt(12, playerData.getRegenMana());
                pstmt.setInt(13, playerData.getSorte());
                pstmt.setDouble(14, playerData.getDanoFinal());
                pstmt.setDouble(15, playerData.getDefesaFinal());

                pstmt.executeUpdate();
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Erro ao salvar dados do jogador: " + e.getMessage());
            }
        });
    }

    // Método assíncrono para carregar os dados do jogador
    public void carregarDadosJogadorAsync(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(txRPG.getInstance(), () -> {
            PlayerData playerData = carregarDadosJogador(player.getUniqueId());

            if (playerData == null) {
                playerData = criarPlayerDataDefault(player);
            }

            carregarOuCriarRunas(player, playerData);

            final PlayerData finalPlayerData = playerData;
            Bukkit.getScheduler().runTask(txRPG.getInstance(), () -> {
                txRPG.getInstance().getPlayerData().put(player.getUniqueId(), finalPlayerData);
            });
        });
    }

    // Método para criar um objeto PlayerData com valores padrão
    private PlayerData criarPlayerDataDefault(Player player) {
        return new PlayerData(
                player.getUniqueId(),
                player.getName(),
                txRPG.getInstance().getConfiguracao().getDanoPadrao(),
                txRPG.getInstance().getConfiguracao().getDefesaPadrao(),
                txRPG.getInstance().getConfiguracao().getIntelPadrao(),
                txRPG.getInstance().getConfiguracao().getAmpCombatePadrao(),
                txRPG.getInstance().getConfiguracao().getAlcancePadrao(),
                txRPG.getInstance().getConfiguracao().getPenDefesaPadrao(),
                txRPG.getInstance().getConfiguracao().getBloqueioPadrao(),
                txRPG.getInstance().getConfiguracao().getRouboVidaPadrao(),
                txRPG.getInstance().getConfiguracao().getRegenVidaPadrao(),
                txRPG.getInstance().getConfiguracao().getRegenManaPadrao(),
                txRPG.getInstance().getConfiguracao().getSortePadrao(),
                0, 0
        );
    }

    // Método para carregar ou criar runas
    private void carregarOuCriarRunas(Player player, PlayerData playerData) {
        try (PreparedStatement pstmt = conexao.prepareStatement(CARREGAR_DADOS)) {
            pstmt.setString(1, player.getUniqueId().toString());
            // Aqui você pode adicionar lógica para criar runas se não existirem
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para carregar os dados do jogador
    private PlayerData carregarDadosJogador(UUID uuid) {
        try (PreparedStatement pstmt = conexao.prepareStatement(CARREGAR_DADOS)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extrairPlayerData(rs, uuid);
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao carregar dados do jogador: " + e.getMessage());
        }
        return null;
    }

    // Método para extrair PlayerData do ResultSet
    private PlayerData extrairPlayerData(ResultSet rs, UUID uuid) throws SQLException {
        String nick = rs.getString("nick");
        double dano = rs.getDouble("dano");
        double defesa = rs.getDouble("defesa");
        int intel = rs.getInt("intel");
        int ampCombate = rs.getInt("ampCombate");
        int alcance = rs.getInt("alcance");
        double penDefesa = rs.getDouble("penDefesa");
        int bloqueio = rs.getInt("bloqueio");
        int rouboVida = rs.getInt("rouboVida");
        int regenVida = rs.getInt("regenVida");
        int regenMana = rs.getInt("regenMana");
        int sorte = rs.getInt("sorte");
        double danoFinal = rs.getDouble("danoFinal");
        double defesaFinal = rs.getDouble("defesaFinal");

        return new PlayerData(uuid, nick, dano, defesa, intel, ampCombate, alcance, penDefesa, bloqueio, rouboVida, regenVida, regenMana, sorte, danoFinal, defesaFinal);
    }

    // Método assíncrono para salvar dados do jogador
    public void salvarDadosJogadorAsync(PlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(txRPG.getInstance(), () -> salvarDadosJogador(playerData));
    }
}
