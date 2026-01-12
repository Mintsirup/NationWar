package com.nationwar.team;

import com.nationwar.NationWar;
import org.bukkit.entity.Player;
import java.io.File;
import java.util.*;

public class TeamMain {
    private final NationWar plugin;
    private final File teamFile;
    private TeamGson.TeamData data;
    private final TeamChest teamChest;

    public TeamMain(NationWar plugin) {
        this.plugin = plugin;
        this.teamFile = new File(plugin.getDataFolder(), "team.json");
        this.data = TeamGson.load(teamFile);
        this.teamChest = new TeamChest(); // 추가
    }

    public void createTeam(String teamName, Player leader) {
        // 기준서: 누구나 생성 가능, 생성자가 팀장이 된다.
        if (data.teams.containsKey(teamName)) return;
        List<String> members = new ArrayList<>();
        members.add(leader.getUniqueId().toString());
        data.teams.put(teamName, members);
        saveTeams();
    }

    public void deleteTeam(String teamName) {
        // 기준서: 팀을 삭제하고 팀원들을 방랑자로 변경
        data.teams.remove(teamName);
        data.colors.remove(teamName);
        saveTeams();
    }

    public boolean isLeader(String teamName, Player player) {
        // 기준서: 생성자가 팀장 (리스트의 0번째 인덱스)
        List<String> members = data.teams.get(teamName);
        return members != null && members.get(0).equals(player.getUniqueId().toString());
    }

    public String getPlayerTeam(UUID uuid) {
        for (Map.Entry<String, List<String>> entry : data.teams.entrySet()) {
            if (entry.getValue().contains(uuid.toString())) return entry.getKey();
        }
        return "방랑자"; // 기준서: 기본 상태
    }

    public TeamChest getTeamChest() { return teamChest; }
    public TeamGson.TeamData getData() { return data; }

    public boolean sameTeam(Player p1, Player p2) {
        String t1 = getPlayerTeam(p1.getUniqueId());
        String t2 = getPlayerTeam(p2.getUniqueId());
        return !t1.equals("방랑자") && t1.equals(t2);
    }

    public void saveTeams() { TeamGson.save(teamFile, data); }
}