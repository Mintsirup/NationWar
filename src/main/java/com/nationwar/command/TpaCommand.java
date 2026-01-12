package com.nationwar.command;

import com.nationwar.NationWar;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {
    private final NationWar plugin;
    public TpaCommand(NationWar plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (args.length < 1) return false;

        if (args[0].equals("수락") || args[0].equals("거절")) {
            // 수락/거절 로직 처리
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) return true;

        // 기준서: 같은 팀원에게만 보낼 수 있음
        if (!plugin.getTeamMain().sameTeam(p, target)) {
            p.sendMessage("§c팀원에게만 tpa를 보낼 수 있습니다.");
            return true;
        }

        // 기준서: 20분의 쿨타임
        long cooldown = plugin.getTpaMain().getRemainCoolDown(p.getUniqueId());
        if (cooldown > 0) {
            p.sendMessage("§c쿨타임이 " + (cooldown / 1000 / 60) + "분 남았습니다.");
            return true;
        }

        plugin.getTpaMain().sendRequest(p, target);

        // 기준서: [수락] [거절] 문구가 포함된 메시지 전송
        TextComponent msg = new TextComponent("§e" + p.getName() + "님의 TPA 요청: ");
        TextComponent accept = new TextComponent("§a[수락] ");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa 수락"));
        TextComponent deny = new TextComponent("§c[거절]");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa 거절"));

        msg.addExtra(accept);
        msg.addExtra(deny);
        target.spigot().sendMessage(msg);
        p.sendMessage("§a요청을 보냈습니다.");

        return true;
    }
}