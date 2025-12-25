package com.mintsirup.nationwar.core;

import com.mintsirup.nationwar.NationWar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class CoreListener implements Listener {
    private final NationWar plugin;

    public CoreListener(NationWar plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ArmorStand)) return;

        ArmorStand stand = (ArmorStand) entity;
        if (stand.getCustomName() == null || !stand.getCustomName().startsWith("Core-")) return;

        String idStr = stand.getCustomName().substring(5);
        UUID id;
        try {
            id = UUID.fromString(idStr);
        } catch (IllegalArgumentException ex) {
            return;
        }

        Core core = plugin.getCoreManager().getCore(id);
        if (core == null) return;

        // Only count damage coming from Ghast fireballs
        boolean ghastDamage = false;
        if (event.getDamager() instanceof Fireball) {
            Object shooter = ((Fireball) event.getDamager()).getShooter();
            if (shooter instanceof Ghast) ghastDamage = true;
        }

        if (!ghastDamage) return;

        event.setCancelled(true); // prevent armor stand from being removed by vanilla logic

        int damage = Math.max(1, (int) Math.round(event.getFinalDamage()));
        core.takeDamage(damage);
    }
}
