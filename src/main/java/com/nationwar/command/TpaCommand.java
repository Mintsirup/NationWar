package com.nationwar.command;

import com.nationwar.team.TeamMain;
import com.nationwar.tpa.TpaMain;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class TpaCommand implements CommandExecutor, TabCompleter {

    private final TpaMain tpaMain;
    private final TeamMain teamMain;

    public TpaCommand(TpaMain tpaMain, TeamMain teamMain) {
        this.tpaMain = tpaMain;
        this.teamMain = teamMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("플레이어만 사용할 수 있는 명령어입니다.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§c/tpa <플레이어>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§c해당 플레이어를 찾을 수 없습니다.");
            return true;
        }

        if (!teamMain.sameTeam(player, target)) {
            player.sendMessage("§c같은 팀원에게만 TPA를 보낼 수 있습니다.");
            return true;
        }

        if (tpaMain.hasCooldown(player)) {
            player.sendMessage("§cTPA 쿨타임이 남아 있습니다. (" +
                    tpaMain.getRemainCooldown(player) + "초)");
            return true;
        }

        tpaMain.sendRequest(player, target);

        // 메시지
        TextComponent msg = new TextComponent(
                "§e" + player.getName() + "님이 TPA 요청을 보냈습니다. (1분 후 만료)\n"
        );

        TextComponent accept = new TextComponent("§a[수락]");
        accept.setClickEvent(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/tpaccept " + player.getName()
        ));

        TextComponent deny = new TextComponent(" §c[거절]");
        deny.setClickEvent(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/tpdeny " + player.getName()
        ));

        msg.addExtra(accept);
        msg.addExtra(deny);

        target.spigot().sendMessage(msg);
        player.sendMessage("§aTPA 요청을 보냈습니다.");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender instanceof Player player) {
            return Bukkit.getOnlinePlayers().stream()
                    .filter(p -> teamMain.sameTeam(player, p))
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
