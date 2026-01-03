package com.nationwar.listeners;

import com.nationwar.team.TeamMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class TeamInviteChatListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();

        if (msg.equalsIgnoreCase("/수락")) {
            event.setCancelled(true);

            if (!TeamMain.hasInvite(player)) {
                player.sendMessage("유효한 팀 초대가 없습니다.");
                return;
            }

            TeamMain.acceptInvite(player);
            player.sendMessage("팀 초대를 수락하셨습니다.");
        }

        if (msg.equalsIgnoreCase("/거절")) {
            event.setCancelled(true);

            if (!TeamMain.hasInvite(player)) {
                player.sendMessage("유효한 팀 초대가 없습니다.");
                return;
            }

            TeamMain.denyInvite(player);
            player.sendMessage("팀 초대를 거절하셨습니다.");
        }
    }
}
