package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

public class TeamCommand implements CommandExecutor {
    private final NationWar plugin;
    public TeamCommand(NationWar plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;

        if (args.length > 0 && args[0].equals("수락")) {
            UUID uuid = p.getUniqueId();
            String invitedTeam = plugin.getTeamInviteManager().getInvite(uuid);

            if (invitedTeam == null) {
                p.sendMessage("§c받은 초대장이 없거나 만료되었습니다.");
                return true;
            }

            // 1. 가입 처리 (리스트 존재 여부 확인 추가)
            if (plugin.getTeamMain().getData().teams.containsKey(invitedTeam)) {
                plugin.getTeamMain().getData().teams.get(invitedTeam).add(uuid.toString());
                plugin.getTeamMain().saveTeams();
            }

            // 2. 팀장에게 즉시 텔레포트
            String leaderUUIDStr = plugin.getTeamMain().getData().leaders.get(invitedTeam);
            if (leaderUUIDStr != null) {
                Player leader = Bukkit.getPlayer(UUID.fromString(leaderUUIDStr));
                if (leader != null && leader.isOnline()) {
                    p.teleport(leader.getLocation());
                    p.sendMessage("§e[!] §a팀에 합류하여 팀장 " + leader.getName() + " 님에게 이동되었습니다.");
                }
            }

            p.sendMessage("§a§l" + invitedTeam + " §f팀의 일원이 되신 것을 환영합니다!");
            plugin.getTeamMain().updateDisplay(p);

            // 3. 마지막에 초대장 삭제
            plugin.getTeamInviteManager().removeInvite(uuid);
            return true;
        }

        // --- 2. 초대 거절 로직 ---
        if (args.length > 0 && args[0].equals("거절")) {
            String invitedTeam = plugin.getTeamInviteManager().getInvite(p.getUniqueId());
            if (invitedTeam == null) {
                p.sendMessage("§c거절할 초대장이 없습니다.");
                return true;
            }

            plugin.getTeamInviteManager().removeInvite(p.getUniqueId());
            p.sendMessage("§c팀 초대를 거절했습니다.");
            return true;
        }

        // --- 3. 기본 메뉴 열기 ---
        if (args.length == 0) {
            plugin.getGUIManager().openTeamMenu(p);
            return true;
        }

        // --- 4. 팀 생성 로직 ---
        String teamName = args[0];
        if (!plugin.getTeamMain().getPlayerTeam(p.getUniqueId()).equals("방랑자")) {
            p.sendMessage("§c이미 팀에 소속되어 있습니다.");
            return true;
        }

        // 기준서: /팀 <이름> 명령어로 누구나 생성 가능, 생성자가 팀장이 됨
        plugin.getTeamMain().createTeam(teamName, p);
        p.sendMessage("§a팀 '" + teamName + "'이 생성되었습니다. 당신은 팀장입니다.");
        return true;
    }
}