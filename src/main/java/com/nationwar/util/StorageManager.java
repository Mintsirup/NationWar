package com.nationwar.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nationwar.NationWar;
import com.nationwar.team.Team;
import com.nationwar.team.TeamManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Gson 기반의 저장관리자.
 * teams.json, cores.json 등을 관리합니다.
 */
public class StorageManager {

    private final NationWar plugin;
    private final TeamManager teamManager;
    private final Gson gson;
    private final File teamsFile;
    private final File coresFile;

    public StorageManager(NationWar plugin, TeamManager teamManager) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.teamsFile = new File(plugin.getDataFolder(), "teams.json");
        this.coresFile = new File(plugin.getDataFolder(), "cores.json");
    }

    public void loadAll() {
        loadTeams();
        // TODO: load cores and other data
    }

    public void saveAll() {
        saveTeams();
        // TODO: save cores and other data
    }

    private void loadTeams() {
        if (!teamsFile.exists()) {
            plugin.getLogger().info("teams.json not found, creating default set");
            // Create default '방랑자' team
            teamManager.createWandererIfAbsent();
            saveTeams();
            return;
        }

        try (FileReader fr = new FileReader(teamsFile)) {
            Type listType = new TypeToken<List<Team>>() {}.getType();
            List<Team> list = gson.fromJson(fr, listType);
            if (list == null) list = new ArrayList<>();
            teamManager.loadFromList(list);
            plugin.getLogger().info("teams.json loaded (" + list.size() + " teams)");
        } catch (IOException e) {
            plugin.getLogger().severe("teams.json 읽기 실패: " + e.getMessage());
        }
    }

    private void saveTeams() {
        try {
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
            List<Team> list = teamManager.getAllTeams();
            try (FileWriter fw = new FileWriter(teamsFile)) {
                gson.toJson(list, fw);
            }
        } catch (IOException e) {
            plugin.getLogger().severe("teams.json 쓰기 실패: " + e.getMessage());
        }
    }
}
