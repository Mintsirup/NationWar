package com.nationwar.team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamInviteManager {
    private final Map<UUID, String> inviteRequests = new HashMap<>();

    public void sendInvite(UUID target, String teamName) {
        inviteRequests.put(target, teamName);
    }

    // 수정: 단순히 가져오기만 하도록 변경 (삭제는 Command에서 담당)
    public String getInvite(UUID target) {
        return inviteRequests.get(target);
    }

    public void removeInvite(UUID uuid) {
        inviteRequests.remove(uuid);
    }
}