package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public class MainMenu {
    public static void open(Player p) {
        Inventory inv = GUIManager.createMenu("§0메인 메뉴", 27);

        inv.setItem(11, createItem(Material.IRON_SWORD, "§e§l팀 관리", "§f나의 팀 상태를 확인하고 관리합니다."));
        inv.setItem(13, createItem(Material.BEACON, "§b§l코어 현황", "§f전 서버 코어의 점령 상태를 확인합니다."));
        inv.setItem(15, createItem(Material.BOOK, "§a§l전쟁 정보", "§f전쟁 규칙 및 개인 스탯을 확인합니다."));

        p.openInventory(inv);
    }

    private static ItemStack createItem(Material m, String name, String... lore) {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
}