package com.nationwar.team;

import com.nationwar.core.CoreGson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.UUID;

public class TeamMain {

    public static void createTeam(Player owner, String teamName) {
        String lowerName = teamName.toLowerCase();
        if (lowerName.equals("null") || lowerName.equals("없음") || lowerName.equals("방랑자")) {
            owner.sendMessage("§c[!] 해당 이름은 팀 이름으로 사용할 수 없습니다.");
            return;
        }
        if (TeamGson.getTeams().containsKey(teamName)) {
            owner.sendMessage("§c[!] 이미 존재하는 팀 이름입니다.");
            return;
        }
        if (!getPlayerTeam(owner).equals("방랑자")) {
            owner.sendMessage("§c[!] 이미 소속된 팀이 있습니다.");
            return;
        }

        TeamGson.TeamData data = new TeamGson.TeamData();
        data.owner = owner.getUniqueId();
        data.members.add(owner.getUniqueId());

        TeamGson.getTeams().put(teamName, data);
        TeamGson.saveTeams();
        owner.sendMessage("§a[!] " + teamName + " 팀이 생성되었습니다.");
    }

    public static void deleteTeam(String teamName) {
        if (!TeamGson.getTeams().containsKey(teamName)) return;

        TeamGson.getTeams().remove(teamName);
        TeamGson.saveTeams();

        for (CoreGson.CoreData core : CoreGson.getCores()) {
            if (core.owner.equals(teamName)) {
                core.owner = "없음";
                core.hp = 5000;
            }
        }
        CoreGson.saveCores();
        Bukkit.broadcastMessage("§6[!] §e" + teamName + " 팀이 해체되었습니다.");
    }

    public static boolean isLeader(Player player) {
        String teamName = getPlayerTeam(player);
        if (teamName.equals("방랑자")) return false;
        TeamGson.TeamData data = TeamGson.getTeams().get(teamName);
        return data != null && data.owner.equals(player.getUniqueId());
    }

    public static void setTeamColor(String teamName, ChatColor color) {
        if (TeamGson.getTeams().containsKey(teamName)) {
            TeamGson.getTeams().get(teamName).color = color.name();
            TeamGson.saveTeams();
        }
    }

    public static String getPlayerTeam(Player player) {
        return TeamGson.getPlayerTeam(player.getUniqueId());
    }
}