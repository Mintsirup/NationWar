package com.nationwar.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        // TODO 메뉴 판별
        // TODO 메뉴별 클릭 처리
        // TODO 코어 메뉴 클릭 시 CoreMain 호출
    }
}
