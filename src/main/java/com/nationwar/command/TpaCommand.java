package com.nationwar.command;

import com.nationwar.NationWar;
import com.nationwar.tpa.TpaMain;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {

    private final TpaMain tpaMain;
    private final TeamMain teamMain;

    public TpaCommand(NationWar plugin) {
        this.tpaMain = plugin.getTpaMain();
        this.teamMain = plugin.getTeamMain();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("플레이어만 사용할 수 있습니다.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§c사용법: /tpa <플레이어|accept|deny>");
            return true;
        }

        // 수락
        if (args[0].equalsIgnoreCase("accept")) {
            if (!tpaMain.hasRequest(player)) {
                player.sendMessage("§c수락할 TPA 요청이 없습니다.");
                return true;
            }
            tpaMain.accept(player);
            return true;
        }

        // 거절
        if (args[0].equalsIgnoreCase("deny")) {
            if (!tpaMain.hasRequest(player)) {
                player.sendMessage("§c거절할 TPA 요청이 없습니다.");
                return true;
            }
            tpaMain.deny(player);
            return true;
        }

        // 대상 지정
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage("§c플레이어를 찾을 수 없습니다.");
            return true;
        }

        if (player.equals(target)) {
            player.sendMessage("§c자기 자신에게는 사용할 수 없습니다.");
            return true;
        }

        // 팀 제한
        if (!teamMain.sameTeam(player, target)) {
            player.sendMessage("§c같은 팀원에게만 TPA를 사용할 수 있습니다.");
            return true;
        }

        // 쿨타임
        if (tpaMain.hasCooldown(player)) {
            player.sendMessage("§cTPA 쿨타임 남음: "
                    + tpaMain.getRemainCooldown(player) + "초");
            return true;
        }

        tpaMain.sendRequest(player, target);
        return true;
    }
}
