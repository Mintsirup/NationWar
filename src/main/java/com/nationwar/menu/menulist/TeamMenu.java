package com.nationwar.menu.menulist;

import com.nationwar.NationWar;
import com.nationwar.menu.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public class TeamMenu {
    public static void open(Player p) {
        Inventory inv = GUIManager.createMenu("§0팀 메뉴", 27);
        String team = NationWar.getInstance().getTeamMain().getPlayerTeam(p.getUniqueId());

        inv.setItem(10, createItem(Material.CHEST, "§6국가창고 열기", "§f팀원 전용 창고를 엽니다."));
        inv.setItem(12, createItem(Material.NAME_TAG, "§b팀원 초대", "§f다른 플레이어를 우리 팀으로 초대합니다."));
        inv.setItem(14, createItem(Material.CYAN_DYE, "§3팀 색상 변경", "§f보스바 및 팀 색상을 설정합니다."));
        inv.setItem(16, createItem(Material.BARRIER, "§c팀 해체/탈퇴", "§7팀을 나갑니다."));

        p.openInventory(inv);
    }
    private static ItemStack createItem(Material m, String n, String l) {
        ItemStack i = new ItemStack(m); ItemMeta mt = i.getItemMeta();
        mt.setDisplayName(n); mt.setLore(Arrays.asList(l)); i.setItemMeta(mt); return i;
    }
}