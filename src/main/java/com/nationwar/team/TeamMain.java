package com.nationwar.team;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeamMain {

    private static final Map<String, String> teamOwner = new HashMap<>();
    private static final Map<Player, String> playerTeam = new HashMap<>();

    public static void createTeam(String name, Player owner) {
        teamOwner.put(name, owner.getName());
        playerTeam.put(owner, name);
        TeamGson.save();
    }

    public static boolean teamExists(String name) {
        return teamOwner.containsKey(name);
    }

    public static String getTeam(Player p) {
        if (!playerTeam.containsKey(p)) return "방랑자";
        return playerTeam.get(p);
    }

    public static void setTeam(Player p, String team) {
        playerTeam.put(p, team);
        TeamGson.save();
    }

    public static boolean isOwner(Player p) {
        String t = getTeam(p);
        if (!teamOwner.containsKey(t)) return false;
        return teamOwner.get(t).equals(p.getName());
    }

    public static Map<String, String> getTeamOwnerMap() {
        return teamOwner;
    }

    public static Map<Player, String> getPlayerTeamMap() {
        return playerTeam;
    }
}
