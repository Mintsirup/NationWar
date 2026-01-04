package com.nationwar.menu.menulist;

import com.nationwar.NationWar;
import com.nationwar.core.CoreMain;
import com.nationwar.menu.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public class CoreMenu {
    public static void open(Player p) {
        Inventory inv = GUIManager.createMenu("§0코어 현황", 27);
        CoreMain cm = NationWar.getInstance().getCoreMain();
        int[] slots = {10, 11, 12, 14, 15, 16};

        for (int i = 0; i < 6; i++) {
            CoreMain.CoreData data = cm.getCores().get(i + 1);
            if (data == null) continue;

            Material mat = data.ownerTeam.equals("없음") ? Material.LIGHT_GRAY_WOOL : Material.LIME_WOOL;
            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§l● " + (i + 1) + "번 코어");
            meta.setLore(Arrays.asList("§f소유: §7" + data.ownerTeam, "§bX: " + (int)data.x + " Z: " + (int)data.z, "§e클릭 시 좌표가 채팅창에 표시됩니다."));
            item.setItemMeta(meta);
            inv.setItem(slots[i], item);
        }
        p.openInventory(inv);
    }
}