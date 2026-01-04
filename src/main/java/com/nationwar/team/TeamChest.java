package com.nationwar.team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import java.util.HashMap;
import java.util.Map;

public class TeamChest {
    // 팀 이름별 인벤토리 저장소
    private final Map<String, Inventory> teamInventories = new HashMap<>();

    /**
     * 팀 창고 열기
     * @param player 창고를 여는 플레이어
     * @param teamName 해당 플레이어의 팀 이름
     */
    public void openChest(Player player, String teamName) {
        // 해당 팀의 창고가 없으면 새로 생성 (6줄 - 54칸)
        Inventory inventory = teamInventories.computeIfAbsent(teamName,
                name -> Bukkit.createInventory(null, 54, "§0국가창고 - " + name));

        player.openInventory(inventory);
    }

    // 서버 종료 시 창고 아이템 정보를 저장하는 로직이 추가될 수 있습니다.
    public Map<String, Inventory> getTeamInventories() {
        return teamInventories;
    }
}