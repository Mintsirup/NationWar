package com.nationwar.listeners;

import com.nationwar.NationWar;
import com.nationwar.core.CoreGson;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class CoreDamageListener implements Listener {
    private final NationWar plugin;

    public CoreDamageListener(NationWar plugin) { this.plugin = plugin; }

    @EventHandler
    public void onGeneralDamage(EntityDamageEvent event) {
        // 코어 가스트인지 확인
        if (!(event.getEntity() instanceof Ghast) || !event.getEntity().hasMetadata("core_id")) return;

        EntityDamageEvent.DamageCause cause = event.getCause();

        // TNT, 크리퍼, 침대/앵커 폭발 등 모든 폭발 데미지 차단
        if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION ||
                cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            event.setCancelled(true);
            return;
        }

        // 불, 용암, 낙하 데미지 차단
        if (cause == EntityDamageEvent.DamageCause.FIRE ||
                cause == EntityDamageEvent.DamageCause.FIRE_TICK ||
                cause == EntityDamageEvent.DamageCause.LAVA ||
                cause == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // 1. 코어 가스트인지 확인
        if (!(event.getEntity() instanceof Ghast) || !event.getEntity().hasMetadata("core_id")) {
            return;
        }

        // 2. 공격자가 플레이어인지 확인 (TNT는 플레이어가 아니므로 여기서도 차단됨)
        if (!(event.getDamager() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        // 3. 점령 시간 확인 (기준서 로직)
        if (!plugin.getCoreMain().isCaptureTime()) {
            event.setCancelled(true);
            return;
        }

        // 4. 게임 시작 여부 확인 (보호막)
        if (!plugin.getCoreMain().isGameStarted()) {
            event.setCancelled(true);
            Player p = (Player) event.getDamager();
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c§l[!] 아직 점령 시간이 아닙니다! (코어 보호막 작동 중)"));
            p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.5f, 1.2f);
            p.spawnParticle(Particle.SMOKE, event.getEntity().getLocation(), 20, 0.5, 0.5, 0.5, 0.05);
            return;
        }

        Player damager = (Player) event.getDamager();
        String teamName = plugin.getTeamMain().getPlayerTeam(damager.getUniqueId());

        // 5. 방랑자 점령 불가
        if (teamName.equals("방랑자")) {
            event.setCancelled(true);
            damager.sendMessage("§c방랑자는 코어를 공격할 수 없습니다.");
            return;
        }

        Ghast ghast = (Ghast) event.getEntity();
        String name = ghast.getCustomName();
        if (name == null || !name.contains("코어")) return;

        // ID 추출 및 데미지 계산
        int coreId = Integer.parseInt(name.replaceAll("[^0-9]", ""));
        CoreGson.CoreInfo core = plugin.getCoreMain().getCoreData().cores.get(coreId);

        core.hp -= event.getFinalDamage();

        // 액션바 표시 (중복된 코드 하나로 정리)
        damager.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new TextComponent("§e[CORE " + coreId + "] §f남은 체력: §c" + (int)core.hp + " §7/ 5000"));

        if (core.hp <= 0) {
            core.owner = teamName;
            core.hp = 5000.0;

            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage("§6§l[!] 코어 점령 알림");
            Bukkit.broadcastMessage("§e" + teamName + " §f팀이 §6" + coreId + "번 코어§f를 완전히 점령했습니다!");
            Bukkit.broadcastMessage(" ");

            for (Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(online.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
                if (plugin.getTeamMain().getPlayerTeam(online.getUniqueId()).equals(teamName)) {
                    online.sendTitle("§6§lCORE CAPTURED", "§f우리 팀이 " + coreId + "번 코어를 점령했습니다!", 10, 40, 10);
                }
            }
            plugin.getCoreMain().saveCores();
            // checkWinner로 수정 (메서드 명 확인)
            this.checkWinner(teamName);
        }
    }

    private void checkWinner(String potentialWinner) {
        if (potentialWinner.equals("없음") || potentialWinner.equals("방랑자")) return;

        int ownedCount = 0;
        for (CoreGson.CoreInfo core : plugin.getCoreMain().getCoreData().cores) {
            if (potentialWinner.equals(core.owner)) {
                ownedCount++;
            }
        }

        // 기준서: 6개의 코어를 모두 점령했을 때
        if (ownedCount >= 6) {
            announceVictory(potentialWinner);
        }
    }

    private void spawnVictoryFireworks(Location loc) {
        // 폭죽 객체 생성
        Firework fw = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();

        // 폭죽 효과 설정 (금색과 흰색이 섞인 화려한 폭죽)
        FireworkEffect effect = FireworkEffect.builder()
                .withColor(org.bukkit.Color.YELLOW) // 금색
                .withFade(org.bukkit.Color.WHITE)  // 흰색으로 사라짐
                .with(FireworkEffect.Type.BALL_LARGE) // 큰 구체 형태
                .trail(true)  // 잔상 효과
                .flicker(true) // 반짝임 효과
                .build();

        fwm.addEffect(effect);
        fwm.setPower(1); // 날아가는 높이 (1이면 적당히 낮게 터짐)
        fw.setFireworkMeta(fwm);
    }

    private void announceVictory(String winnerTeam) {
        // 1. 전 서버 타이틀 및 공지
        String title = "§6§lVICTORY";
        String subtitle = "§e" + winnerTeam + " §f팀이 모든 코어를 점령했습니다!";

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.sendTitle(title, subtitle, 20, 100, 20);
            online.sendMessage("§f-----------------------------------");
            online.sendMessage("§e§l[!] §f국가전쟁이 끝났습니다!");
            online.sendMessage("§e최종 우승팀: §f" + winnerTeam);
            online.sendMessage("§f-----------------------------------");

            // 2. 축하 폭죽 소환 (팀 색상에 맞춰 소환 가능)
            spawnVictoryFireworks(online.getLocation());
        }

        // 3. 게임 종료 후 처리 (예: 모든 가스트 제거)
        plugin.getCoreMain().removeAllCoreGhasts();
    }
}