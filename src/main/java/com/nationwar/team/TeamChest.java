package com.nationwar.team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class TeamChest {

    private static final Map<String, Inventory> chestMap = new HashMap<>();

    public static Inventory getChest(String team) {
        if (!chestMap.containsKey(team)) {
            chestMap.put(team, Bukkit.createInventory(null, 54, "국가 창고"));
        }
        return chestMap.get(team);
    }

    public static void openChest(Player p) {
        String team = TeamMain.getTeam(p);
        if (team.equals("방랑자")) return;
        p.openInventory(getChest(team));
    }

    public static Map<String, Inventory> getChestMap() {
        return chestMap;
    }
}
