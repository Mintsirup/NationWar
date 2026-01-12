package com.nationwar.team;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamMain {

    private final NationWar plugin;
    private final TeamGson teamGson;

    // 팀이름 -> UUID 문자열 리스트
    private Map<String, List<String>> teams = new HashMap<>();

    public static final String DEFAULT_TEAM = "방랑자";

    public TeamMain(NationWar plugin) {
        this.plugin = plugin;
        this.teamGson = new TeamGson(plugin);
    }

    /* ===================== 로드 / 저장 ===================== */

    public void loadTeams() {
        teams = teamGson.load();

        // 방랑자 팀 없으면 생성
        teams.putIfAbsent(DEFAULT_TEAM, new ArrayList<>());

        // 서버에 있는 플레이어 중 팀 없는 애들 방랑자로
        Bukkit.getOnlinePlayers().forEach(this::setDefaultTeam);
    }

    public void saveTeams() {
        teamGson.save(teams);
    }

    /* ===================== 기본 ===================== */

    public void setDefaultTeam(Player player) {
        removeFromAllTeams(player);
        teams.get(DEFAULT_TEAM).add(player.getUniqueId().toString());
    }

    private void removeFromAllTeams(Player player) {
        teams.values().forEach(list ->
                list.remove(player.getUniqueId().toString())
        );
    }

    public String getTeam(Player player) {
        String uuid = player.getUniqueId().toString();
        for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
            if (entry.getValue().contains(uuid)) {
                return entry.getKey();
            }
        }
        return DEFAULT_TEAM;
    }

    public boolean sameTeam(Player a, Player b) {
        return getTeam(a).equals(getTeam(b));
    }

    /* ===================== 팀 생성 / 삭제 ===================== */

    public boolean createTeam(String name, Player leader) {
        if (teams.containsKey(name)) return false;

        teams.put(name, new ArrayList<>());
        removeFromAllTeams(leader);
        teams.get(name).add(leader.getUniqueId().toString());
        saveTeams();
        return true;
    }

    public void deleteTeam(String name) {
        if (!teams.containsKey(name)) return;
        if (name.equals(DEFAULT_TEAM)) return;

        List<String> members = teams.get(name);
        teams.remove(name);

        // 팀원들 방랑자로
        members.forEach(uuid ->
                teams.get(DEFAULT_TEAM).add(uuid)
        );
        saveTeams();
    }

    /* ===================== 팀장 ===================== */

    public boolean isLeader(Player player) {
        String team = getTeam(player);
        if (team.equals(DEFAULT_TEAM)) return false;

        List<String> members = teams.get(team);
        return members != null && !members.isEmpty()
                && members.get(0).equals(player.getUniqueId().toString());
    }

    /* ===================== 초대 / 추가 ===================== */

    public void addPlayerToTeam(Player player, String team) {
        if (!teams.containsKey(team)) return;
        removeFromAllTeams(player);
        teams.get(team).add(player.getUniqueId().toString());
        saveTeams();
    }

    public Map<String, List<String>> getTeams() {
        return teams;
    }
}
