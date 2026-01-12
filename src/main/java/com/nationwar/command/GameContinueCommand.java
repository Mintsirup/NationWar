package com.nationwar.command;

import com.nationwar.NationWar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameContinueCommand implements CommandExecutor {
    private final NationWar plugin;
    public GameContinueCommand(NationWar plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 기준서: core.json 내용을 불러와 코어 데이터 읽기
        boolean success = plugin.getCoreMain().reloadCoreFromConfig();
        if (success) {
            plugin.getLogger().info("코어 불러오기 성공");
        } else {
            plugin.getLogger().info("코어 불러오기 실패");
        }
        return true;
    }
}