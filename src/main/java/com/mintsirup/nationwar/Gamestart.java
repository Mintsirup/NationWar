package com.mintsirup.nationwar;

import com.mintsirup.nationwar.core.Core;
import com.mintsirup.nationwar.core.CoreManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class Gamestart {
    private final Plugin plugin;
    private final CoreManager coreManager;

    public Gamestart(Plugin plugin, CoreManager coreManager) {
        this.plugin = plugin;
        this.coreManager = coreManager;
    }

    public void startGame() {
        spawnCoresFromConfig();
        safeTeleportPlayers();
    }

    private void spawnCoresFromConfig() {
        // Example: read core spawns from config under "core-spawns"
        List<?> spawns = plugin.getConfig().getList("core-spawns");
        if (spawns == null) return;
        for (Object o : spawns) {
            if (!(o instanceof String)) continue;
            String s = (String) o; // format: world:x:y:z
            String[] parts = s.split(":");
            if (parts.length < 4) continue;
            World w = plugin.getServer().getWorld(parts[0]);
            if (w == null) continue;
            try {
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);
                Location loc = new Location(w, x, y, z);
                Core core = new Core(loc, 100);
                coreManager.addCore(core);
            } catch (NumberFormatException ignored) {}
        }
    }

    private void safeTeleportPlayers() {
        // Teleport players to configured spawn or each core's location's highest block
        Location configured = null;
        if (plugin.getConfig().contains("lobby-spawn")) {
            String s = plugin.getConfig().getString("lobby-spawn");
            if (s != null) {
                String[] parts = s.split(":");
                if (parts.length >= 4) {
                    World w = plugin.getServer().getWorld(parts[0]);
                    if (w != null) {
                        try {
                            double x = Double.parseDouble(parts[1]);
                            double y = Double.parseDouble(parts[2]);
                            double z = Double.parseDouble(parts[3]);
                            configured = new Location(w, x, y, z);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
        }

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            Location target = configured;
            if (target == null) {
                // fallback: teleport to first core highest block if available
                List<Core> cores = coreManager.getAllCores();
                if (!cores.isEmpty()) {
                    Location coreLoc = cores.get(0).getLocation();
                    target = coreLoc.getWorld().getHighestBlockAt(coreLoc).getLocation().add(0,1,0);
                } else {
                    target = p.getWorld().getSpawnLocation();
                }
            } else {
                target = target.getWorld().getHighestBlockAt(target).getLocation().add(0,1,0);
            }

            p.teleport(target, TeleportCause.PLUGIN);
        }
    }
}
