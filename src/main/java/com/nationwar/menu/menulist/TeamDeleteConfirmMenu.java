package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamDeleteConfirmMenu implements GUIMenu {

    private final Inventory inventory;

    public TeamDeleteConfirmMenu(Player player) {
        this.inventory = Bukkit.createInventory(this, 27, "팀 삭제 확인");

        // 4번 슬롯: 철 검 (아무 동작 없음)
        inventory.setItem(4, item(Material.IRON_SWORD, "§c팀 삭제"));

        // 19번 슬롯: 확인
        inventory.setItem(19, item(Material.LIME_WOOL, "§a확인"));

        // 25번 슬롯: 취소
        inventory.setItem(25, item(Material.RED_WOOL, "§c취소"));
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
        TeamMain teamMain = TeamMain.getInstance();

        if (slot == 19) {
            teamMain.deleteTeam(player);
            player.closeInventory();
        } else if (slot == 25) {
            gui.openTeamMenu(player);
        }
    }
}
