package com.nationwar.team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamChest {

    public static void open(Player player) {

        TeamMain team = TeamMain.getPlayerTeam(player);

        if (team == TeamMain.getWandererTeam()) {
            player.sendMessage("방랑자는 국가 창고를 사용할 수 없습니다.");
            return;
        }

        Inventory inv = team.getTeamChest();

        if (inv == null) {
            inv = Bukkit.createInventory(null, 54, team.getDisplayName() + " 국가 창고");
            team.teamChest = inv;
        }

        player.openInventory(inv);
    }
}
