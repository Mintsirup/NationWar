package com.nationwar.command;

import com.nationwar.tpa.TpaMain;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpaCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length == 1 && args[0].equalsIgnoreCase("수락")) {
            if (!TpaMain.hasRequest(player)) return true;
            TpaMain.accept(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("거절")) {
            if (!TpaMain.hasRequest(player)) return true;
            TpaMain.deny(player);
            return true;
        }

        if (args.length != 1) return true;

        if (TpaMain.hasCooldown(player)) {
            player.sendMessage("TPA는 20분에 한 번만 사용할 수 있습니다.");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) return true;

        TpaMain.sendRequest(player, target);

        target.sendMessage(player.getName() + "이(가) 당신에게 TPA 요청을 보냈습니다. 이 요청은 1분 후에 만료됩니다.");

        TextComponent accept = new TextComponent("[수락]");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa 수락"));

        TextComponent deny = new TextComponent("[거절]");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa 거절"));

        target.spigot().sendMessage(accept, new TextComponent(" "), deny);

        player.sendMessage("TPA 요청을 보냈습니다.");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
            list.add("수락");
            list.add("거절");
        }

        return list;
    }
}
