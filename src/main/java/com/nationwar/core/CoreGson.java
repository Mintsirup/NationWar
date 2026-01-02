package com.nationwar.core;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.*;
import java.util.Map;

public class CoreGson {

    public static void save() {
        File folder = Bukkit.getPluginManager().getPlugin("NationWar").getDataFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, "cores.json");

        JsonObject root = new JsonObject();
        JsonArray arr = new JsonArray();

        for (Map.Entry<Integer, Location> e : CoreMain.getCoreLocations().entrySet()) {
            int id = e.getKey();
            Location loc = e.getValue();

            JsonObject obj = new JsonObject();
            obj.addProperty("id", id);
            obj.addProperty("world", loc.getWorld().getName());
            obj.addProperty("x", loc.getX());
            obj.addProperty("y", loc.getY());
            obj.addProperty("z", loc.getZ());
            obj.addProperty("owner", CoreMain.getCoreOwner(id));
            obj.addProperty("health", CoreMain.getCoreHealth(id));

            arr.add(obj);
        }

        root.add("cores", arr);

        try (Writer w = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(root, w);
        } catch (Exception ignored) {
        }
    }

    public static void load() {
        File folder = Bukkit.getPluginManager().getPlugin("NationWar").getDataFolder();
        File file = new File(folder, "cores.json");

        if (!file.exists()) {
            return;
        }

        try (Reader r = new FileReader(file)) {
            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
            JsonArray arr = root.getAsJsonArray("cores");

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();

                int id = obj.get("id").getAsInt();
                String worldName = obj.get("world").getAsString();
                World world = Bukkit.getWorld(worldName);
                double x = obj.get("x").getAsDouble();
                double y = obj.get("y").getAsDouble();
                double z = obj.get("z").getAsDouble();

                String owner = obj.get("owner").getAsString();
                int health = obj.get("health").getAsInt();

                Location loc = new Location(world, x, y, z);

                CoreMain.getCoreLocations().put(id, loc);
                CoreMain.getCoreOwners().put(id, owner);
                CoreMain.getCoreHealthMap().put(id, health);
            }
        } catch (Exception ignored) {
        }
    }
}
