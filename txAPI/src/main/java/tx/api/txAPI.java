package tx.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class txAPI extends JavaPlugin {


    @Override
    public void onEnable() {
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

}
