package com.nationwar.tpa;

import com.nationwar.NationWar;
import com.nationwar.team.TeamMain;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.UUID;

public class TpaMain {
    public static final HashMap<UUID, UUID> tpaRequests = new HashMap<>();

    public static void sendRequest(Player sender, Player target) {
        String senderTeam = TeamMain.getPlayerTeam(sender);
        String targetTeam = TeamMain.getPlayerTeam(target);

        if (senderTeam.equals("방랑자") || !senderTeam.equals(targetTeam)) {
            sender.sendMessage("§c[!] TPA는 같은 팀원에게만 가능합니다.");
            return;
        }

        tpaRequests.put(target.getUniqueId(), sender.getUniqueId());

        TextComponent msg = new TextComponent("§e[!] " + sender.getName() + "님의 TPA 요청: ");
        TextComponent accept = new TextComponent("§a§l[수락] ");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa 수락"));
        TextComponent deny = new TextComponent("§c§l[거절]");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa 거절"));

        msg.addExtra(accept);
        msg.addExtra(deny);
        target.spigot().sendMessage(msg);
        sender.sendMessage("§a[!] 요청을 보냈습니다.");

        new BukkitRunnable() {
            @Override
            public void run() {
                tpaRequests.remove(target.getUniqueId());
            }
        }.runTaskLater(NationWar.getInstance(), 1200L);
    }

    public static void acceptRequest(Player target) {
        UUID senderUUID = tpaRequests.get(target.getUniqueId());
        if (senderUUID == null) {
            target.sendMessage("§c[!] 요청이 없습니다.");
            return;
        }
        Player sender = Bukkit.getPlayer(senderUUID);
        if (sender != null) sender.teleport(target.getLocation());
        tpaRequests.remove(target.getUniqueId());
    }

    public static void denyRequest(Player target) {
        tpaRequests.remove(target.getUniqueId());
        target.sendMessage("§c[!] 거절했습니다.");
    }
}