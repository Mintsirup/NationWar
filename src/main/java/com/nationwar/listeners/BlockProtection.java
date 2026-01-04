package com.nationwar.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockProtection implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.WHITE_CONCRETE) {
            if (!e.getPlayer().isOp()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§c코어 블록은 일반적인 방법으로 파괴할 수 없습니다.");
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        // 코어 주변 설치 제한 로직 필요 시 추가
    }
}