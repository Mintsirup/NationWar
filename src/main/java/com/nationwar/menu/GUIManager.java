package com.nationwar.menu;

import com.nationwar.menu.menulist.*;
import org.bukkit.entity.Player;

public class GUIManager {

    public static void openMainMenu(Player player) {
        new MainMenu().open(player);
    }

    public static void openTeamMenu(Player player) {
        new TeamMenu().open(player);
    }

    public static void openCoreMenu(Player player) {
        new CoreMenu().open(player);
    }

    public static void openInfoMenu(Player player) {
        new InfoMenu().open(player);
    }

    public static void openTeamColorMenu(Player player) {
        new TeamColorMenu().open(player);
    }

    public static void openTeamInviteListMenu(Player player) {
        new TeamInviteListMenu().open(player);
    }

    public static void openTeamInviteConfirmMenu(Player player) {
        new TeamInviteConfirmMenu().open(player);
    }

    public static void openTeamDeleteConfirmMenu(Player player) {
        new TeamDeleteConfirmMenu().open(player);
    }
}
