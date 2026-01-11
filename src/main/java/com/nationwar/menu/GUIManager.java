package com.nationwar.menu;

import com.nationwar.menu.menulist.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIManager implements Listener {

    private final JavaPlugin plugin;

    public GUIManager(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /* =========================
       GUI 열기 메서드
     ========================= */

    public void openMainMenu(Player player) {
        player.openInventory(new MainMenu(player).getInventory());
    }

    public void openTeamMenu(Player player) {
        player.openInventory(new TeamMenu(player).getInventory());
    }

    public void openTeamColorMenu(Player player) {
        player.openInventory(new TeamColorMenu(player).getInventory());
    }

    public void openTeamInviteListMenu(Player player) {
        player.openInventory(new TeamInviteListMenu(player).getInventory());
    }

    public void openTeamInviteConfirmMenu(Player player, Player target) {
        player.openInventory(new TeamInviteConfirmMenu(player, target).getInventory());
    }

    public void openTeamDeleteConfirmMenu(Player player) {
        player.openInventory(new TeamDeleteConfirmMenu(player).getInventory());
    }

    public void openCoreMenu(Player player) {
        player.openInventory(new CoreMenu(player).getInventory());
    }

    public void openInfoMenu(Player player) {
        player.openInventory(new InfoMenu(player).getInventory());
    }

    /* =========================
       클릭 이벤트 분기
     ========================= */

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory inv = event.getInventory();
        if (inv == null) return;

        if (inv.getHolder() instanceof GUIMenu menu) {
            event.setCancelled(true);
            menu.onClick(player, event.getSlot());
        }
    }
}
