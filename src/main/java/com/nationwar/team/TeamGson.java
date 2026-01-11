package com.nationwar.team;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nationwar.NationWar;
import java.io.*;
import java.util.*;

public class TeamGson {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File file;
    private static TeamDataContainer container = new TeamDataContainer();

    public static class TeamDataContainer {
        // 이 부분이 HashMap이어야 $.teams 경로의 BEGIN_OBJECT 에러가 나지 않습니다.
        public HashMap<String, TeamData> teams = new HashMap<>();
    }

    public static class TeamData {
        public UUID owner;
        public List<UUID> members = new ArrayList<>();
        public String color = "WHITE";
    }

    public static void loadTeams() {
        if (!NationWar.getInstance().getDataFolder().exists()) {
            NationWar.getInstance().getDataFolder().mkdirs();
        }
        file = new File(NationWar.getInstance().getDataFolder(), "team.json");
        if (!file.exists()) {
            saveTeams();
        } else {
            try (Reader reader = new FileReader(file)) {
                // 구조가 바뀌었으므로 파일 내용이 비어있거나 다르면 예외가 발생할 수 있음
                TeamDataContainer loaded = gson.fromJson(reader, TeamDataContainer.class);
                if (loaded != null) {
                    container = loaded;
                }
            } catch (Exception e) {
                // 에러 발생 시 초기화 후 새로 저장하여 파일 구조 강제 갱신
                container = new TeamDataContainer();
                saveTeams();
            }
        }
    }

    public static void saveTeams() {
        if (file == null) file = new File(NationWar.getInstance().getDataFolder(), "team.json");
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(container, writer);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static HashMap<String, TeamData> getTeams() {
        return container.teams;
    }

    public static String getPlayerTeam(UUID uuid) {
        for (String teamName : container.teams.keySet()) {
            if (container.teams.get(teamName).members.contains(uuid)) return teamName;
        }
        return "방랑자";
    }
}