package com.nationwar.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nationwar.NationWar;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreGson {

    private final File file;
    private final Gson gson;

    public CoreGson(NationWar plugin) {
        this.file = new File(plugin.getDataFolder(), "core.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public List<CoreData> load() {
        try {
            if (!file.exists()) {
                return new ArrayList<>();
            }
            FileReader reader = new FileReader(file);
            Type type = new TypeToken<Map<String, List<CoreData>>>(){}.getType();
            Map<String, List<CoreData>> data = gson.fromJson(reader, type);
            reader.close();

            if (data == null || !data.containsKey("cores")) {
                return new ArrayList<>();
            }
            return data.get("cores");
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void save(List<CoreData> cores) {
        try {
            Map<String, Object> wrapper = new HashMap<>();
            wrapper.put("cores", cores);

            FileWriter writer = new FileWriter(file);
            gson.toJson(wrapper, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
