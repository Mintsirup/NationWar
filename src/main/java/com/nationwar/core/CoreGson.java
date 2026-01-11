package com.nationwar.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nationwar.NationWar;
import java.io.*;
import java.util.*;

public class CoreGson {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File file;
    private static CoreContainer container = new CoreContainer();

    public static class CoreContainer {
        public List<CoreData> cores = new ArrayList<>();
    }

    public static class CoreData {
        public String owner = "없음";
        public double hp = 5000.0;
        public double x, y, z;
        public int id;
    }

    public static void load() {
        file = new File(NationWar.getInstance().getDataFolder(), "core.json");
        if (file.exists()) {
            try (Reader r = new FileReader(file)) {
                container = gson.fromJson(r, CoreContainer.class);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public static void save() {
        try (Writer w = new FileWriter(file)) {
            gson.toJson(container, w);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<CoreData> getCores() { return container.cores; }
}