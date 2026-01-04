package com.nationwar.listeners;

import com.nationwar.menu.menulist.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (!title.startsWith("§0")) return; // 우리 플러그인 메뉴는 제목이 §0으로 시작함

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) return;

        if (title.equals("§0메인 메뉴")) {
            switch (event.getCurrentItem().getType()) {
                case IRON_SWORD -> TeamMenu.open(player);
                case BEACON -> CoreMenu.open(player);
                case BOOK -> InfoMenu.open(player);
            }
        } else if (title.equals("§0코어 현황")) {
            if (event.getCurrentItem().getType().toString().contains("WOOL")) {
                player.closeInventory();
                // 코어 좌표 메시지 전송 로직 등 추가 가능
            }
        }
        // 나머지 메뉴(팀 관리, 색상 변경 등)에 대한 클릭 로직을 여기에 추가
    }
}