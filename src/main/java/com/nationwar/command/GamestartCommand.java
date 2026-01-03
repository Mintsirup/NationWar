package com.nationwar.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GamestartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // TODO 콘솔 전용 체크
        // TODO 월드보더 설정
        // TODO 플레이어 랜덤 TP
        // TODO 코어 생성 호출
        // TODO broadcast title 출력

        return true;
    }
}
