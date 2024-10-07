package tx.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class txAPI extends JavaPlugin {


    @Override
    public void onEnable() {

        loadConfiguration();

        String ip = getServer().getIp();
        getLogger().info("Licença carregada: " + ip);
        getLogger().info("Validando licença...");

        if (!LicencaValidator.validarLicenca(ip)) {
            getLogger().warning("Licença inválida! O plugin será desativado.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else {
            getLogger().info("Licença válida! O plugin está funcionando.");
        }

        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&e========== txAPI =========="));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&e   Habilitado com sucesso  "));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&e========== txAPI =========="));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&c========== txAPI =========="));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&c  Desabilitado com sucesso "));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&c========== txAPI =========="));
    }

    private void loadConfiguration(){
        saveDefaultConfig();
        getConfig().options().copyDefaults(false);
    }

}
