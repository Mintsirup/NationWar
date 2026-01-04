package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length == 0) {
            player.sendMessage("§c사용법: /tpa [닉네임] 또는 /tpa 수락/거절");
            return true;
        }

        if (args[0].equals("수락")) {
            NationWar.getInstance().getTpaMain().acceptRequest(player);
        } else if (args[0].equals("거절")) {
            NationWar.getInstance().getTpaMain().denyRequest(player);
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§c해당 플레이어를 찾을 수 없습니다.");
                return true;
            }
            NationWar.getInstance().getTpaMain().sendRequest(player, target);
        }
        return true;
    }
}