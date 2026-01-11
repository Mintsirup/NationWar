package com.nationwar.command;

import com.google.gson.JsonObject;
import com.nationwar.NationWar;
import com.nationwar.core.CoreMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;

public class GameContinueCommand implements CommandExecutor {

    private final NationWar plugin;
    private final CoreMain coreMain;

    public GameContinueCommand(NationWar plugin, CoreMain coreMain) {
        this.plugin = plugin;
        this.coreMain = coreMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // 콘솔 전용
        if (sender != Bukkit.getConsoleSender()) {
            sender.sendMessage("§c이 명령어는 콘솔에서만 실행할 수 있습니다.");
            return true;
        }

        boolean success = true;
        World world = Bukkit.getWorlds().get(0);

        for (JsonObject core : coreMain.getCores().values()) {
            try {
                int x = core.get("x").getAsInt();
                int y = core.get("y").getAsInt();
                int z = core.get("z").getAsInt();

                // 좌표 유효성 검사
                if (y <= 0) {
                    success = false;
                    continue;
                }

                Location loc = new Location(world, x + 0.5, y + 1, z + 0.5);

                Ghast ghast = (Ghast) world.spawnEntity(loc, EntityType.GHAST);
                ghast.setAI(false);
                ghast.setSilent(true);
                ghast.setInvulnerable(false);
                ghast.setCustomNameVisible(false);

            } catch (Exception e) {
                success = false;
                e.printStackTrace();
            }
        }

        if (success) {
            plugin.getLogger().info("[GameContinue] 모든 코어가 정상적으로 불러와졌습니다. (성공)");
        } else {
            plugin.getLogger().warning("[GameContinue] 일부 코어를 불러오는 데 실패했습니다.");
        }

        return true;
    }
}
