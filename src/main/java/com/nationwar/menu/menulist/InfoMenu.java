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

        ItemStack guide = createItem(Material.KNOWLEDGE_BOOK, "§a§l플레이 가이드");
        ItemMeta gm = guide.getItemMeta();
        gm.setLore(Arrays.asList(
                "§71. §f/팀 <이름>으로 국가를 세우세요.",
                "§72. §f팀원을 모집하고 §e국가창고§f를 활용하세요.",
                "§73. §6월/수/금/일 19:00§f에 열리는 코어를 공격하세요.",
                "§74. §f모든 코어(6개)를 차지하면 승리합니다!"
        ));
        guide.setItemMeta(gm);
        inv.setItem(13, guide); // 기존 stick 대신 가이드북 배치

        ItemStack stick = createItem(Material.DEBUG_STICK, "§b명령어 정보");
        ItemMeta sm = stick.getItemMeta();
        sm.setLore(Arrays.asList("§f/메뉴, /팀", "§f/tpa /국가창고"));
        stick.setItemMeta(sm);

        inv.setItem(10, guide);
        inv.setItem(13, stick);
        inv.setItem(16, createItem(Material.COMMAND_BLOCK, "§d제작자. Mintina_"));
        p.openInventory(inv);
    }

    private ItemStack createItem(Material m, String n) {
        ItemStack s = new ItemStack(m); ItemMeta mt = s.getItemMeta(); mt.setDisplayName(n); s.setItemMeta(mt); return s;
    }
}