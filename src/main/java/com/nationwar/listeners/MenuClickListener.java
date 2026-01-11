package com.nationwar.listeners;

import com.nationwar.menu.menulist.*;
import com.nationwar.team.TeamMain;
import com.nationwar.team.TeamChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        String title = e.getView().getTitle();
        Player p = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();

        if (title.equals("MainMenu")) {
            e.setCancelled(true);
            if (slot == 10) TeamMenu.open(p);
            if (slot == 13) CoreMenu.open(p);
            if (slot == 16) InfoMenu.open(p);
        }
        else if (title.equals("TeamMenu")) {
            e.setCancelled(true);
            if (TeamMain.isLeader(p)) {
                if (slot == 10) TeamColorMenu.open(p);
                if (slot == 13) TeamInviteListMenu.open(p);
                if (slot == 16) TeamDeleteConfirmMenu.open(p);
            } else if (slot == 13) {
                TeamChest.open(p);
            }
        }
        // 추가 메뉴 슬롯들은 기준서 수치에 따라 각 메뉴 클래스 로직에 대응됩니다.
    }
}