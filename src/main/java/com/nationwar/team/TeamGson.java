package com.nationwar.team;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nationwar.NationWar;
import java.io.*;
import java.util.*;

public class TeamGson {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static TeamDataContainer data = new TeamDataContainer();
    public static Map<String, String> colors = new HashMap<>();

    public static class TeamDataContainer {
        public Map<String, List<String>> teams = new HashMap<>();
    }

    public static void load() {
        File file = new File(NationWar.getInstance().getDataFolder(), "team.json");
        if (file.exists()) {
            try (Reader r = new FileReader(file)) { data = gson.fromJson(r, TeamDataContainer.class); } catch (Exception e) {}
        }
    }

    public static void save() {
        File file = new File(NationWar.getInstance().getDataFolder(), "team.json");
        try (Writer w = new FileWriter(file)) { gson.toJson(data, w); } catch (Exception e) {}
    }

    public static Map<String, List<String>> getTeams() { return data.teams; }
}