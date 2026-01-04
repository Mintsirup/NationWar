package com.nationwar.team;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class TeamChest {
    public static Inventory createChest(String teamName) {
        return Bukkit.createInventory(null, 54, "§0" + teamName + " 국가 창고");
    }
}