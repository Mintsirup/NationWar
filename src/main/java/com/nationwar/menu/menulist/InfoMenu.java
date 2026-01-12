package com.nationwar.menu.menulist;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public class InfoMenu {
    private final NationWar plugin;
    public InfoMenu(NationWar plugin) { this.plugin = plugin; }

    public void open(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "정보 메뉴");

        ItemStack book = createItem(Material.BOOK, "§e국가전쟁 정보");
        ItemMeta bm = book.getItemMeta();
        bm.setLore(Arrays.asList("§f월, 수, 금 19:00~20:00 점령 가능", "§f코어 6개 점령 시 승리"));
        book.setItemMeta(bm);

        ItemStack stick = createItem(Material.DEBUG_STICK, "§b명령어 정보");
        ItemMeta sm = stick.getItemMeta();
        sm.setLore(Arrays.asList("§f/메뉴, /팀, /tpa", "§f/국가창고, /gamestart"));
        stick.setItemMeta(sm);

        inv.setItem(10, book);
        inv.setItem(13, stick);
        inv.setItem(16, createItem(Material.COMMAND_BLOCK, "§d제작자. Mintina_"));
        p.openInventory(inv);
    }

    private ItemStack createItem(Material m, String n) {
        ItemStack s = new ItemStack(m); ItemMeta mt = s.getItemMeta(); mt.setDisplayName(n); s.setItemMeta(mt); return s;
    }
}