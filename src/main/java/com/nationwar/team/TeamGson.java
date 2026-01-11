package com.nationwar.team;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.nationwar.NationWar;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TeamGson {

    private final File file;
    private final Gson gson;

    public TeamGson(NationWar plugin) {
        this.file = new File(plugin.getDataFolder(), "team.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public JsonObject load() {
        try {
            if (!file.exists()) {
                save(createDefault());
            }
            return gson.fromJson(new FileReader(file), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(JsonObject object) {
        try {
            FileWriter writer = new FileWriter(file);
            gson.toJson(object, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JsonObject createDefault() {
        JsonObject root = new JsonObject();
        root.add("teams", new JsonObject());
        return root;
    }
}
