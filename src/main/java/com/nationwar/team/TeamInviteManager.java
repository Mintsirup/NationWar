package com.nationwar.team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamInviteManager {
    // <초대받은유저UUID, 보낸팀이름>
    private final Map<UUID, String> inviteRequests = new HashMap<>();

    public void sendInvite(UUID target, String teamName) {
        inviteRequests.put(target, teamName);
    }

    public String getInvite(UUID target) {
        return inviteRequests.remove(target);
    }
}