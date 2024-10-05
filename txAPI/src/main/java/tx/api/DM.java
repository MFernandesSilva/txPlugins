package tx.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DM {

    private String np = "&cVocê não tem permissão para usar este comando.";
    private String cc = "&cComando apenas para jogadores.";


    public String np() { return Mensagem.formatar(this.np); }

    public String cc() { return Mensagem.formatar(this.cc); }
    public static void onEnable(Plugin plugin){
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&a========== " + plugin.getName() + " =========="));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&aAutor: txDEV"));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&aHabilitado com sucesso!"));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&a========== " + plugin.getName() + " =========="));
    }

    public static void onDisable(Plugin plugin){
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&c========== " + plugin.getName() + " =========="));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&cAutor: txDEV"));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&cDesabilitado com sucesso!"));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&c========== " + plugin.getName() + " =========="));
    }
}
