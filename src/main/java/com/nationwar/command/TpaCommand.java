package com.nationwar.command;

import com.nationwar.NationWar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * /tpa <player>
 * - 요청 전송, 1분 후 만료, 수락/거절 클릭 지원(Adventure)
 * - 탭완성 기본 구현
 *
 * (상세 상태 저장/쿨타임은 실제 구현 시 추가)
 */
public class TpaCommand implements CommandExecutor, TabCompleter {

    private final NationWar plugin;

    public TpaCommand(NationWar plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("플레이어만 사용 가능합니다.");
            return true;
        }
        if (args.length < 1) {
            p.sendMessage("사용법: /tpa <플레이어>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            p.sendMessage("플레이어를 찾을 수 없습니다.");
            return true;
        }

        // 요청 메시지 (클릭 수락/거절)
        Component accept = Component.text("[수락]").clickEvent(ClickEvent.runCommand("/tpaaccept " + p.getName()));
        Component deny = Component.text("[거절]").clickEvent(ClickEvent.runCommand("/tpadeny " + p.getName()));
        target.sendMessage(Component.text(p.getName() + "이(가) 당신에게 tpa요청을 보냈습니다. 이 요청은 1분 후 만료됩니다. "));
        target.sendMessage(accept.append(Component.text(" ")).append(deny));
        p.sendMessage("tpa 요청을 전송했습니다.");

        // TODO: 요청 저장/만료/쿨타임 로직 구현
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) list.add(p.getName());
            }
        }
        return list;
    }
}
