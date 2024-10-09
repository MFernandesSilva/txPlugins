package tx.rpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tx.api.DM;
import tx.api.Mensagem;
import tx.rpg.data.ReinosPlayerData;
import tx.rpg.data.RunasPlayerData;
import tx.rpg.itens.Reinos;
import tx.rpg.reinos.Reino;
import tx.rpg.reinos.ReinoAPI;
import tx.rpg.reinos.TipoReino;
import tx.rpg.runas.Runa;
import tx.rpg.runas.RunaAPI;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;

public class ReinosCommand implements CommandExecutor {
    DM dm = new DM();

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {

        if (!(s instanceof Player)) {
            s.sendMessage(dm.cc());
            return true;
        }

        Player player = (Player) s;

        // Verifica se o jogador tem permissão para usar o comando
        if (!player.hasPermission("txrpg.reinos.op")) {
            player.sendMessage(dm.np());
            return true;
        }

        if (args.length == 2) {
            // Comando: /reinos give <nivel>
            if (args[0].equalsIgnoreCase("give")) {
                if (!s.hasPermission("txrpg.admin")){
                    s.sendMessage(dm.np());
                    return true;
                }
                return handleGiveCommand(player, s, args);
            }
            // Comando: /reinos romper <player>
            if (args[0].equalsIgnoreCase("romper")) {
                if (!s.hasPermission("txrpg.admin")){
                    s.sendMessage(dm.np());
                    return true;
                }
                return handleRomperCommand(s, args);
            }
            // Comando: /reinos upgrade <player>
            if (args[0].equalsIgnoreCase("upgrade")) {
                if (!s.hasPermission("txrpg.admin")){
                    s.sendMessage(dm.np());
                    return true;
                }
                return handleUpgradeCommand(s, args);
            }
        } else if (args.length == 3){
            // /reinos set <nivel> <player>
            if (args[0].equalsIgnoreCase("set")){
                if (!s.hasPermission("txrpg.admin")){
                    s.sendMessage(dm.np());
                    return true;
                }
                try {
                    int nivel = Integer.parseInt(args[1]);
                    Player target = Bukkit.getPlayer(args[2]);

                    setarNivelReino(s, target, nivel);
                } catch (Exception e){
                    s.sendMessage(Mensagem.formatar("&cUso: /reinos set <nivel>"));
                }
            }
        }
        return false;
    }

    // Método para lidar com o comando give
    //Comando: /reinos give <nivel>
    private boolean handleGiveCommand(Player player, CommandSender s, String[] args) { // parei aqui, tem que fazer verificacao de nivel para por nome
        if (!(s instanceof Player)){
            s.sendMessage(dm.cc());
            return true;
        }

        int nivel = obterNivel(s, args[1]);
        if (nivel == -1) return true;

        ItemStack reino = obterRunaPorTipoENivel(nivel);
        if (reino != null) {
            player.getInventory().addItem(reino);
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&aVocê recebeu um reino nível " + nivel + "."));
        } else {
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cErro ao criar a runa."));
        }
        return true;
    }

    // Método para lidar com o comando romper
    private boolean handleRomperCommand(CommandSender s, String[] args) {

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado."));
            return true;
        }

        ReinosPlayerData playerData = txRPG.getInstance().getReinosPlayerData().get(target.getUniqueId());
        if (playerData == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado ou sem atributos."));
            return true;
        }

        Reino reino = playerData.getReinos().get(TipoReino.REINO);
        if (reino == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não possui este tipo de runa."));
            return true;
        }

        if (!ReinoAPI.podeEvoluirReino(target, TipoReino.REINO)) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cA runa não pode ser rompida ainda."));
            return true;
        }

        int novoNivel = reino.getNivel() + 1;
        reino.setNivel(novoNivel);

        txRPG.getInstance().reinosDB().salvarDadosJogadorAsync(playerData);
        s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Reino rompido com sucesso para &c" + target.getName() + "&7."));
        target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Sua runa foi rompida para o nível &c" + novoNivel + "&7!"));

        return true;
    }

    // Método para lidar com o comando upgrade
    // Comando: /reinos upgrade <player>
    private boolean handleUpgradeCommand(CommandSender s, String[] args) {

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado."));
            return true;
        }

        ReinosPlayerData playerData = txRPG.getInstance().getReinosPlayerData().get(target.getUniqueId());

        if (playerData == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado ou sem atributos."));
            return true;
        }

        Reino reino = playerData.getReinos().get(TipoReino.REINO);
        if (reino == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não possui este tipo de runa."));
            return true;
        }

        // Lógica para atualizar a runa
        if (reino.getNivel() == 0){
            reino.setNivel(1);
        } else if (reino.getNivel() < ReinoAPI.getNivelMaximo(reino.getNivel())) {
            reino.setNivel(reino.getNivel() + 1);
        } else {
            target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cO reino já está no nivel máximo ou precisa romper."));
            return true;
        }

        txRPG.getInstance().reinosDB().salvarDadosJogadorAsync(playerData);
        s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Reino atualizado com sucesso para &c" + target.getName() + "&7."));
        target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Seu reino foi atualizada."));

        return true;
    }

    // Método auxiliar para obter o nível
    private int obterNivel(CommandSender s, String nivelArg) {
        try {
            int nivel = Integer.parseInt(nivelArg);
            if (nivel < 1 || nivel > 53) {
                s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível inválido."));
                return -1;
            }
            return nivel;
        } catch (NumberFormatException e) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível deve ser um número."));
            return -1;
        }
    }

    // Método auxiliar para obter a runa por tipo e nível
    private ItemStack obterRunaPorTipoENivel(int nivel) {
        Reinos reinos = new Reinos();

        if (nivel >= 1 && nivel <= 20){
            return reinos.reinoMortal[nivel - 1];
        } else if (nivel >= 21 && nivel <= 35) {
            return reinos.reinoDeCombate[nivel - 1];
        } else if (nivel >= 36 && nivel <= 45) {
            return reinos.reinoCelestial[nivel - 1];
        } else if (nivel >= 46 && nivel <= 50){
            return reinos.reinoImortal[nivel - 1];
        } else if (nivel >= 51 && nivel <= 53) {
            return reinos.reinoDeus[nivel - 1];
        } else {
            return null;
        }
    }

    private void setarNivelReino(CommandSender sender, Player player, int nivel){

        if (player == null){
            sender.sendMessage(Mensagem.formatar("&cPrecisa informar o jogador"));
            return;
        }

        if (!player.isOnline()){
            sender.sendMessage(Mensagem.formatar("&cJogador não está online"));
            return;
        }
        ReinosPlayerData reinosPlayerData = txRPG.getInstance().getReinosPlayerData().get(player.getUniqueId());
        Reino reino = reinosPlayerData.getReinos().get(TipoReino.REINO);

        reino.setNivel(nivel);

        String reinoString = "";

        if (nivel >= 1 && nivel <= 20){
            reinoString = Mensagem.formatar("&7MORTAL");
        } else if (nivel >= 21 && nivel <= 35) {
            reinoString = Mensagem.formatar("&aDE COMBATE");
        } else if (nivel >= 36 && nivel <= 45) {
            reinoString = Mensagem.formatar("&5CELESTIAL");
        } else if (nivel >= 46 && nivel <= 50){
            reinoString = Mensagem.formatar("&cIMORTAL");
        } else if (nivel >= 51 && nivel <= 53) {
            reinoString = Mensagem.formatar("&6DEUS");
        }


        if (sender != player) {
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "seu reino foi setado para " + reinoString));
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "reino de &e" + player.getName() + " foi setado para " + reinoString));
        } else {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "seu reino foi setado para " + reinoString));
        }
    }
}
