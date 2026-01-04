package com.nationwar.listeners;

import com.nationwar.core.CoreMain;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.*;

public class PlayerDistanceDetect implements Listener {
    private final CoreMain coreMain;
    private final TeamMain teamMain;
    private final Set<UUID> warnedPlayers = new HashSet<>();

    public PlayerDistanceDetect(CoreMain coreMain, TeamMain teamMain) {
        this.coreMain = coreMain;
        this.teamMain = teamMain;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String playerTeam = teamMain.playerTeams.getOrDefault(player.getUniqueId(), "방랑자");

        for (int i = 0; i < coreMain.coreLocations.size(); i++) {
            Location coreLoc = coreMain.coreLocations.get(i);
            if (!coreLoc.getWorld().equals(player.getWorld())) continue;

            double distance = player.getLocation().distance(coreLoc);
            if (distance <= 250) {
                coreMain.bossBars.get(i).addPlayer(player);
                String ownerTeam = coreMain.coreOwners.get(i);

                if (!ownerTeam.equals("없음") && !ownerTeam.equals(playerTeam)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 0, false, false));
                    String warnKey = player.getUniqueId() + "-" + i;
                    if (!warnedPlayers.contains(player.getUniqueId())) {
                        player.sendTitle("§c적 팀의 코어 주변에 접근했습니다!", "§f발광이 적용됩니다", 10, 40, 10);
                        warnedPlayers.add(player.getUniqueId());

                        for (UUID memberId : teamMain.teamMembers.getOrDefault(ownerTeam, new ArrayList<>())) {
                            Player member = Bukkit.getPlayer(memberId);
                            if (member != null) member.sendMessage("당신의 코어에 " + playerTeam + "팀의 " + player.getName() + "이(가) 접근했습니다!");
                        }
                    }
                }
            } else {
                coreMain.bossBars.get(i).removePlayer(player);
                warnedPlayers.remove(player.getUniqueId());
            }
        }
    }
}