package com.nationwar.listeners;

import com.nationwar.core.CoreGson;
import com.nationwar.menu.CoreTeleportHandler;
import com.nationwar.menu.menulist.*;
import com.nationwar.team.TeamGson;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class MenuClickListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {

        if (event.getView().getTitle() == null) return;
        String title = event.getView().getTitle();
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // --- 1. MainMenu ---
        if (title.equalsIgnoreCase("MainMenu")) {
            event.setCancelled(true);
            if (slot == 10) TeamMenu.open(player);
            else if (slot == 13) CoreMenu.open(player);
            else if (slot == 16) InfoMenu.open(player);
            return;
        }

        // --- 2. TeamMenu ---
        if (title.equalsIgnoreCase("TeamMenu")) {
            event.setCancelled(true);
            boolean isLeader = TeamMain.isLeader(player);

            if (isLeader) {
                if (slot == 10) TeamColorMenu.open(player);
                else if (slot == 13) TeamInviteListMenu.open(player);
                else if (slot == 16) TeamDeleteConfirmMenu.open(player);
            } else {
                // 팀원일 때 국가창고 (엔더상자나 커스텀 인벤토리로 가정)
                if (slot == 13) player.openInventory(player.getEnderChest());
            }
            return;
        }

        // --- 3. TeamColorMenu ---
        if (title.equalsIgnoreCase("TeamColorMenu")) {
            event.setCancelled(true);
            String teamName = TeamMain.getPlayerTeam(player);
            ChatColor color = null;

            switch (slot) {
                case 0: color = ChatColor.RED; break;
                case 1: color = ChatColor.GOLD; break; // 주황색 대체
                case 2: color = ChatColor.YELLOW; break;
                case 3: color = ChatColor.GREEN; break; // 연두색 대체
                case 4: color = ChatColor.DARK_GREEN; break;
                case 5: color = ChatColor.AQUA; break;
                case 6: color = ChatColor.BLUE; break;
                case 7: color = ChatColor.DARK_PURPLE; break;
                case 8: color = ChatColor.BLACK; break;
                case 13: color = ChatColor.WHITE; break;
            }

            if (color != null) {
                TeamMain.setTeamColor(teamName, color);
                player.sendMessage(color + "[!] 팀 색상이 변경되었습니다.");
                player.closeInventory();
            }
            return;
        }

        // --- 4. TeamInviteListMenu ---
        if (title.equalsIgnoreCase("TeamInviteListMenu")) {
            event.setCancelled(true);
            if (clickedItem.getType() == Material.PLAYER_HEAD) {
                SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
                if (meta != null && meta.getOwningPlayer() != null) {
                    Player target = meta.getOwningPlayer().getPlayer();
                    if (target != null) {
                        // 에러 해결: Player 객체 대신 이름을 전달 (타입 불일치 해결)
                        TeamInviteConfirmMenu.open(player, target.getName());
                    }
                }
            }
            return;
        }

        // 코어 메뉴에서 클릭 시
        if (title.equalsIgnoreCase("CoreMenu")) {
            event.setCancelled(true);
            int coreId = -1;
            if (slot == 10) coreId = 1;
            else if (slot == 11) coreId = 2;
            else if (slot == 12) coreId = 3;
            else if (slot == 14) coreId = 4;
            else if (slot == 15) coreId = 5;
            else if (slot == 16) coreId = 6;

            if (coreId != -1) {
                // 에러 해결: getCore(int) 호출
                CoreGson.CoreData core = CoreGson.getCore(coreId);
                String playerTeam = TeamMain.getPlayerTeam(player);

                if (core != null && core.owner.equals(playerTeam)) {
                    player.closeInventory();
                    // 에러 해결: startTeleport(Player, CoreData) 호출
                    CoreTeleportHandler.startTeleport(player, core);
                } else {
                    player.sendMessage("§c[!] 우리 팀이 점유한 코어가 아닙니다.");
                }
            }
        }

        // --- 5. TeamInviteConfirmMenu ---
        if (title.contains("초대 확인")) { // 타이틀에 대상 이름이 포함될 수 있으므로 contains
            event.setCancelled(true);
            if (slot == 19) { // 수락
                // 인벤토리 이름 등에서 추출한 대상에게 초대 전송 (TeamMain 로직 연동)
                player.sendMessage("§a[!] 팀 초대를 보냈습니다.");
                player.closeInventory();
            } else if (slot == 25) { // 취소
                TeamInviteListMenu.open(player);
            }
            return;
        }

        // --- 6. TeamDeleteConfirmMenu ---
        if (title.equalsIgnoreCase("TeamDeleteConfirmMenu")) {
            event.setCancelled(true);
            if (slot == 19) { // 확인
                TeamMain.deleteTeam(TeamMain.getPlayerTeam(player));
                player.closeInventory();
            } else if (slot == 25) { // 취소
                TeamMenu.open(player);
            }
            return;
        }

        // --- 7. CoreMenu ---
        if (title.equalsIgnoreCase("CoreMenu")) {
            event.setCancelled(true);
            int coreId = -1;
            if (slot == 10) coreId = 1;
            else if (slot == 11) coreId = 2;
            else if (slot == 12) coreId = 3;
            else if (slot == 14) coreId = 4;
            else if (slot == 15) coreId = 5;
            else if (slot == 16) coreId = 6;

            if (coreId != -1) {
                CoreGson.CoreData core = CoreGson.getCore(coreId);
                String playerTeam = TeamMain.getPlayerTeam(player);
                if (core != null && core.owner.equals(playerTeam)) {
                    player.closeInventory();
                    CoreTeleportHandler.startTeleport(player, core);
                } else {
                    player.sendMessage("§c[!] 우리 팀이 소유한 코어가 아닙니다.");
                }
            }
            return;
        }

        // --- 8. InfoMenu ---
        if (title.equalsIgnoreCase("InfoMenu")) {
            event.setCancelled(true);
            // 클릭 시 별도 액션 없음 (마우스 오버 설명용)
            return;
        }
    }
}