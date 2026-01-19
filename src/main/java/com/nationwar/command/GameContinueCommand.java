package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameContinueCommand implements CommandExecutor {
    private final NationWar plugin;
    public GameContinueCommand(NationWar plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return false;

        // 1. 데이터를 기반으로 가스트들 재생성
        plugin.getCoreMain().respawnAllCores();

        // 2. [핵심] 게임 시작 상태 강제 활성화 (보호막 해제)
        plugin.getCoreMain().setGameStarted(true);

        // 3. 시각적 알림 (옵션)
        sender.sendMessage("§a§l[!] §f코어 복구 및 보호막 해제가 완료되었습니다.");
        Bukkit.broadcastMessage("§6§l[!] §국가전쟁이 재개됩니다.");

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
        }

        return true;
    }
}