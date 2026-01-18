package com.nationwar.team;

import com.nationwar.NationWar;
import com.nationwar.core.CoreGson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import java.util.Collections;

import java.io.File;
import java.util.*;

public class TeamMain {
    private final Set<String> storageLock = new HashSet<>(); // 창고 잠금 목록

    public boolean isStorageLocked(String teamName) { return storageLock.contains(teamName); }
    public void lockStorage(String teamName) { storageLock.add(teamName); }
    public void unlockStorage(String teamName) { storageLock.remove(teamName); }
    private final NationWar plugin;
    private final File teamFile;
    private TeamGson.TeamData data;
    private final TeamChest teamChest;

    public TeamMain(NationWar plugin) {
        this.plugin = plugin;
        this.teamFile = new File(plugin.getDataFolder(), "team.json");
        this.data = TeamGson.load(teamFile);
        this.teamChest = new TeamChest(plugin); // 추가
    }

    public void checkLeaderActivity() {
        long oneWeekMillis = 7L * 24 * 60 * 60 * 1000; // 7일을 밀리초로 계산
        long currentTime = System.currentTimeMillis();
        boolean changed = false;

        for (String teamName : data.leaders.keySet()) {
            String leaderUUIDStr = data.leaders.get(teamName);
            UUID leaderUUID = UUID.fromString(leaderUUIDStr);
            OfflinePlayer leader = Bukkit.getOfflinePlayer(leaderUUID);

            // 마지막 접속 기록 확인 (접속한 적이 없으면 0 반환)
            long lastPlayed = leader.getLastPlayed();

            if (lastPlayed != 0 && (currentTime - lastPlayed) > oneWeekMillis) {
                // 일주일 이상 미접속 시 새로운 팀장 선출
                List<String> members = data.teams.get(teamName);
                if (members.size() > 1) {
                    // 팀장 제외하고 무작위 셔플
                    List<String> candidates = new ArrayList<>(members);
                    candidates.remove(leaderUUIDStr);
                    Collections.shuffle(candidates);

                    String newLeaderUUID = candidates.get(0);
                    data.leaders.put(teamName, newLeaderUUID);

                    Bukkit.getLogger().info("[NationWar] " + teamName + " 팀의 팀장이 미접속으로 인해 변경되었습니다.");
                    changed = true;
                }
            }
        }

        if (changed) {
            saveTeams();
        }
    }

    public void resetAllData() {
        // 모든 유저 방랑자로 변경
        data.teams.clear();
        data.leaders.clear();
        data.colors.clear();
        saveTeams();

        // 코어 소유권 초기화
        for (CoreGson.CoreInfo core : plugin.getCoreMain().getCoreData().cores) {
            core.owner = "없음";
            core.hp = 5000;
        }
        plugin.getCoreMain().saveCores();

        Bukkit.broadcastMessage("§c§l[!] §f모든 팀 데이터와 코어 상태가 초기화되었습니다.");
    }

    public void createTeam(String teamName, Player leader) {

        // 1. 이미 존재하는 팀 이름인지 확인
        if (plugin.getTeamMain().getData().teams.containsKey(teamName)) {
            leader.sendMessage("§c이미 사용 중인 팀 이름입니다. 다른 이름을 선택해주세요.");
            return;
        }

        // 2. 이미 팀에 소속되어 있는지 확인 (기존 로직)
        if (!plugin.getTeamMain().getPlayerTeam(leader.getUniqueId()).equals("방랑자")) {
            leader.sendMessage("§c이미 소속된 팀이 있습니다. 먼저 팀을 탈퇴해야 합니다.");
            return;
        }


        // 기준서: 누구나 생성 가능, 생성자가 팀장이 된다.
        if (data.teams.containsKey(teamName)) return;
        List<String> members = new ArrayList<>();
        members.add(leader.getUniqueId().toString());
        data.teams.put(teamName, members);
        saveTeams();
        leader.sendMessage(" ");
        leader.sendMessage("§6§l[ 시스템 ] §e" + teamName + " §f팀이 창설되었습니다!");
        leader.sendMessage("§f- §e/메뉴§f를 열어 팀원을 초대하거나 팀 색상을 정하세요.");
        leader.sendMessage("§f- 점령 시간(월/수/금 19시)에 코어를 공격하여 점령하세요.");
        leader.sendMessage(" ");
        leader.playSound(leader.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
    }

    public void deleteTeam(String teamName) {
        // 기준서: 팀을 삭제하고 팀원들을 방랑자로 변경
        data.teams.remove(teamName);
        data.colors.remove(teamName);
        saveTeams();
    }

    public void updateDisplay(Player player) {
        // 1. JSON 데이터에서 최신 정보 가져오기
        String teamName = getPlayerTeam(player.getUniqueId());
        String colorStr = data.colors.getOrDefault(teamName, "WHITE");

        org.bukkit.ChatColor color;
        try {
            color = org.bukkit.ChatColor.valueOf(colorStr.toUpperCase());
        } catch (Exception e) {
            color = org.bukkit.ChatColor.WHITE;
        }

        // 2. 표시할 텍스트 생성: [팀이름] 닉네임
        String prefix = color + "[" + teamName + "] §f";
        String fullName = prefix + player.getName();

        // 3. 플레이어 객체에 직접 반영 (탭 리스트 및 채팅 이름)
        player.setDisplayName(fullName);
        player.setPlayerListName(fullName);

        // 4. 머리 위 이름표 색상을 위한 최소한의 스코어보드 처리
        // (마인크래프트 엔진 한계상 머리 위 이름 색상은 스코어보드 팀이 반드시 필요합니다)
        org.bukkit.scoreboard.Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        org.bukkit.scoreboard.Team sTeam = sb.getTeam(teamName);

        if (sTeam == null) {
            sTeam = sb.registerNewTeam(teamName);
        }

        sTeam.setPrefix(prefix);
        sTeam.setColor(color);
        if (!sTeam.hasEntry(player.getName())) {
            sTeam.addEntry(player.getName());
        }
    }

    public void changeColor(String teamName, String colorName) {
        // JSON 데이터 업데이트 및 파일 저장
        data.colors.put(teamName, colorName.toUpperCase());
        saveTeams(); // 이 메서드 내에서 가공된 GSON이 파일로 쓰여집니다.

        // 저장된 JSON 데이터를 바탕으로 모든 온라인 플레이어 화면 갱신
        for (Player p : Bukkit.getOnlinePlayers()) {
            updateDisplay(p);
        }
    }

    public boolean isLeader(String teamName, Player player) {
        // 기준서: 생성자가 팀장 (리스트의 0번째 인덱스)
        List<String> members = data.teams.get(teamName);
        return members != null && members.get(0).equals(player.getUniqueId().toString());
    }

    public String getPlayerTeam(UUID uuid) {
        for (Map.Entry<String, List<String>> entry : data.teams.entrySet()) {
            if (entry.getValue().contains(uuid.toString())) return entry.getKey();
        }
        return "방랑자"; // 기준서: 기본 상태
    }

    public TeamChest getTeamChest() { return teamChest; }
    public TeamGson.TeamData getData() { return data; }

    public boolean sameTeam(Player p1, Player p2) {
        String t1 = getPlayerTeam(p1.getUniqueId());
        String t2 = getPlayerTeam(p2.getUniqueId());
        return !t1.equals("방랑자") && t1.equals(t2);
    }

    public void saveTeams() { TeamGson.save(teamFile, data); }
}