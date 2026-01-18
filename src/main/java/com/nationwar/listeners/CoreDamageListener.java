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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;
import java.util.UUID;

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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        // 1. 코어 확인
        if (!(event.getEntity() instanceof Ghast) || !event.getEntity().hasMetadata("core_id")) return;

        // 2. 기본적으로 모든 데미지 취소 (보호막)
        event.setCancelled(true);

        // 3. 공격자 확인
        if (!(event.getDamager() instanceof Player)) return;
        Player damager = (Player) event.getDamager();

        // 4. 점령 시간 및 게임 시작 여부 통합 체크
        if (!plugin.getCoreMain().isCaptureTime() || !plugin.getCoreMain().isGameStarted()) {
            damager.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c§l[!] 보호막 작동 중! (점령 시간이 아닙니다)"));
            return;
        }

        // 5. 팀 확인
        String teamName = plugin.getTeamMain().getPlayerTeam(damager.getUniqueId());
        if (teamName.equals("방랑자")) {
            damager.sendMessage("§c방랑자는 코어를 공격할 수 없습니다.");
            return;
        }

        // 6. [핵심] 여기서만 타격 허용 및 HP 차감
        event.setCancelled(false);

        Ghast ghast = (Ghast) event.getEntity();
        int coreId = ghast.getMetadata("core_id").get(0).asInt();
        CoreGson.CoreInfo core = plugin.getCoreMain().getCoreData().cores.get(coreId);

        double damage = event.getFinalDamage();
        core.hp -= damage;

        // 액션바 및 알림 로직 (기존과 동일)
        damager.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new TextComponent("§e[CORE " + coreId + "] §fHP: §c" + (int)core.hp + " §7/ 5000"));

        if (core.hp <= 0) {
            handleCapture(core, coreId, teamName);
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

    private void handleCapture(CoreGson.CoreInfo core, int id, String team) {
        core.owner = team;
        core.hp = 5000.0;
        Bukkit.broadcastMessage("§6§l[!] §e" + team + " §f팀이 §6" + id + "번 코어§f를 점령했습니다!");
        plugin.getCoreMain().saveCores();
        // 승리 판정 로직 호출...
    }

    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        // null 체크를 추가하여 안전하게 보스바 제거
        if (plugin.getDistanceDetect() != null) {
            plugin.getDistanceDetect().removeBossBar(event.getPlayer());
        }
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