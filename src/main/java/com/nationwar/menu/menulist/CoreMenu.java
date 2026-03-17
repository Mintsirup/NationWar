package com.nationwar.menu.menulist;

import com.nationwar.NationWar;
import com.nationwar.core.CoreGson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class CoreMenu {
    private final NationWar plugin;
    public CoreMenu(NationWar plugin) { this.plugin = plugin; }

    public void open(Player p) {
        List<CoreGson.CoreInfo> cores = plugin.getCoreMain().getCoreData().cores;
        int coreCount = cores.size();
        String team = plugin.getTeamMain().getPlayerTeam(p.getUniqueId());

        // 코어 수에 따라 인벤토리 크기 결정 (9의 배수, 최소 9, 최대 54)
        int rows = Math.max(1, (int) Math.ceil(coreCount / 9.0) + 1);
        int invSize = Math.min(rows * 9, 54);

        Inventory inv = Bukkit.createInventory(null, invSize, "코어 메뉴");
        int[] slots = calcSlots(coreCount, invSize);

        for (int i = 0; i < coreCount; i++) {
            CoreGson.CoreInfo core = cores.get(i);
            boolean isOwner = core.owner.equals(team);
            Material mat = isOwner ? Material.BEACON : Material.BARRIER;

            List<String> lore = new ArrayList<>();
            lore.add("§7좌표: X: " + (int)core.x + " Y: " + (int)core.y + " Z: " + (int)core.z);
            lore.add("§7소유팀: " + (core.owner.equals("없음") ? "§7없음" : "§e" + core.owner));
            lore.add(isOwner ? "§a§l[클릭 시 텔레포트 가능]" : "§c§l[점령 시 텔레포트 가능]");

            inv.setItem(slots[i], createItem(mat, "§f코어 " + i, lore));
        }
        p.openInventory(inv);
    }

    /**
     * 코어들을 인벤토리 중앙에 균등 배치하는 슬롯 배열 계산
     * 한 행에 최대 9개, 행마다 중앙 정렬
     */
    private int[] calcSlots(int count, int invSize) {
        int[] slots = new int[count];
        int rows = invSize / 9;
        int totalRows = (int) Math.ceil((double) count / 9);
        int startRow = (rows - totalRows) / 2; // 수직 중앙 정렬

        int idx = 0;
        for (int row = 0; row < totalRows && idx < count; row++) {
            int itemsInRow = Math.min(9, count - idx);
            int startCol = (9 - itemsInRow) / 2; // 수평 중앙 정렬
            for (int col = 0; col < itemsInRow; col++) {
                slots[idx++] = (startRow + row) * 9 + startCol + col;
            }
        }
        return slots;
    }

    private ItemStack createItem(Material m, String n, List<String> lore) {
        ItemStack s = new ItemStack(m);
        ItemMeta mt = s.getItemMeta();
        mt.setDisplayName(n);
        if (lore != null) mt.setLore(lore);
        s.setItemMeta(mt);
        return s;
    }
}