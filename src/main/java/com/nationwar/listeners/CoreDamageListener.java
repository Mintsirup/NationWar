package com.nationwar.listeners;

import com.nationwar.core.CoreGson;
import com.nationwar.core.CoreMain;
import com.nationwar.team.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class CoreDamageListener implements Listener {

    @EventHandler
    public void onCoreDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Ghast)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        Ghast ghast = (Ghast) event.getEntity();
        String teamName = TeamMain.getPlayerTeam(player);

        // 1. 점령 시간 확인 (월,수,금 19-20시)
        if (!CoreMain.isCaptureTime()) {
            player.sendMessage("§c[!] 지금은 코어 점령 시간이 아닙니다!");
            event.setCancelled(true);
            return;
        }

        // 2. 방랑자 확인
        if (teamName.equals("방랑자")) {
            player.sendMessage("§c[!] 방랑자는 코어를 점령할 수 없습니다.");
            event.setCancelled(true);
            return;
        }

        // 3. 데미지 계산 및 체력 차감
        // 가스트의 이름을 통해 ID 추출 (예: "§6코어 0" -> "0")
        String name = ghast.getCustomName();
        if (name == null) return;
        int coreId = Integer.parseInt(name.replaceAll("[^0-9]", ""));
        CoreGson.CoreData core = CoreGson.getCore(coreId);

        if (core != null) {
            core.hp -= event.getFinalDamage();

            // 4. 코어 파괴 (체력 0 이하)
            if (core.hp <= 0) {
                core.owner = teamName;
                core.hp = 5000; // 체력 초기화
                Bukkit.broadcastMessage("§6§l[!] §e" + teamName + "§f 팀이 §6코어 " + coreId + "§f를 점령했습니다!");

                CoreGson.saveCores(); // 데이터 저장
                checkWinner(); // 승리 조건 체크
            }
        }
    }

    // 모든 코어를 한 팀이 먹었는지 확인
    private void checkWinner() {
        String firstOwner = CoreGson.getCores().get(0).owner;
        if (firstOwner.equals("없음")) return;

        for (CoreGson.CoreData core : CoreGson.getCores()) {
            if (!core.owner.equals(firstOwner)) return; // 하나라도 주인이 다르면 종료
        }

        // 승리 처리
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle("§6§l" + firstOwner + " 팀 승리!", "§f모든 코어를 점령하였습니다.", 10, 100, 20);
            spawnFirework(p);
        }
    }

    private void spawnFirework(Player p) {
        Firework fw = p.getWorld().spawn(p.getLocation(), Firework.class);
        FireworkMeta meta = fw.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).build());
        meta.setPower(1);
        fw.setFireworkMeta(meta);
    }
}