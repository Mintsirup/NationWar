package com.nationwar.core;

import java.util.UUID;

public class Core {
    private UUID id;
    private String worldName;
    private int x;
    private int y;
    private int z;
    private int hp;
    private String ghastUuid; // serialized as string

    public Core() {
        // default constructor for Gson
    }

    public Core(UUID id, String worldName, int x, int y, int z, int hp) {
        this.id = id;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.hp = hp;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getWorldName() { return worldName; }
    public void setWorldName(String worldName) { this.worldName = worldName; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getZ() { return z; }
    public void setZ(int z) { this.z = z; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public void damage(int amount) { this.hp = Math.max(0, this.hp - amount); }
    public String getGhastUuid() { return ghastUuid; }
    public void setGhastUuid(String ghastUuid) { this.ghastUuid = ghastUuid; }
}
