package com.nationwar.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nationwar.NationWar;
import java.io.*;
import java.util.Map;

public class CoreGson {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File file;

    public CoreGson() {
        this.file = new File(NationWar.getInstance().getDataFolder(), "cores.json");
    }

    public void saveCores(Map<Integer, CoreMain.CoreData> coreData) {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(coreData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, CoreMain.CoreData> loadCores() {
        if (!file.exists()) return null;
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}