package com.nationwar.team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class TeamMain {

    private static final Map<String, TeamMain> teams = new HashMap<>();
    private static final Map<UUID, TeamMain> playerTeamMap = new HashMap<>();

    private final String name;
    private ChatColor color;
    private UUID leader;
    private final Set<UUID> members = new HashSet<>();
    Inventory teamChest;
    private final Set<Integer> ownedCores = new HashSet<>();

    /* ================= 방랑자 ================= */

    private static TeamMain wandererTeam;

    public static void initWandererTeam() {
        wandererTeam = new TeamMain("방랑자", ChatColor.GRAY, null);
        teams.put("방랑자", wandererTeam);
    }

    public static TeamMain getWandererTeam() {
        return wandererTeam;
    }

    /* ================= 생성 ================= */

    public TeamMain(String name, ChatColor color, UUID leader) {
        this.name = name;
        this.color = color;
        this.leader = leader;

        if (leader != null) {
            members.add(leader);
            playerTeamMap.put(leader, this);
        }

        this.teamChest = Bukkit.createInventory(null, 54, color + name + " 국가 창고");
        teams.put(name, this);
    }

    /* ================= 팀 생성 ================= */

    public static boolean createTeam(Player creator, String teamName) {
        if (teams.containsKey(teamName)) {
            creator.sendMessage("이미 존재하는 팀 이름입니다.");
            return false;
        }

        if (getPlayerTeam(creator) != wandererTeam) {
            creator.sendMessage("이미 팀에 소속되어 있습니다.");
            return false;
        }

        new TeamMain(teamName, ChatColor.WHITE, creator.getUniqueId());
        creator.sendMessage("팀이 생성되었습니다.");
        return true;
    }

    /* ================= 팀 조회 ================= */

    public static TeamMain getPlayerTeam(Player player) {
        return playerTeamMap.getOrDefault(player.getUniqueId(), wandererTeam);
    }

    public static Collection<TeamMain> getAllTeams() {
        return teams.values();
    }

    /* ================= 초대 ================= */

    public boolean invite(Player inviter, Player target) {

        if (!isLeader(inviter)) {
            inviter.sendMessage("팀장만 초대할 수 있습니다.");
            return false;
        }

        if (target == null) {
            inviter.sendMessage("플레이어를 찾을 수 없습니다.");
            return false;
        }

        TeamMain targetTeam = getPlayerTeam(target);

        if (targetTeam == this) {
            inviter.sendMessage("이미 같은 팀에 소속되어 있습니다.");
            return false;
        }

        if (targetTeam != wandererTeam) {
            targetTeam.members.remove(target.getUniqueId());
        }

        setPlayerTeam(target, this);

        target.sendMessage(color + name + " 팀에 초대되었습니다.");
        inviter.sendMessage("플레이어를 팀에 초대하였습니다.");
        return true;
    }

    /* ================= 추방 ================= */

    public boolean kick(Player kicker, Player target) {

        if (!isLeader(kicker)) {
            kicker.sendMessage("팀장만 추방할 수 있습니다.");
            return false;
        }

        if (target == null) {
            kicker.sendMessage("플레이어를 찾을 수 없습니다.");
            return false;
        }

        if (!members.contains(target.getUniqueId())) {
            kicker.sendMessage("해당 플레이어는 팀원이 아닙니다.");
            return false;
        }

        if (leader.equals(target.getUniqueId())) {
            kicker.sendMessage("팀장은 추방할 수 없습니다.");
            return false;
        }

        members.remove(target.getUniqueId());
        setPlayerTeam(target, wandererTeam);

        target.sendMessage("팀에서 추방되었습니다.");
        kicker.sendMessage("플레이어를 팀에서 추방하였습니다.");
        return true;
    }

    /* ================= 팀 삭제 ================= */

    public void delete() {
        for (UUID uuid : new HashSet<>(members)) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                setPlayerTeam(p, wandererTeam);
            }
        }
        ownedCores.clear();
        teams.remove(name);
    }

    /* ================= 내부 ================= */

    private static void setPlayerTeam(Player player, TeamMain team) {
        TeamMain old = playerTeamMap.get(player.getUniqueId());
        if (old != null) {
            old.members.remove(player.getUniqueId());
        }
        playerTeamMap.put(player.getUniqueId(), team);
        team.members.add(player.getUniqueId());
    }

    private boolean isLeader(Player player) {
        return leader != null && leader.equals(player.getUniqueId());
    }

    /* ================= Getter ================= */

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return color + name;
    }

    public ChatColor getColor() {
        return color;
    }

    public Inventory getTeamChest() {
        return teamChest;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public boolean ownsCore(int id) {
        return ownedCores.contains(id);
    }
}
