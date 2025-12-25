package com.nationwar.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nationwar.NationWar;
import org.bukkit.plugin.PluginLogger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 간단한 Gson 기반의 저장관리자.
 * 실제로는 팀 데이터, 코어 데이터, 국가창고 등을 이 클래스로 직렬화/역직렬화합니다.
 * (여기서는 기본 틀만 제공)
 */
public class StorageManager {

    private final NationWar plugin;
    private final Gson gson;
    private final File teamsFile;
    private final File coresFile;

    public StorageManager(NationWar plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.teamsFile = new File(plugin.getDataFolder(), "teams.json");
        this.coresFile = new File(plugin.getDataFolder(), "cores.json");
    }

    public void loadAll() {
        // TODO: teams.json, cores.json 로드 구현
        // 예시: if (teamsFile.exists()) { read and parse }
    }

    public void saveAll() {
        // TODO: 현재 메모리 데이터→JSON 쓰기 구현
        try {
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
            // 예시 더미 파일 생성(실제 구조 구현 시 교체)
            try (FileWriter fw = new FileWriter(teamsFile)) {
                gson.toJson("{}", fw);
            }
            try (FileWriter fw = new FileWriter(coresFile)) {
                gson.toJson("{}", fw);
            }
        } catch (IOException e) {
            plugin.getLogger().severe("저장 중 오류 발생: " + e.getMessage());
        }
    }
}
