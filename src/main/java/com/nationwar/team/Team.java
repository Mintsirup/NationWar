package com.nationwar.team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Team model for NationWar
 */
public class Team {
    private String name;
    private String leader; // UUID string
    private List<String> members = new ArrayList<>(); // UUID strings
    private String color = "WHITE"; // optional

    public Team() {}

    public Team(String name, UUID leaderUuid) {
        this.name = name;
        this.leader = leaderUuid.toString();
        this.members = new ArrayList<>();
        this.members.add(leaderUuid.toString());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getLeaderUuid() {
        return UUID.fromString(leader);
    }

    public void setLeaderUuid(UUID leaderUuid) {
        this.leader = leaderUuid.toString();
    }

    public List<UUID> getMembersUuid() {
        List<UUID> out = new ArrayList<>();
        for (String s : members) out.add(UUID.fromString(s));
        return out;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void addMember(UUID uuid) {
        String s = uuid.toString();
        if (!members.contains(s)) members.add(s);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid.toString());
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid.toString());
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
