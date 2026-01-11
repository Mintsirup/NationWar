package com.nationwar.menu.menulist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InfoMenu implements GUIMenu {

    private final Inventory inventory;

    public InfoMenu() {
        this.inventory = Bukkit.createInventory(this, 27, "정보");

        inventory.setItem(10, book());
        inventory.setItem(13, debug());
        inventory.setItem(16, creator());
    }

    private ItemStack book() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e국가 전쟁");
        meta.setLore(List.of(
                "§7코어를 점령하여",
                "§76개의 코어를 모두 차지하면",
                "§7승리합니다.",
                "",
                "§719:00 ~ 20:00 점령 가능"
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack debug() {
        ItemStack item = new ItemStack(Material.DEBUG_STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a명령어 목록");
        meta.setLore(List.of(
                "§f/메뉴",
                "§f/팀",
                "§f/tpa",
                "§f/국가창고"
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack creator() {
        ItemStack item = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§b제작자");
        meta.setLore(List.of("§fMintina_"));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Inventory getInventoryHolder() {
        return inventory;
    }

    @Override
    public void onClick(Player player, int slot) {
        // 전부 설명용 → 클릭해도 아무 일 없음
    }
}
