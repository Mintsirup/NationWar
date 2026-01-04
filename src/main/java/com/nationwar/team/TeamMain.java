package com.nationwar.team;

import com.nationwar.NationWar;
import java.util.*;

public class TeamMain {
    private final Map<UUID, String> playerTeams = new HashMap<>(); // 유저 UUID - 팀 이름
    private final Map<String, List<UUID>> teamMembers = new HashMap<>(); // 팀 이름 - 멤버 UUID 리스트
    private final TeamChest teamChest;
    private final TeamGson teamGson;

    public TeamMain() {
        this.teamChest = new TeamChest();
        this.teamGson = new TeamGson();
        // 플러그인 시작 시 데이터 로드 로직을 여기에 추가할 수 있습니다.
    }

    // 플레이어의 팀 확인 (없으면 "방랑자" 반환)
    public String getPlayerTeam(UUID uuid) {
        return playerTeams.getOrDefault(uuid, "방랑자");
    }

    // 팀 생성 및 가입
    public void createTeam(String teamName, UUID leaderUUID) {
        if (playerTeams.containsKey(leaderUUID)) return;
        playerTeams.put(leaderUUID, teamName);
        teamMembers.computeIfAbsent(teamName, k -> new ArrayList<>()).add(leaderUUID);
    }

    // 팀 해체
    public void deleteTeam(String teamName) {
        List<UUID> members = teamMembers.get(teamName);
        if (members != null) {
            for (UUID uuid : members) {
                playerTeams.remove(uuid);
            }
            teamMembers.remove(teamName);
        }
    }

    // Getter들
    public TeamChest getTeamChest() { return teamChest; }
    public Map<String, List<UUID>> getTeamMembers() { return teamMembers; }
}