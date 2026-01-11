package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamMenu implements GUIMenu {

    private final Player player;
    private final Inventory inventory;

    public TeamMenu(Player player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this, 27, "팀 메뉴");

        TeamMain teamMain = TeamMain.getInstance();
        boolean isLeader = teamMain.isLeader(player);

        if (isLeader) {
            // 팀장 UI
            inventory.setItem(10, createItem(Material.RED_DYE, "§c팀 색 설정"));
            inventory.setItem(13, createItem(Material.PLAYER_HEAD, "§a팀원 초대"));
            inventory.setItem(16, createItem(Material.IRON_SWORD, "§4팀 삭제"));
        } else {
            // 팀원 UI
            inventory.setItem(13, createItem(Material.CHEST, "§e국가 창고"));
        }
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
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

        if (teamMain.isLeader(player)) {
            if (slot == 10) {
                gui.openTeamColorMenu(player);
            } else if (slot == 13) {
                gui.openTeamInviteListMenu(player);
            } else if (slot == 16) {
                gui.openTeamDeleteConfirmMenu(player);
            }
        } else {
            if (slot == 13) {
                teamMain.openTeamChest(player);
            }
        }
    }
}
