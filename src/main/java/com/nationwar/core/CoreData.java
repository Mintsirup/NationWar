package com.nationwar.core;

public class CoreData {

    public String owner;
    public double hp;
    public int x, y, z;
    public int id;

    public CoreData(int id, int x, int y, int z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.owner = "없음";
        this.hp = 5000.0;
    }
}
