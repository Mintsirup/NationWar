package com.nationwar.menu.menulist;

import com.nationwar.core.CoreGson;
import com.nationwar.menu.GUIManager;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CoreMenu {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8코어 메뉴");
        String myTeam = TeamMain.getPlayerTeam(player);
        int[] slots = {10, 11, 12, 14, 15, 16};

        for (int i = 0; i < 6; i++) {
            CoreGson.CoreData core = CoreGson.getCore(i);
            boolean isMine = core.owner.equals(myTeam) && !myTeam.equals("방랑자");

            Material mat = isMine ? Material.BEACON : Material.BARRIER;
            String status = isMine ? "§a[점령 중]" : "§c[미점령]";
            inv.setItem(slots[i], GUIManager.createItem(mat, "§l코어 " + i + " " + status,
                    isMine ? "§7클릭 시 10초 후 이동합니다." : "§7소유한 팀만 이동 가능합니다."));
        }
        player.openInventory(inv);
    }
}