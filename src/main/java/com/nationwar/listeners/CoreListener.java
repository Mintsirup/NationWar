package com.nationwar.listeners;

import com.nationwar.NationWar;
import org.bukkit.event.Listener;

/**
 * 코어 관련 리스너 (근접 감지, 보스바 관리, 가스트 데미지 처리 등)
 * 실제 코어 스폰/가스트 기반 데미지 로직은 이 클래스와 추가 매니저 클래스(CoreManager 등)에 구현합니다.
 */
public class CoreListener implements Listener {

    private final NationWar plugin;

    public CoreListener(NationWar plugin) {
        this.plugin = plugin;
    }

    // TODO: 가스트가 코어를 공격할 때 발생하는 데미지 이벤트 처리,
    // 코어 근접 감지 시 보스바 표시, 접근 알림 등 구현
}
