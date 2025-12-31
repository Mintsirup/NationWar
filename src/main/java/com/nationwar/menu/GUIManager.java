package com.nationwar.menu;

import com.nationwar.menu.menulist.InfoMenu;
import org.bukkit.entity.Player;

public class GUIManager {

    public static void openInfoMenu(Player player) {
        new InfoMenu(player).open();
    }
}
