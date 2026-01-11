package com.nationwar.menu.menulist;

import com.nationwar.core.CoreMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CoreMenu implements GUIMenu {

    private final Inventory inventory;

    public CoreMenu(Player player) {
        this.inventory = Bukkit.createInventory(this, 27, "코어 메뉴");

        CoreMain coreMain = CoreMain.getInstance();

        int[] slots = {10, 11, 12, 14, 15, 16};

        for (int i = 0; i < 6; i++) {
            boolean owned = coreMain.isOwnedByPlayerTeam(player, i);

            Material mat = owned ? Material.BEACON : Material.BARRIER;
            String name = owned ? "§a코어 " + i : "§c코어 " + i;

            inventory.setItem(slots[i], item(mat, name));
        }
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
        CoreMain coreMain = CoreMain.getInstance();

        int[] slots = {10, 11, 12, 14, 15, 16};

        for (int i = 0; i < slots.length; i++) {
            if (slot == slots[i]) {
                coreMain.tryTeleportToCore(player, i);
                return;
            }
        }
    }
}
