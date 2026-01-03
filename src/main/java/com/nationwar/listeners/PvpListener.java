package com.nationwar.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        // TODO 같은 팀이면 취소
    }
}
