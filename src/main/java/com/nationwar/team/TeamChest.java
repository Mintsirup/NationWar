package com.nationwar.team;

import com.google.gson.Gson;
import com.nationwar.NationWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TeamChest {
    private static Map<String, String> chests = new HashMap<>();

    public static void open(Player player) {
        String team = TeamMain.getPlayerTeam(player);
        if (team.equals("방랑자")) return;

        String data = chests.getOrDefault(team, "");
        Inventory inv = data.isEmpty() ? Bukkit.createInventory(null, 54, "국가 창고") : deserialize(data);
        player.openInventory(inv);
    }

    public static String serialize(Inventory inv) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inv.getSize());
            for (int i = 0; i < inv.getSize(); i++) dataOutput.writeObject(inv.getItem(i));
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) { return ""; }
    }

    public static Inventory deserialize(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inv = Bukkit.createInventory(null, dataInput.readInt(), "국가 창고");
            for (int i = 0; i < inv.getSize(); i++) inv.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inv;
        } catch (Exception e) { return Bukkit.createInventory(null, 54, "국가 창고"); }
    }
}