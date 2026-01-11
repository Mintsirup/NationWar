package com.nationwar.listeners;

import com.google.gson.JsonObject;
import com.nationwar.NationWar;
import com.nationwar.core.CoreMain;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CoreDamageListener implements Listener {

    private final NationWar plugin;
    private final CoreMain coreMain;

    public CoreDamageListener(NationWar plugin, CoreMain coreMain) {
        this.plugin = plugin;
        this.coreMain = coreMain;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        Entity entity = event.getEntity();
        if (!(entity instanceof Ghast)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Ghast ghast = (Ghast) entity;

        JsonObject core = getCoreByLocation(ghast);
        if (core == null) return;

        double hp = core.get("hp").getAsDouble();
        double damage = event.getFinalDamage();

        hp -= damage;
        if (hp < 0) hp = 0;

        core.addProperty("hp", hp);
        coreMain.saveCores();

        // 가스트 실제 체력은 의미 없음 → 즉시 회복
        ghast.setHealth(ghast.getMaxHealth());

        event.setCancelled(true);
    }

    private JsonObject getCoreByLocation(Ghast ghast) {
        for (JsonObject core : coreMain.getCores().values()) {
            int x = core.get("x").getAsInt();
            int y = core.get("y").getAsInt();
            int z = core.get("z").getAsInt();

            if (ghast.getLocation().getBlockX() == x
                    && ghast.getLocation().getBlockY() == y + 1
                    && ghast.getLocation().getBlockZ() == z) {
                return core;
            }
        }
        return null;
    }
}
