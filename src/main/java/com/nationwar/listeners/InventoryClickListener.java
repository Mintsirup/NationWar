package com.nationwar.listeners;

import com.nationwar.menu.GUIManager;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        String name = event.getCurrentItem().getItemMeta().getDisplayName();

        event.setCancelled(true);

        if (title.equals("국가 전쟁 메뉴")) {
            if (name.equals("팀 메뉴")) {
                GUIManager.openTeamMenu(player);
            }
            if (name.equals("코어 메뉴")) {
                GUIManager.openCoreMenu(player);
            }
            if (name.equals("정보 메뉴")) {
                GUIManager.openInfoMenu(player);
            }
        }

        if (title.equals("팀 메뉴")) {
            if (name.equals("팀 초대")) {
                GUIManager.openTeamInviteListMenu(player);
            }
            if (name.equals("팀 색 설정")) {
                GUIManager.openTeamColorMenu(player);
            }
            if (name.equals("팀 삭제")) {
                GUIManager.openTeamDeleteConfirmMenu(player);
            }
        }

        if (title.equals("팀 초대 확인")) {
            if (name.equals("초대 수락")) {
                Player target = Bukkit.getPlayerExact("플레이어"); // 임시 대상
                if (target != null && TeamMain.canInvite(target.getName())) {
                    TeamMain.invite(target, TeamMain.getTeam(player));

                    target.sendMessage(
                            "§e" + TeamMain.getTeam(player) + " 팀에서 초대를 보냈습니다.\n" +
                                    "§a[수락] §c[거절]"
                    );
                }
                player.closeInventory();
            }
        }


        if (title.equals("팀 초대")) {
            if (name.equals("플레이어")) {
                GUIManager.openTeamInviteConfirmMenu(player);
            }
        }
    }
}
