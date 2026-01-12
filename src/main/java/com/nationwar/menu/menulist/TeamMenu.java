package com.nationwar.menu.menulist;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamMenu {
    private final NationWar plugin;
    public TeamMenu(NationWar plugin) { this.plugin = plugin; }

    public void open(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "팀 메뉴");
        String team = plugin.getTeamMain().getPlayerTeam(p.getUniqueId());

        if (plugin.getTeamMain().isLeader(team, p)) {
            inv.setItem(10, createItem(Material.CYAN_DYE, "§f팀 색 설정 메뉴"));
            inv.setItem(13, createItem(Material.PLAYER_HEAD, "§f플레이어 리스트 메뉴"));
            inv.setItem(16, createItem(Material.BARRIER, "§f팀 삭제 확인 메뉴"));
        } else {
            inv.setItem(13, createItem(Material.ENDER_CHEST, "§f국가창고"));
        }
        p.openInventory(inv);
    }

    private ItemStack createItem(Material m, String n) {
        ItemStack s = new ItemStack(m); ItemMeta mt = s.getItemMeta(); mt.setDisplayName(n); s.setItemMeta(mt); return s;
    }
}