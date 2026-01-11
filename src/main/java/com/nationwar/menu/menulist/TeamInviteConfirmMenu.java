package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class TeamInviteConfirmMenu implements GUIMenu {

    private final Inventory inventory;
    private final Player target;

    public TeamInviteConfirmMenu(Player inviter, Player target) {
        this.target = target;
        this.inventory = Bukkit.createInventory(this, 27, "팀 초대 확인");

        // 4번 슬롯: 대상 플레이어 헤드 (아무 동작 없음)
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwningPlayer(target);
        skullMeta.setDisplayName("§a" + target.getName());
        head.setItemMeta(skullMeta);
        inventory.setItem(4, head);

        // 19번 슬롯: 수락(초대 전송)
        inventory.setItem(19, item(Material.LIME_WOOL, "§a초대 보내기"));

        // 25번 슬롯: 취소
        inventory.setItem(25, item(Material.RED_WOOL, "§c취소"));
    }

    private ItemStack item(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Inventory getInventoryHolder() {
        return inventory;
    }

    @Override
    public void onClick(Player player, int slot) {
        GUIManager gui = new GUIManager(Bukkit.getPluginManager().getPlugin("NationWar"));
        TeamMain teamMain = TeamMain.getInstance();

        if (slot == 19) {
            teamMain.sendInvite(player, target);
            player.closeInventory();
        } else if (slot == 25) {
            gui.openTeamInviteListMenu(player);
        }
    }
}
