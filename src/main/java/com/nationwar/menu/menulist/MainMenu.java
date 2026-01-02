package com.nationwar.menu.menulist;

import com.nationwar.core.CoreMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenu implements Listener {

    private final String MAIN_MENU = "§f메인 메뉴";
    private final String TEAM_MENU = "§f팀 메뉴";
    private final String TEAM_INVITE_LIST = "§f팀 초대 메뉴";
    private final String TEAM_INVITE_CONFIRM = "§f팀 초대 확인";
    private final String TEAM_COLOR_MENU = "§f팀 색 설정 메뉴";
    private final String TEAM_DELETE_CONFIRM = "§f팀 삭제 확인";
    private final String CORE_MENU = "§f코어 메뉴";
    private final String INFO_MENU = "§f정보 메뉴";

    private Player inviteTarget;

    public void openMain(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, MAIN_MENU);
        inv.setItem(20, create(Material.PAPER, "§f팀 메뉴로 이동합니다"));
        inv.setItem(22, create(Material.BEACON, "§f코어 메뉴로 이동합니다"));
        inv.setItem(24, create(Material.BOOK, "§f정보 메뉴로 이동합니다"));
        player.openInventory(inv);
    }

    private void openTeam(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, TEAM_MENU);
        inv.setItem(20, create(Material.PLAYER_HEAD, "§f팀 초대 메뉴로 이동합니다"));
        inv.setItem(22, create(Material.REDSTONE, "§f팀 색 설정 메뉴로 이동합니다"));
        inv.setItem(24, create(Material.LAVA_BUCKET, "§f팀 삭제 메뉴로 이동합니다"));
        inv.setItem(49, create(Material.ARROW, "§f메인 메뉴로 돌아갑니다"));
        player.openInventory(inv);
    }

    private void openTeamInviteList(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, TEAM_INVITE_LIST);
        inv.setItem(49, create(Material.ARROW, "§f팀 메뉴로 돌아갑니다"));
        player.openInventory(inv);
    }

    private void openTeamInviteConfirm(Player player, Player target) {
        this.inviteTarget = target;
        Inventory inv = Bukkit.createInventory(null, 27, TEAM_INVITE_CONFIRM);
        inv.setItem(11, create(Material.LIME_WOOL, "§f초대하시겠습니까?"));
        inv.setItem(15, create(Material.RED_WOOL, "§f취소합니다"));
        player.openInventory(inv);
    }

    private void openTeamColor(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, TEAM_COLOR_MENU);
        inv.setItem(10, create(Material.RED_DYE, "§f빨간색으로 변경합니다"));
        inv.setItem(12, create(Material.BLUE_DYE, "§f파란색으로 변경합니다"));
        inv.setItem(14, create(Material.GREEN_DYE, "§f초록색으로 변경합니다"));
        inv.setItem(49, create(Material.ARROW, "§f팀 메뉴로 돌아갑니다"));
        player.openInventory(inv);
    }

    private void openTeamDeleteConfirm(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, TEAM_DELETE_CONFIRM);
        inv.setItem(11, create(Material.TNT, "§f팀을 삭제합니다"));
        inv.setItem(15, create(Material.BARRIER, "§f취소합니다"));
        player.openInventory(inv);
    }

    private void openCore(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, CORE_MENU);
        inv.setItem(10, create(Material.BEACON, "§f코어 1"));
        inv.setItem(12, create(Material.BEACON, "§f코어 2"));
        inv.setItem(14, create(Material.BEACON, "§f코어 3"));
        inv.setItem(28, create(Material.BEACON, "§f코어 4"));
        inv.setItem(30, create(Material.BEACON, "§f코어 5"));
        inv.setItem(32, create(Material.BEACON, "§f코어 6"));
        inv.setItem(49, create(Material.ARROW, "§f메인 메뉴로 돌아갑니다"));
        player.openInventory(inv);
    }

    private void openInfo(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, INFO_MENU);
        inv.setItem(22, create(Material.BOOK, "§f점령시간은 19:00~20:00입니다"));
        inv.setItem(49, create(Material.ARROW, "§f메인 메뉴로 돌아갑니다"));
        player.openInventory(inv);
    }

    private ItemStack create(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        String name = e.getCurrentItem().getItemMeta().getDisplayName();
        e.setCancelled(true);

        if (title.equals(MAIN_MENU)) {
            if (name.contains("팀 메뉴")) openTeam(player);
            if (name.contains("코어 메뉴")) openCore(player);
            if (name.contains("정보 메뉴")) openInfo(player);
        }

        if (title.equals(TEAM_MENU)) {
            if (name.contains("팀 초대")) openTeamInviteList(player);
            if (name.contains("색 설정")) openTeamColor(player);
            if (name.contains("삭제")) openTeamDeleteConfirm(player);
            if (name.contains("메인 메뉴")) openMain(player);
        }

        if (title.equals(TEAM_INVITE_LIST)) {
            if (name.contains("돌아갑니다")) openTeam(player);
        }

        if (title.equals(TEAM_INVITE_CONFIRM)) {
            if (name.contains("초대")) player.sendMessage("§f초대를 완료하셨습니다.");
            if (name.contains("취소")) player.sendMessage("§f초대를 취소하셨습니다.");
            openTeam(player);
        }

        if (title.equals(TEAM_COLOR_MENU)) {
            if (name.contains("변경합니다")) player.sendMessage("§f팀 색상이 변경되었습니다.");
            if (name.contains("돌아갑니다")) openTeam(player);
        }

        if (title.equals(TEAM_DELETE_CONFIRM)) {
            if (name.contains("삭제합니다")) player.sendMessage("§f팀을 삭제하셨습니다.");
            if (name.contains("취소")) player.sendMessage("§f팀 삭제를 취소하셨습니다.");
            openTeam(player);
        }

        /** 코어 메뉴 열기 - 실시간 연동 */
        private void openCore(Player player) {
            Inventory inv = Bukkit.createInventory(null, 54, CORE_MENU);

            String playerTeam = TeamMain.getTeam(player);

            for (int id : CoreMain.getCoreLocations().keySet()) {
                String owner = CoreMain.getCoreOwner(id);
                int hp = CoreMain.getCoreHealth(id);

                Material mat;
                String name;

                if (owner.equals(playerTeam)) {
                    mat = Material.BEACON;
                    name = "§f내 팀 코어 #" + (id + 1) + " HP: " + hp + "/5000 (텔레포트 가능)";
                } else {
                    mat = Material.BARRIER;
                    name = "§c적 코어 #" + (id + 1) + " HP: " + hp + "/5000 (텔레포트 불가)";
                }

                ItemStack item = new ItemStack(mat);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(name);
                item.setItemMeta(meta);

                int slot = id;
                if (slot >= 54) slot = 53;
                inv.setItem(slot, item);
            }

            inv.setItem(49, create(Material.ARROW, "§f메인 메뉴로 돌아갑니다"));
            player.openInventory(inv);
        }

        @EventHandler
        public void onClick(InventoryClickEvent e) {
            if (!(e.getWhoClicked() instanceof Player)) return;
            Player player = (Player) e.getWhoClicked();
            String title = e.getView().getTitle();

            if (title.equals(CORE_MENU)) {
                e.setCancelled(true);
                if (e.getCurrentItem() == null) return;
                if (!e.getCurrentItem().hasItemMeta()) return;
                if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

                String name = e.getCurrentItem().getItemMeta().getDisplayName();

                if (name.contains("돌아갑니다")) {
                    openMain(player);
                    return;
                }

                // 텔레포트 시도
                if (name.contains("텔레포트 가능")) {
                    // 코어 위치 가져오기
                    for (int id : CoreMain.getCoreLocations().keySet()) {
                        if (name.contains("#" + (id + 1))) {
                            player.teleport(CoreMain.getCoreLocation(id));
                            player.sendMessage("§f코어 #" + (id + 1) + "로 이동하셨습니다.");
                            break;
                        }
                    }
                } else {
                    player.sendMessage("§c적 팀 코어는 이동할 수 없습니다.");
                }

                // 메뉴를 최신 상태로 갱신
                openCore(player);
            }
        }

        private ItemStack create(Material material, String name) {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            item.setItemMeta(meta);
            return item;
        }

        if (title.equals(INFO_MENU)) {
            if (name.contains("돌아갑니다")) openMain(player);
        }
    }
}
