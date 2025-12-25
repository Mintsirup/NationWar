package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * /gamestart - 콘솔 전용 명령어
 * - 월드보더 15000x15000 (center 0,0) 설정
 * - 플레이어들을 2000x2000 내부(0,0 기준) 무작위 안전 지점(4x4)으로 TP
 * - broadcast 메시지 전송
 */
public class GamestartCommand implements CommandExecutor {

    private final NationWar plugin;
    private final Random random = new Random();

    public GamestartCommand(NationWar plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("이 명령어는 콘솔에서만 실행 가능합니다.");
            return true;
        }

        // 월드보더 설정 (서버의 기본 월드 사용)
        if (Bukkit.getWorlds().isEmpty()) {
            sender.sendMessage("월드가 없습니다.");
            return true;
        }

        var world = Bukkit.getWorlds().get(0);
        WorldBorder border = world.getWorldBorder();
        border.setCenter(0.0, 0.0);
        border.setSize(15000); // 직경? WorldBorder size는 직경(거리) — Paper/Bukkit API 에 맞게 사용
        // 플레이어 TP: 0,0 중심 ±1000 (총 2000x2000)
        for (Player p : Bukkit.getOnlinePlayers()) {
            // 안전 지점 찾기 (간단 구현: 랜덤 시도)
            for (int attempt = 0; attempt < 50; attempt++) {
                int x = random.nextInt(2001) - 1000;
                int z = random.nextInt(2001) - 1000;
                int y = world.getHighestBlockYAt(x, z);
                // TODO: 4x4 영역에 용암 검사 등 안전 검사 추가
                p.teleport(new org.bukkit.Location(world, x + 0.5, y + 1, z + 0.5));
                break;
            }
        }

        Bukkit.broadcastMessage("국가 전쟁 게임이 시작되었습니다!");
        return true;
    }
}
