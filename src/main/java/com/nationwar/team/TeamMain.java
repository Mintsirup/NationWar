package com.nationwar.team;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import java.util.*;

public class TeamMain {
    public Map<UUID, String> playerTeams = new HashMap<>();
    public Map<String, UUID> teamLeaders = new HashMap<>();
    public Map<String, List<UUID>> teamMembers = new HashMap<>();
    public Map<String, String> teamColors = new HashMap<>();
    public Map<String, Inventory> teamChests = new HashMap<>();

    public void createTeam(String teamName, UUID leaderId) {
        if (playerTeams.containsKey(leaderId) && !playerTeams.get(leaderId).equals("방랑자")) return;

        playerTeams.put(leaderId, teamName);
        teamLeaders.put(teamName, leaderId);

        List<UUID> members = new ArrayList<>();
        members.add(leaderId);
        teamMembers.put(teamName, members);

        teamColors.put(teamName, "§f");
        teamChests.put(teamName, TeamChest.createChest(teamName));
    }

    public void removeTeam(String teamName) {
        List<UUID> members = teamMembers.get(teamName);
        if (members != null) {
            for (UUID uuid : members) {
                playerTeams.put(uuid, "방랑자");
            }
        }
        teamLeaders.remove(teamName);
        teamMembers.remove(teamName);
        teamColors.remove(teamName);
        teamChests.remove(teamName);
    }
}