package com.nationwar.team;

import org.bukkit.entity.Player;
import java.util.List;
import java.util.Map;

public class TeamMain {
    public static String getPlayerTeam(Player player) {
        String uuid = player.getUniqueId().toString();
        for (Map.Entry<String, List<String>> entry : TeamGson.getTeams().entrySet()) {
            if (entry.getValue().contains(uuid)) return entry.getKey();
        }
        return "방랑자";
    }

    public static boolean isLeader(Player player) {
        String team = getPlayerTeam(player);
        if (team.equals("방랑자")) return false;
        List<String> members = TeamGson.getTeams().get(team);
        return members != null && !members.isEmpty() && members.get(0).equals(player.getUniqueId().toString());
    }
}