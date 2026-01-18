package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TeamChestCommand implements CommandExecutor {
    private final NationWar plugin;
    public TeamChestCommand(NationWar plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        // 1. 플레이어의 팀 확인
        String team = plugin.getTeamMain().getPlayerTeam(p.getUniqueId());

        if (team.equals("방랑자")) {
            p.sendMessage("§c§l[!] §c팀이 없는 방랑자는 국가 창고를 사용할 수 없습니다.");
            return true;
        }

        // 2. TeamChest의 open 메서드를 호출하여 인벤토리를 가져옴
        // 이 메서드 내부에서 이미 [중복 접속 체크 + 잠금 설정 + 로그 기록]이 진행됩니다.
        Inventory inv = plugin.getTeamMain().getTeamChest().open(p, team);

        // 3. 인벤토리가 null이 아니면(정상적으로 열 수 있는 상태면) 열어줌
        if (inv != null) {
            p.openInventory(inv);
            p.sendMessage("§e§l[!] §f국가 창고를 열었습니다. §7(닫을 때 자동 저장)");
        }
        // inv가 null인 경우는 이미 TeamChest.open() 내부에서
        // "다른 팀원이 사용 중"이라는 메시지를 보냈으므로 별도 처리가 필요 없습니다.

        return true;
    }
}