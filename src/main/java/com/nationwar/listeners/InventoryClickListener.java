package com.nationwar.listeners;

import com.nationwar.menu.GUIManager;
import com.nationwar.team.TeamMain;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getCurrentItem() == null) return;

        ItemStack item = event.getCurrentItem();
        String title = event.getView().getTitle();

        /* ================= 메인 메뉴 ================= */

        if (title.equals("메인 메뉴")) {
            event.setCancelled(true);

            if (item.getType() == Material.WHITE_BANNER) {
                GUIManager.openTeam(player);
            }

            if (item.getType() == Material.BEACON) {
                GUIManager.openCore(player);
            }

            if (item.getType() == Material.BOOK) {
                GUIManager.openInfo(player);
            }
            return;
        }

        /* ================= 팀 메뉴 ================= */

        if (title.equals("팀 메뉴")) {
            event.setCancelled(true);

            if (item.getType() == Material.PLAYER_HEAD) {
                GUIManager.openTeamInvite(player);
            }

            if (item.getType() == Material.RED_DYE) {
                GUIManager.openTeamColor(player);
            }

            if (item.getType() == Material.BARRIER) {
                GUIManager.openTeamDeleteConfirm(player);
            }
            return;
        }

        /* ================= 팀 색 설정 ================= */

        if (title.equals("팀 색 설정")) {
            event.setCancelled(true);

            TeamMain team = TeamMain.getPlayerTeam(player);
            if (team == TeamMain.getWandererTeam()) return;
            if (!team.getMembers().contains(player.getUniqueId())) return;
            if (!player.getUniqueId().equals(team.getLeader())) {
                player.sendMessage("팀장만 팀 색을 변경할 수 있습니다.");
                return;
            }

            if (item.getType() == Material.RED_DYE) {
                team.setColor(org.bukkit.ChatColor.RED);
                player.sendMessage("팀 색상이 변경되었습니다.");
            }

            if (item.getType() == Material.BLUE_DYE) {
                team.setColor(org.bukkit.ChatColor.BLUE);
                player.sendMessage("팀 색상이 변경되었습니다.");
            }

            if (item.getType() == Material.GREEN_DYE) {
                team.setColor(org.bukkit.ChatColor.GREEN);
                player.sendMessage("팀 색상이 변경되었습니다.");
            }
            return;
        }

        /* ================= 팀 삭제 확인 ================= */

        if (title.equals("팀 삭제 확인")) {
            event.setCancelled(true);

            TeamMain team = TeamMain.getPlayerTeam(player);
            if (team == TeamMain.getWandererTeam()) return;
            if (!player.getUniqueId().equals(team.getLeader())) {
                player.sendMessage("팀장만 팀을 삭제할 수 있습니다.");
                return;
            }

            if (item.getType() == Material.GREEN_WOOL) {
                team.delete();
                player.closeInventory();
                player.sendMessage("팀이 삭제되었습니다.");
            }

            if (item.getType() == Material.RED_WOOL) {
                player.closeInventory();
            }
            return;
        }

        /* ================= 코어 / 정보 ================= */

        if (title.equals("코어 목록") || title.equals("정보")) {
            event.setCancelled(true);
        }
    }
}
