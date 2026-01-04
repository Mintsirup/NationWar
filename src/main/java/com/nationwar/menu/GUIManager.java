package com.nationwar.menu;

import com.nationwar.menu.menulist.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GUIManager {

    public static void openMain(Player player) {
        player.openInventory(new MainMenu(player).getInventory());
    }

    public static void openTeam(Player player) {
        player.openInventory(new TeamMenu(player).getInventory());
    }

    public static void openTeamInvite(Player player) {
        player.openInventory(new TeamInviteListMenu(player).getInventory());
    }

    public static void openTeamColor(Player player) {
        player.openInventory(new TeamColorMenu(player).getInventory());
    }

    public static void openTeamDeleteConfirm(Player player) {
        player.openInventory(new TeamDeleteConfirmMenu(player).getInventory());
    }

    public static void openCore(Player player) {
        player.openInventory(new CoreMenu(player).getInventory());
    }

    public static void openInfo(Player player) {
        player.openInventory(new InfoMenu(player).getInventory());
    }
}
