package com.nationwar.command;

import com.nationwar.NationWar;
import com.nationwar.tpa.TpaMain;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class TpaCommand implements CommandExecutor, TabCompleter {
    private final TpaMain tpaMain;

    public TpaCommand(TpaMain tpaMain) {
        this.tpaMain = tpaMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && !target.equals(player)) {
                tpaMain.sendRequest(player, target);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) players.add(p.getName());
            return players;
        }
        return null;
    }
}