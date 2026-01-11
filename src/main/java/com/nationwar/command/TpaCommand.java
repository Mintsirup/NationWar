package com.nationwar.command;

import com.nationwar.tpa.TpaMain;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§e[!] /tpa [플레이어] - 요청 전송");
            player.sendMessage("§e[!] /tpa 수락/거절 - 요청 응답");
            return true;
        }

        if (args[0].equals("수락")) {
            TpaMain.acceptRequest(player);
        } else if (args[0].equals("거절")) {
            TpaMain.denyRequest(player);
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§c[!] 해당 플레이어를 찾을 수 없습니다.");
                return true;
            }
            TpaMain.sendRequest(player, target);
        }
        return true;
    }
}