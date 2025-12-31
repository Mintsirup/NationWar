package com.nationwar.menu.menulist;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InfoMenu implements Listener {

    private final Player player;
    private final Inventory inv;

    public InfoMenu(Player player) {
        this.player = player;
        this.inv = Bukkit.createInventory(null, 27, "NationWar 정보");

        Bukkit.getPluginManager().registerEvents(this, NationWar.getInstance());
        setupItems();
    }

    private void setupItems() {
        inv.setItem(11, createItem(Material.BOOK, "게임 안내", List.of(
                "• 국가를 만들고 영토를 지켜라",
                "• 코어를 점령하면 승리",
                "• 팀워크가 핵심일겁니다",
                "• 행운을 빕니다"
        )));

        inv.setItem(15, createItem(Material.PAPER, "제작 규칙", List.of(
                "• 본체 클래스 1개 원칙",
                "• Helper / Util 금지",
                "• GUI = 화면 1클래스"
        )));
    }

    private ItemStack createItem(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void open() {
        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals("NationWar 정보")) return;
        if (e.getWhoClicked() instanceof Player) {
            e.setCancelled(true); // 정보 메뉴는 클릭 동작 없음
        }
    }
}
