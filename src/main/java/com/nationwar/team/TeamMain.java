package com.nationwar.team;

import com.nationwar.NationWar;
import org.bukkit.inventory.Inventory;
import java.util.*;
import java.util.stream.Collectors;

public class TeamMain {
    public Map<UUID, String> playerTeams = new HashMap<>();
    public Map<String, UUID> teamLeaders = new HashMap<>();
    public Map<String, List<UUID>> teamMembers = new HashMap<>();
    public Map<String, String> teamColors = new HashMap<>();
    public Map<String, Inventory> teamChests = new HashMap<>();
    private final TeamGson teamGson = new TeamGson();

    public void createTeam(String teamName, UUID leaderId) {
        if (hasTeam(leaderId)) return;

        playerTeams.put(leaderId, teamName);
        teamLeaders.put(teamName, leaderId);

        List<UUID> members = new ArrayList<>();
        members.add(leaderId);
        teamMembers.put(teamName, members);
        teamColors.put(teamName, "§f");

        if (!teamChests.containsKey(teamName)) {
            teamChests.put(teamName, TeamChest.createChest(teamName));
        }

        saveTeams(); // 데이터 변경 시 저장
    }

    public void saveTeams() {
        Map<String, Object> data = new HashMap<>();
        Map<String, List<String>> serializeMembers = new HashMap<>();

        // 팀별 UUID 리스트를 문자열 리스트로 변환
        for (Map.Entry<String, List<UUID>> entry : teamMembers.entrySet()) {
            serializeMembers.put(entry.getKey(), entry.getValue().stream()
                    .map(UUID::toString)
                    .collect(Collectors.toList()));
        }

        data.put("teams", serializeMembers);
        teamGson.save(data);
    }

    public boolean hasTeam(UUID uuid) {
        return playerTeams.containsKey(uuid) && !playerTeams.get(uuid).equals("방랑자");
    }
}