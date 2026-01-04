package com.nationwar.team;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class TeamGson {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File file;

    /* ================= 초기화 ================= */

    public static void init(File dataFolder) {
        file = new File(dataFolder, "teams.json");
    }

    /* ================= 저장 ================= */

    public static void save() {
        Map<String, TeamData> dataMap = new HashMap<>();

        for (TeamMain team : TeamMain.getAllTeams()) {
            if (team == TeamMain.getWandererTeam()) continue;

            TeamData data = new TeamData();
            data.name = team.getName();
            data.color = team.getColor().name();
            data.leader = team.getLeader();
            data.members = new ArrayList<>(team.getMembers());
            data.cores = new ArrayList<>(team.ownedCores);
            data.chest = team.getTeamChest().getContents();

            dataMap.put(team.getName(), data);
        }

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(dataMap, writer);
        } catch (Exception ignored) {
        }
    }

    /* ================= 로드 ================= */

    public static void load() {
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, TeamData>>() {}.getType();
            Map<String, TeamData> dataMap = gson.fromJson(reader, type);

            if (dataMap == null) return;

            for (TeamData data : dataMap.values()) {
                TeamMain team = new TeamMain(
                        data.name,
                        ChatColor.valueOf(data.color),
                        data.leader
                );

                for (UUID uuid : data.members) {
                    team.members.add(uuid);
                }

                for (int coreId : data.cores) {
                    team.addCore(coreId);
                }

                Inventory inv = Bukkit.createInventory(null, 54, team.getDisplayName() + " 국가 창고");
                inv.setContents(data.chest);
                team.teamChest = inv;
            }

        } catch (Exception ignored) {
        }
    }

    /* ================= 저장용 데이터 ================= */

    private static class TeamData {
        String name;
        String color;
        UUID leader;
        List<UUID> members;
        List<Integer> cores;
        ItemStack[] chest;
    }
}
