package com.nationwar.menu;

import com.nationwar.NationWar;
import com.nationwar.menu.menulist.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public class GUIManager {
    public void openMainMenu(Player player) { player.openInventory(MainMenu.getInventory()); }
    public void openCoreMenu(Player player) { player.openInventory(CoreMenu.getInventory()); }
    public void openInfoMenu(Player player) { player.openInventory(InfoMenu.getInventory()); }
    public void openTeamMenu(Player player) { player.openInventory(TeamMenu.getInventory()); }
    public void openTeamColorMenu(Player player) { player.openInventory(TeamColorMenu.getInventory()); }
    public void openTeamInviteListMenu(Player player) { player.openInventory(TeamInviteListMenu.getInventory()); }

    public void handleMenuClick(Player player, String title, int slot, ItemStack item) {
        if (item == null || item.getType() == Material.GRAY_STAINED_GLASS_PANE) return;

        if (title.contains("메인 메뉴")) {
            if (slot == 10) openCoreMenu(player);
            else if (slot == 13) openInfoMenu(player);
            else if (slot == 16) openTeamMenu(player);
            else if (slot == 22) player.closeInventory();
        } else if (title.contains("코어 메뉴")) {
            if (slot == 19) openMainMenu(player);
            else if (slot >= 10 && slot <= 16 && slot != 13) {
                // 팀 소유권 확인 후 텔레포트 로직
                int coreIdx = (slot < 13) ? slot - 10 : slot - 11;
                String team = NationWar.getInstance().getTeamMain().playerTeams.get(player.getUniqueId());
                String owner = NationWar.getInstance().getCoreMain().coreOwners.get(coreIdx);
                if (team != null && team.equals(owner)) {
                    player.teleport(NationWar.getInstance().getCoreMain().coreLocations.get(coreIdx));
                    player.sendMessage("코어로 이동했습니다.");
                }
            }
        } else if (title.contains("팀 메뉴")) {
            if (slot == 10) openTeamColorMenu(player);
            else if (slot == 11) openTeamInviteListMenu(player);
            else if (slot == 12) player.openInventory(TeamDeleteConfirmMenu.getInventory());
            else if (slot == 19) openMainMenu(player);
        } else if (title.contains("정보 메뉴") || title.contains("팀 색") || title.contains("초대") || title.contains("삭제")) {
            if (item.getType() == Material.ARROW) openMainMenu(player);
        }
    }

    public static ItemStack createItem(Material m, String name, String... lore) {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (lore.length > 0) meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static void fillGui(org.bukkit.inventory.Inventory inv, int... exclude) {
        ItemStack glass = createItem(Material.GRAY_STAINED_GLASS_PANE, "§8");
        for (int i = 0; i < inv.getSize(); i++) {
            final int slot = i;
            if (Arrays.stream(exclude).noneMatch(s -> s == slot)) inv.setItem(i, glass);
        }
    }
}