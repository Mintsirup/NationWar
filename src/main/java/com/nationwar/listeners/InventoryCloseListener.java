package com.nationwar.listeners;

import com.nationwar.NationWar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InventoryCloseListener implements Listener {
    private final NationWar plugin;

    public InventoryCloseListener(NationWar plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String title = event.getView().getTitle();

        // "국가 창고" 타이틀 확인
        if (title.contains("국가 창고")) {
            Player player = (Player) event.getPlayer();

            // 타이틀에서 팀 이름 추출 (§0팀이름 국가 창고 -> 팀이름)
            String teamName = title.replace("§0", "").replace(" 국가 창고", "");

            // 1. 데이터 업데이트 및 저장 & 잠금 해제 & 로그 기록
            // 아까 TeamChest에 만든 close 메서드를 호출합니다.
            plugin.getTeamMain().getTeamChest().close(player, teamName, event.getInventory());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String teamName = plugin.getTeamMain().getPlayerTeam(player.getUniqueId());

        // 만약 플레이어가 창고를 열어둔 채로 나갔다면 잠금 강제 해제
        if (plugin.getTeamMain().isStorageLocked(teamName)) {
            plugin.getTeamMain().unlockStorage(teamName);
            plugin.getLogger().warning("[Storage] " + player.getName() + "님이 창고 사용 중 퇴장하여 " + teamName + " 팀 창고 잠금을 강제 해제했습니다.");
        }
    }
}