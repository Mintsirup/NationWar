package com.nationwar.menu.menulist;

import com.nationwar.menu.GUIManager;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class TeamInviteListMenu implements GUIMenu {

    private final Inventory inventory;

    public TeamInviteListMenu(Player player) {
        this.inventory = Bukkit.createInventory(this, 27, "팀원 초대");

        TeamMain teamMain = TeamMain.getInstance();
        int slot = 0;

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (slot >= 27) break;
            if (target.equals(player)) continue;
            if (!teamMain.isWanderer(target)) continue;

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(target);
            meta.setDisplayName("§a" + target.getName());
            head.setItemMeta(meta);

            inventory.setItem(slot, head);
            slot++;
        }
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
        ItemStack item = inventory.getItem(slot);
        if (item == null || item.getType() != Material.PLAYER_HEAD) return;

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta == null || meta.getOwningPlayer() == null) return;

        UUID targetUUID = meta.getOwningPlayer().getUniqueId();
        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null) return;

        GUIManager gui = new GUIManager(Bukkit.getPluginManager().getPlugin("NationWar"));
        gui.openTeamInviteConfirmMenu(player, target);
    }
}
