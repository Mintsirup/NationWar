package com.nationwar.team;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;

import java.util.*;

/**
 * TeamManager keeps teams in-memory and provides utility methods.
 */
public class TeamManager {

    private final NationWar plugin;
    private final Map<String, Team> teamsByName = new HashMap<>();

    public TeamManager(NationWar plugin) {
        this.plugin = plugin;
    }

    public synchronized boolean createTeam(String name, UUID leader) {
        String key = normalize(name);
        if (teamsByName.containsKey(key)) return false;
        Team team = new Team(name, leader);
        teamsByName.put(key, team);
        return true;
    }

    public synchronized boolean disbandTeam(String name) {
        String key = normalize(name);
        return teamsByName.remove(key) != null;
    }

    public synchronized Team getTeamByName(String name) {
        return teamsByName.get(normalize(name));
    }

    public synchronized Team getTeamOf(UUID playerUuid) {
        for (Team t : teamsByName.values()) {
            if (t.isMember(playerUuid)) return t;
        }
        return null;
    }

    public synchronized boolean addMember(String teamName, UUID playerUuid) {
        Team t = getTeamByName(teamName);
        if (t == null) return false;
        if (getTeamOf(playerUuid) != null) return false; // already in a team
        t.addMember(playerUuid);
        return true;
    }

    public synchronized boolean removeMember(String teamName, UUID playerUuid) {
        Team t = getTeamByName(teamName);
        if (t == null) return false;
        t.removeMember(playerUuid);
        return true;
    }

    public synchronized List<Team> getAllTeams() {
        return new ArrayList<>(teamsByName.values());
    }

    public synchronized void loadFromList(List<Team> list) {
        teamsByName.clear();
        for (Team t : list) {
            teamsByName.put(normalize(t.getName()), t);
        }
    }

    public synchronized void createWandererIfAbsent() {
        // Ensure "방랑자" team exists
        String wanderer = "방랑자";
        if (getTeamByName(wanderer) == null) {
            // create with a dummy leader UUID (all zeros) since it's a system team
            createTeam(wanderer, new UUID(0L, 0L));
            // remove the dummy leader from members so it's empty
            Team t = getTeamByName(wanderer);
            if (t != null) {
                t.getMembers().clear();
            }
            plugin.getLogger().info("Default team '방랑자' created");
        }
    }

    private String normalize(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
}
