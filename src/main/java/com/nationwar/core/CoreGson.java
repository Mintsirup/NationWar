package com.nationwar.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

public class CoreGson {

    private static final Gson gson = new Gson();
    private static final File file = new File("plugins/NationWar/core.json");

    public static void save(Map<Integer, String> ownerMap) {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(ownerMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, String> load() {
        if (!file.exists()) return null;

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<Integer, String>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
