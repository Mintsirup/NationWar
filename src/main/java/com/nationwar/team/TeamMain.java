package com.nationwar.team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamMain {

    public static final String WANDERER = "방랑자";

    private static Map<String, String> playerTeamMap;
    private static Map<String, Set<String>> teams;
    private static Map<String, String> teamLeader;
    private static Map<String, String> teamColor;

    // 초대 대상 UUID -> 팀 이름
    private static Map<String, String> invites;

    // 초대한 사람 UUID -> 마지막 초대 시간
    private static Map<String, Long> inviteCooldown;

    // 초대 대상 UUID -> 만료시간
    private static Map<String, Long> inviteExpire;

    // 초대 대상 UUID -> 팀 이름
    private static Map<String, String> inviteTeam;

    public static void init() {
        playerTeamMap = new HashMap<>();
        teams = new HashMap<>();
        teamLeader = new HashMap<>();
        invites = new HashMap<>();
        inviteCooldown = new HashMap<>();

        teams.put(WANDERER, new HashSet<>());
    }

    /* =========================
            팀 기본
       ========================= */

    public static boolean hasTeam(Player player) {
        return playerTeamMap.containsKey(player.getUniqueId().toString());
    }

    public static String getTeam(Player player) {
        return playerTeamMap.getOrDefault(player.getUniqueId().toString(), WANDERER);
    }

    public static boolean teamExists(String teamName) {
        return teams.containsKey(teamName);
    }

    public static void createTeam(String teamName, Player leader) {
        String uuid = leader.getUniqueId().toString();

        teams.put(teamName, new HashSet<>());
        teams.get(teamName).add(uuid);

        teamLeader.put(teamName, uuid);
        playerTeamMap.put(uuid, teamName);

        teamColor.put(teamName, "§f"); // 기본 흰색
        teams.get(WANDERER).remove(uuid);
    }

    public static void joinTeam(String teamName, Player player) {
        String uuid = player.getUniqueId().toString();

        teams.get(teamName).add(uuid);
        playerTeamMap.put(uuid, teamName);
        teams.get(WANDERER).remove(uuid);
    }

    public static void setTeamColor(String teamName, String color) {
        if (!teams.containsKey(teamName)) return;
        teamColor.put(teamName, color);
    }

    public static String getTeamColor(String teamName) {
        return teamColor.getOrDefault(teamName, "§f");
    }

    public static String getColoredTeamName(String teamName) {
        return getTeamColor(teamName) + teamName;
    }

    /* =========================
            초대 시스템
       ========================= */

    public static boolean canInvite(String inviterName) {
        Player inviter = Bukkit.getPlayer(inviterName);
        if (inviter == null) return false;

        long now = System.currentTimeMillis();
        long last = inviteCooldown.getOrDefault(inviter.getUniqueId().toString(), 0L);

        return now - last >= 120000; // 2분
    }

    public static void invite(Player target, String teamName) {
        String uuid = target.getUniqueId().toString();

        inviteTeam.put(uuid, teamName);
        inviteExpire.put(uuid, System.currentTimeMillis() + 60000); // 1분
    }

    public static boolean hasInvite(Player player) {
        String uuid = player.getUniqueId().toString();

        if (!inviteTeam.containsKey(uuid)) return false;

        long expire = inviteExpire.get(uuid);
        if (System.currentTimeMillis() > expire) {
            inviteTeam.remove(uuid);
            inviteExpire.remove(uuid);
            return false;
        }
        return true;
    }

    public static void acceptInvite(Player player) {
        String uuid = player.getUniqueId().toString();

        if (!hasInvite(player)) return;

        String teamName = inviteTeam.get(uuid);
        joinTeam(teamName, player);

        inviteTeam.remove(uuid);
        inviteExpire.remove(uuid);
    }

    public static void denyInvite(Player player) {
        String uuid = player.getUniqueId().toString();
        inviteTeam.remove(uuid);
        inviteExpire.remove(uuid);
    }

    public static void deleteTeam(String teamName) {
        if (!teams.containsKey(teamName)) return;
        if (teamName.equals(WANDERER)) return;

        Set<String> members = teams.get(teamName);

        for (String uuid : members) {
            playerTeamMap.remove(uuid);
            teams.get(WANDERER).add(uuid);
        }

        teams.remove(teamName);
        teamLeader.remove(teamName);
    }

    /* =========================
            저장용 getter
       ========================= */

    public static Map<String, String> getPlayerTeamMap() {
        return playerTeamMap;
    }

    public static Map<String, Set<String>> getTeams() {
        return teams;
    }

    public static Map<String, String> getTeamLeader() {
        return teamLeader;
    }

    public static Map<String, String> getTeamColorMap() {
        return teamColor;
    }

    public static void load(
            Map<String, String> playerTeam,
            Map<String, Set<String>> loadedTeams,
            Map<String, String> loadedLeader,
            Map<String, String> loadedColor
    ) {
        playerTeamMap = playerTeam;
        teams = loadedTeams;
        teamLeader = loadedLeader;
        teamColor = loadedColor;
    }

}
