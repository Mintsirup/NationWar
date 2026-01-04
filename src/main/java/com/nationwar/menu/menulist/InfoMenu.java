package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Collections;

public class InfoMenu {
    public static void open(Player p) {
        Inventory inv = GUIManager.createMenu("§0정보 메뉴", 27);
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§f전쟁 시간: §e19:00 ~ 20:00");
        meta.setLore(Collections.singletonList("§7모든 코어를 점령하는 팀이 승리합니다."));
        item.setItemMeta(meta);
        inv.setItem(13, item);
        p.openInventory(inv);
    }
}