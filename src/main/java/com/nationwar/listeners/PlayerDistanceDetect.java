package com.nationwar.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerDistanceDetect implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        // TODO 코어 250블록 접근 감지
        // TODO 보스바 표시
        // TODO 적 코어 접근 알림
    }
}
