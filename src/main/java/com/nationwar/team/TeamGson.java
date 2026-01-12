package com.nationwar.team;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nationwar.NationWar;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamGson {

    private final File file;
    private final Gson gson;

    public TeamGson(NationWar plugin) {
        this.file = new File(plugin.getDataFolder(), "team.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Map<String, List<String>> load() {
        try {
            if (!file.exists()) {
                return new HashMap<>();
            }
            FileReader reader = new FileReader(file);
            Type type = new TypeToken<Map<String, List<String>>>(){}.getType();
            Map<String, List<String>> data = gson.fromJson(reader, type);
            reader.close();
            return data == null ? new HashMap<>() : data;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void save(Map<String, List<String>> teams) {
        try {
            FileWriter writer = new FileWriter(file);
            gson.toJson(teams, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
