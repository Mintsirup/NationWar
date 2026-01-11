package com.nationwar.core;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class CoreGson {

    private final File file;

    public CoreGson(File dataFolder) {
        this.file = new File(dataFolder, "core.json");
    }

    public List<Location> loadCores() {
        List<Location> cores = new ArrayList<>();

        if (!file.exists()) {
            saveCores(cores);
            return cores;
        }

        try (FileReader reader = new FileReader(file)) {
            JsonElement element = JsonParser.parseReader(reader);
            if (!element.isJsonArray()) return cores;

            JsonArray array = element.getAsJsonArray();
            for (JsonElement e : array) {
                JsonObject obj = e.getAsJsonObject();

                String world = obj.get("world").getAsString();
                double x = obj.get("x").getAsDouble();
                double y = obj.get("y").getAsDouble();
                double z = obj.get("z").getAsDouble();

                cores.add(new Location(
                        Bukkit.getWorld(world),
                        x, y, z
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cores;
    }

    public void saveCores(List<Location> cores) {
        JsonArray array = new JsonArray();

        for (Location loc : cores) {
            JsonObject obj = new JsonObject();
            obj.addProperty("world", loc.getWorld().getName());
            obj.addProperty("x", loc.getX());
            obj.addProperty("y", loc.getY());
            obj.addProperty("z", loc.getZ());
            array.add(obj);
        }

        try (FileWriter writer = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(array, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
