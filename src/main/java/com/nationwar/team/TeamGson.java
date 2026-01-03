package com.nationwar.team;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.Set;

public class TeamGson {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File file = new File("plugins/NationWar/team.json");

    private static class TeamData {
        Map<String, String> playerTeam;
        Map<String, Set<String>> teams;
        Map<String, String> leader;
        Map<String, String> color;
    }

    public static void save() {
        try {
            TeamData data = new TeamData();
            data.playerTeam = TeamMain.getPlayerTeamMap();
            data.teams = TeamMain.getTeams();
            data.leader = TeamMain.getTeamLeader();
            data.color = TeamMain.getTeamColorMap();

            FileWriter writer = new FileWriter(file);
            gson.toJson(data, writer);
            writer.close();
        } catch (Exception ignored) {}
    }

    public static void load() {
        try {
            if (!file.exists()) return;

            FileReader reader = new FileReader(file);
            TeamData data = gson.fromJson(reader, TeamData.class);
            reader.close();

            TeamMain.load(
                    data.playerTeam,
                    data.teams,
                    data.leader,
                    data.color
            );
        } catch (Exception ignored) {}
    }
}
