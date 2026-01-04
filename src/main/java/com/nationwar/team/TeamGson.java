package com.nationwar.team;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nationwar.NationWar;
import java.io.*;
import java.util.Map;

public class TeamGson {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File file = new File(NationWar.getInstance().getDataFolder(), "teams.json");

    public void save(Map<String, Object> data) {
        if (!NationWar.getInstance().getDataFolder().exists()) NationWar.getInstance().getDataFolder().mkdirs();
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException ignored) {}
    }

    public Map<String, Object> load() {
        if (!file.exists()) return null;
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, Map.class);
        } catch (IOException ignored) {
            return null;
        }
    }
}