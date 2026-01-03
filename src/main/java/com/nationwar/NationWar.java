package com.nationwar;

import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {

    @Override
    public void onEnable() {

        // TODO TeamMain.init()
        // TODO CoreMain.init()
        // TODO TpaMain.init()

        // TODO TeamGson.load()
        // TODO CoreGson.load()

        // TODO 명령어 등록 (/gamestart, /메뉴, /팀, /tpa, /국가창고)
        // TODO 리스너 등록 (InventoryClickListener, PlayerDistanceDetect, PvpListener)
    }

    @Override
    public void onDisable() {
        // TODO TeamGson.save()
        // TODO CoreGson.save()
    }
}
