package com.nationwar.team;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nationwar.NationWar;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamMain {

    private final NationWar plugin;
    private final TeamGson teamGson;

    // 팀 이름 → UUID 목록
    private final Map<String, Set<UUID>> teams = new HashMap<>();
    // 팀 이름 → 팀장
    private final Map<String, UUID> leaders = new HashMap<>();

    public TeamMain(NationWar plugin) {
        this.plugin = plugin;
        this.teamGson = new TeamGson(plugin);
        load();
    }

    public void load() {
        teams.clear();
        leaders.clear();

        JsonObject root = teamGson.load();
        if (root == null) return;

        JsonObject teamsObj = root.getAsJsonObject("teams");
        for (String teamName : teamsObj.keySet()) {
            JsonArray array = teamsObj.getAsJsonArray(teamName);

            Set<UUID> members = new HashSet<>();
            for (JsonElement e : array) {
                members.add(UUID.fromString(e.getAsString()));
            }

            teams.put(teamName, members);
            leaders.put(teamName, members.iterator().next());
        }
    }

    public void save() {
        JsonObject root = new JsonObject();
        JsonObject teamsObj = new JsonObject();

        for (String team : teams.keySet()) {
            JsonArray array = new JsonArray();
            for (UUID uuid : teams.get(team)) {
                array.add(uuid.toString());
            }
            teamsObj.add(team, array);
        }

        root.add("teams", teamsObj);
        teamGson.save(root);
    }

    // =======================
    // 팀 로직
    // =======================

    public boolean createTeam(Player player, String name) {
        if (teams.containsKey(name)) return false;

        Set<UUID> set = new HashSet<>();
        set.add(player.getUniqueId());

        teams.put(name, set);
        leaders.put(name, player.getUniqueId());
        save();
        return true;
    }

    public String getTeam(Player player) {
        for (String team : teams.keySet()) {
            if (teams.get(team).contains(player.getUniqueId())) {
                return team;
            }
        }
        return "방랑자";
    }

    public boolean isLeader(Player player) {
        String team = getTeam(player);
        if (team.equals("방랑자")) return false;
        return leaders.get(team).equals(player.getUniqueId());
    }

    public boolean sameTeam(Player a, Player b) {
        return getTeam(a).equals(getTeam(b)) && !getTeam(a).equals("방랑자");
    }
}
