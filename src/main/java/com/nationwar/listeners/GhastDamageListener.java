package com.nationwar.listeners;

import com.nationwar.core.Core;
import com.nationwar.core.CoreManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

public class GhastDamageListener implements Listener {

    private final CoreManager coreManager;

    public GhastDamageListener(CoreManager coreManager) {
        this.coreManager = coreManager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damaged = event.getEntity();
        if (damaged instanceof Ghast) {
            UUID uuid = damaged.getUniqueId();
            Core core = coreManager.getCoreByGhastUuid(uuid);
            if (core != null) {
                int dmg = (int)Math.ceil(event.getFinalDamage());
                coreManager.damageCore(core, dmg);
                event.setCancelled(true); // prevent default Ghast damage behavior
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Fireball) {
            Fireball fb = (Fireball) event.getEntity();
            ProjectileSource shooter = fb.getShooter();
            // Map fireball hit to nearest core within small radius
            if (event.getHitEntity() != null) {
                if (event.getHitEntity() instanceof Ghast) {
                    // Ghast hit; handled by EntityDamageByEntity
                    return;
                }
            }
            if (event.getHitBlock() != null || event.getHitEntity() != null) {
                org.bukkit.Location loc = fb.getLocation();
                Core core = coreManager.getNearestCore(loc, 3.5);
                if (core != null) {
                    int amount = 20; // fireball damage mapped to core
                    coreManager.damageCore(core, amount);
                }
            }
        }
    }
}
