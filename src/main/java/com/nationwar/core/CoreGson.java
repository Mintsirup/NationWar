package com.nationwar.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nationwar.NationWar;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CoreGson {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File file;
    private static CoreContainer container = new CoreContainer();

    public static class CoreContainer {
        public List<CoreData> cores = new ArrayList<>();
    }

    public static class CoreData {
        public int id;
        public double x, y, z;
        public double hp = 5000;
        public String owner = "없음";
    }

    public static void loadCores() {
        file = new File(NationWar.getInstance().getDataFolder(), "core.json");
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                container = gson.fromJson(reader, CoreContainer.class);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public static void saveCores() {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(container, writer);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<CoreData> getCores() {
        return container.cores;
    }

    // 에러 해결: int ID를 받아 CoreData를 반환하는 올바른 메서드
    public static CoreData getCore(int id) {
        for (CoreData core : container.cores) {
            if (core.id == id) return core;
        }
        return null;
    }
}