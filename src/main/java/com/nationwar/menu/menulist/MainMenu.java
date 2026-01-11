package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenu implements GUIMenu {

    private final Inventory inventory;

    public MainMenu() {
        this.inventory = Bukkit.createInventory(this, 27, "국가 전쟁 메뉴");

        inventory.setItem(10, item(Material.PLAYER_HEAD, "§a팀 메뉴"));
        inventory.setItem(13, item(Material.BEACON, "§b코어 메뉴"));
        inventory.setItem(16, item(Material.BOOK, "§e정보 메뉴"));
    }

    private ItemStack item(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
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
        GUIManager gui = new GUIManager(Bukkit.getPluginManager().getPlugin("NationWar"));

        if (slot == 10) {
            gui.openTeamMenu(player);
        } else if (slot == 13) {
            gui.openCoreMenu(player);
        } else if (slot == 16) {
            gui.openInfoMenu(player);
        }
    }
}
