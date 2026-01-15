package com.nationwar;

import com.nationwar.command.*;
import com.nationwar.core.CoreMain;
import com.nationwar.listeners.*;
import com.nationwar.team.TeamInviteManager;
import com.nationwar.team.TeamMain;
import com.nationwar.tpa.TpaMain;
import com.nationwar.menu.GUIManager; // 추가
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NationWar extends JavaPlugin {
    private static NationWar instance;
    private TeamMain teamMain;
    private CoreMain coreMain;
    private TpaMain tpaMain;
    private GUIManager guiManager; // 추가
    private TeamInviteManager teamInviteManager;

    @Override
    public void onEnable() {
        instance = this;

        // 1. 데이터 폴더 및 매니저 객체들 먼저 생성 (순서 중요!)
        if (!getDataFolder().exists()) getDataFolder().mkdirs();

        // 리스너가 참조하는 매니저들을 최우선으로 초기화
        this.teamInviteManager = new TeamInviteManager();
        this.teamMain = new TeamMain(this);
        this.coreMain = new CoreMain(this);
        this.tpaMain = new TpaMain(this);
        this.guiManager = new GUIManager(this);

        // 2. 명령어 등록
        getCommand("메뉴").setExecutor(new MenuCommand(this));
        getCommand("gamestart").setExecutor(new GamestartCommand(this));
        getCommand("팀").setExecutor(new TeamCommand(this));
        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("국가창고").setExecutor(new TeamChestCommand(this));
        getCommand("gamecontinue").setExecutor(new GameContinueCommand(this));

        // 3. 리스너 등록
        getServer().getPluginManager().registerEvents(new JoinListener(this), this); // 분리된 리스너 등록
        getServer().getPluginManager().registerEvents(new MenuClickListener(this), this); // 여기서 null 참조 방지됨
        getServer().getPluginManager().registerEvents(new BlockProtection(), this);
        getServer().getPluginManager().registerEvents(new CoreDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PvpListener(this), this);

        coreMain.startTimeChecker();

        new PlayerDistanceDetect(this).runTaskTimer(this, 0L, 20L);

        // 팀장 활동량 체크 (서버가 켜진 후 5초 뒤 실행)
        Bukkit.getScheduler().runTaskLater(this, () -> {
            this.teamMain.checkLeaderActivity();
        }, 100L);

    }

    @Override
    public void onDisable() {
        // 서버 종료 시 모든 코어 가스트 제거
        if (this.coreMain != null) {
            this.coreMain.removeAllCoreGhasts();
        }

        // 팀 데이터 안전하게 저장
        if (this.teamMain != null) {
            this.teamMain.saveTeams();
        }

        getLogger().info("NationWar 플러그인이 안전하게 종료되었습니다.");
    }

    public static NationWar getInstance() { return instance; }
    public TeamMain getTeamMain() { return teamMain; }
    public CoreMain getCoreMain() { return coreMain; }
    public TpaMain getTpaMain() { return tpaMain; }
    public GUIManager getGUIManager() { return guiManager; }// 추가됨
    public TeamInviteManager getTeamInviteManager() { return teamInviteManager; }
}