package com.nationwar.team;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nationwar.NationWar;
import java.io.*;
import java.util.Map;

public class TeamGson {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File file;

    public TeamGson() {
        this.file = new File(NationWar.getInstance().getDataFolder(), "teams.json");
    }

    // 팀 데이터 저장
    public void saveTeams(Map<String, Object> teamData) {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(teamData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 팀 데이터 로드 (필요 시 구현)
    public Map<String, Object> loadTeams() {
        if (!file.exists()) return null;
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}