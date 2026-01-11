package com.nationwar.menu.menulist;

import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamColorMenu implements GUIMenu {

    private final Inventory inventory;

    public TeamColorMenu(Player player) {
        this.inventory = Bukkit.createInventory(this, 27, "팀 색 설정");

        inventory.setItem(0, colorItem(Material.RED_DYE, "빨강", ChatColor.RED));
        inventory.setItem(1, colorItem(Material.ORANGE_DYE, "주황", ChatColor.GOLD));
        inventory.setItem(2, colorItem(Material.YELLOW_DYE, "노랑", ChatColor.YELLOW));
        inventory.setItem(3, colorItem(Material.LIME_DYE, "연두", ChatColor.GREEN));
        inventory.setItem(4, colorItem(Material.GREEN_DYE, "초록", ChatColor.DARK_GREEN));
        inventory.setItem(5, colorItem(Material.LIGHT_BLUE_DYE, "하늘", ChatColor.AQUA));
        inventory.setItem(6, colorItem(Material.BLUE_DYE, "파랑", ChatColor.BLUE));
        inventory.setItem(7, colorItem(Material.PURPLE_DYE, "보라", ChatColor.DARK_PURPLE));
        inventory.setItem(8, colorItem(Material.BLACK_DYE, "검정", ChatColor.BLACK));
        inventory.setItem(13, colorItem(Material.WHITE_DYE, "하양", ChatColor.WHITE));
    }

    private ItemStack colorItem(Material mat, String name, ChatColor color) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color + name);
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
        TeamMain teamMain = TeamMain.getInstance();

        ChatColor color = switch (slot) {
            case 0 -> ChatColor.RED;
            case 1 -> ChatColor.GOLD;
            case 2 -> ChatColor.YELLOW;
            case 3 -> ChatColor.GREEN;
            case 4 -> ChatColor.DARK_GREEN;
            case 5 -> ChatColor.AQUA;
            case 6 -> ChatColor.BLUE;
            case 7 -> ChatColor.DARK_PURPLE;
            case 8 -> ChatColor.BLACK;
            case 13 -> ChatColor.WHITE;
            default -> null;
        };

        if (color == null) return;

        teamMain.setTeamColor(player, color);
        player.sendMessage("§a팀 색상이 변경되었습니다.");
        player.closeInventory();
    }
}
