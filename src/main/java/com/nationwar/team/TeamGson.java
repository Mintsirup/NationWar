package com.nationwar.team;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.Map;

public class TeamGson {

    public static void save() {
        File folder = Bukkit.getPluginManager().getPlugin("NationWar").getDataFolder();
        if (!folder.exists()) folder.mkdirs();

        File file = new File(folder, "teams.json");

        JsonObject root = new JsonObject();
        JsonArray teams = new JsonArray();
        JsonArray members = new JsonArray();

        for (Map.Entry<String, String> e : TeamMain.getTeamOwnerMap().entrySet()) {
            JsonObject o = new JsonObject();
            o.addProperty("name", e.getKey());
            o.addProperty("owner", e.getValue());
            teams.add(o);
        }

        for (Map.Entry<Player, String> e : TeamMain.getPlayerTeamMap().entrySet()) {
            JsonObject o = new JsonObject();
            o.addProperty("player", e.getKey().getName());
            o.addProperty("team", e.getValue());
            members.add(o);
        }

        root.add("teams", teams);
        root.add("members", members);

        try (Writer w = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(root, w);
        } catch (Exception ignored) {}
    }

    public static void load() {
        File folder = Bukkit.getPluginManager().getPlugin("NationWar").getDataFolder();
        File file = new File(folder, "teams.json");
        if (!file.exists()) return;

        try (Reader r = new FileReader(file)) {
            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();

            JsonArray teams = root.getAsJsonArray("teams");
            for (JsonElement el : teams) {
                JsonObject o = el.getAsJsonObject();
                String name = o.get("name").getAsString();
                String owner = o.get("owner").getAsString();
                TeamMain.getTeamOwnerMap().put(name, owner);
            }

            JsonArray members = root.getAsJsonArray("members");
            for (JsonElement el : members) {
                JsonObject o = el.getAsJsonObject();
                String player = o.get("player").getAsString();
                String team = o.get("team").getAsString();
                Player p = Bukkit.getPlayerExact(player);
                if (p != null) {
                    TeamMain.getPlayerTeamMap().put(p, team);
                }
            }

        } catch (Exception ignored) {}
    }
}
