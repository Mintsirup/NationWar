package com.nationwar.listeners;

import com.nationwar.menu.CoreTeleportHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        // 좌표 변화가 없을 때(시점만 돌릴 때)는 무시
        if (event.getFrom().getX() == event.getTo().getX() &&
                event.getFrom().getZ() == event.getTo().getZ()) return;

        // 텔레포트 중이라면 취소 로직 실행
        if (CoreTeleportHandler.isTeleporting(event.getPlayer())) {
            CoreTeleportHandler.cancelTeleport(event.getPlayer(), true);
        }
    }
}