package com.nationwar.command;

import com.nationwar.core.CoreGson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;

public class GameContinueCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CoreGson.load();
        boolean success = true;
        for (CoreGson.CoreData data : CoreGson.getCores()) {
            try {
                Location loc = new Location(Bukkit.getWorlds().get(0), data.x, data.y, data.z);
                Ghast g = (Ghast) loc.getWorld().spawnEntity(loc, EntityType.GHAST);
                g.setAI(false);
                g.setCustomName("코어 " + data.id);
            } catch (Exception e) { success = false; }
        }
        Bukkit.getConsoleSender().sendMessage(success ? "코어 생성 성공" : "코어 생성 실패");
        return true;
    }
}