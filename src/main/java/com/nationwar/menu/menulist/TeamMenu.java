package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamMenu {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8팀 메뉴");
        if (TeamMain.isLeader(player)) {
            inv.setItem(10, GUIManager.createItem(Material.CYAN_DYE, "§b팀 색 설정", "§7팀 닉네임 색상을 변경합니다."));
            inv.setItem(13, GUIManager.createItem(Material.PLAYER_HEAD, "§e팀 초대", "§7방랑자를 팀으로 초대합니다."));
            inv.setItem(16, GUIManager.createItem(Material.BARRIER, "§c팀 삭제", "§7팀을 영구적으로 삭제합니다."));
        } else {
            inv.setItem(13, GUIManager.createItem(Material.CHEST, "§6국가 창고", "§7팀 공용 창고를 확인합니다."));
        }
        player.openInventory(inv);
    }
}