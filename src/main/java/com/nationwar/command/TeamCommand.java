package com.nationwar.command;

import com.nationwar.NationWar;
import com.nationwar.team.Team;
import com.nationwar.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * /팀 command
 * - /팀 <팀이름> : 팀 생성
 * - /팀 invite <팀이름> <플레이어>
 * - /팀 kick <팀이름> <플레이어>
 * - /팀 delete <팀이름>
 */
public class TeamCommand implements CommandExecutor {

    private final NationWar plugin;
    private final TeamManager teamManager;

    public TeamCommand(NationWar plugin) {
        this.plugin = plugin;
        this.teamManager = plugin.getTeamManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("플레이어만 사용 가능합니다.");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage("사용법: /팀 <팀이름> 또는 /팀 <subcommand>");
            return true;
        }

        if (args.length == 1) {
            // create team
            String teamName = args[0];
            if (teamManager.getTeamOf(p.getUniqueId()) != null) {
                p.sendMessage("이미 팀에 속해 있습니다. 팀을 떠난 후 새 팀을 만드세요.");
                return true;
            }
            boolean ok = teamManager.createTeam(teamName, p.getUniqueId());
            if (ok) {
                p.sendMessage("팀 '" + teamName + "'이(가) 생성되었습니다. 당신은 팀장입니다.");
            } else {
                p.sendMessage("같은 이름의 팀이 이미 존재합니다。");
            }
            return true;
        }

        // subcommands
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "invite":
                if (args.length < 3) {
                    p.sendMessage("사용법: /팀 invite <팀이름> <플레이어>");
                    return true;
                }
                return handleInvite(p, args[1], args[2]);
            case "kick":
                if (args.length < 3) {
                    p.sendMessage("사용법: /팀 kick <팀이름> <플레이어>");
                    return true;
                }
                return handleKick(p, args[1], args[2]);
            case "delete":
                if (args.length < 2) {
                    p.sendMessage("사용법: /팀 delete <팀이름>");
                    return true;
                }
                return handleDelete(p, args[1]);
            default:
                p.sendMessage("알 수 없는 하위 명령입니다.");
                return true;
        }
    }

    private boolean handleInvite(Player sender, String teamName, String targetName) {
        Team team = teamManager.getTeamByName(teamName);
        if (team == null) {
            sender.sendMessage("해당 팀이 존재하지 않습니다。");
            return true;
        }
        if (!team.getLeaderUuid().equals(sender.getUniqueId())) {
            sender.sendMessage("팀장만 초대할 수 있습니다。");
            return true;
        }
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("플레이어를 찾을 수 없습니다。");
            return true;
        }
        if (teamManager.getTeamOf(target.getUniqueId()) != null) {
            sender.sendMessage("대상 플레이어는 이미 다른 팀에 속해 있습니다。");
            return true;
        }
        teamManager.addMember(teamName, target.getUniqueId());
        sender.sendMessage(target.getName() + "님을 팀에 초대했습니다。");
        target.sendMessage("당신은 팀 '" + team.getName() + "'에 초대되었습니다。");
        return true;
    }

    private boolean handleKick(Player sender, String teamName, String targetName) {
        Team team = teamManager.getTeamByName(teamName);
        if (team == null) {
            sender.sendMessage("해당 팀이 존재하지 않습니다。");
            return true;
        }
        if (!team.getLeaderUuid().equals(sender.getUniqueId())) {
            sender.sendMessage("팀장만 추방할 수 있습니다。");
            return true;
        }
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("플레이어를 찾을 수 없습니다。");
            return true;
        }
        if (!team.isMember(target.getUniqueId())) {
            sender.sendMessage("대상 플레이어는 해당 팀의 멤버가 아닙니다。");
            return true;
        }
        teamManager.removeMember(teamName, target.getUniqueId());
        sender.sendMessage(target.getName() + "님을 팀에서 추방했습니다。");
        target.sendMessage("당신은 팀 '" + team.getName() + "'에서 추방되었습니다。");
        return true;
    }

    private boolean handleDelete(Player sender, String teamName) {
        Team team = teamManager.getTeamByName(teamName);
        if (team == null) {
            sender.sendMessage("해당 팀이 존재하지 않습니다。");
            return true;
        }
        if (!team.getLeaderUuid().equals(sender.getUniqueId())) {
            sender.sendMessage("팀장만 팀을 ��제할 수 있습니다。");
            return true;
        }
        boolean ok = teamManager.disbandTeam(teamName);
        if (ok) {
            sender.sendMessage("팀이 삭제되었습니다. 팀원은 '방랑자'로 이동됩니다。");
            // move members to wanderer
            Team wanderer = teamManager.getTeamByName("방랑자");
            if (wanderer == null) {
                teamManager.createWandererIfAbsent();
                wanderer = teamManager.getTeamByName("방랑자");
            }
            for (UUID member : team.getMembersUuid()) {
                if (wanderer != null) wanderer.addMember(member);
            }
        } else {
            sender.sendMessage("팀 삭제에 실패했습니다。");
        }
        return true;
    }
}
