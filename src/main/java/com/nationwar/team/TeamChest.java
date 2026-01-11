package com.nationwar.team;

import com.google.gson.Gson;
import com.nationwar.NationWar;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import java.io.*;
import java.util.*;

public class TeamChest {
    private static Map<String, String> chestData = new HashMap<>(); // 팀이름: Base64데이터
    private static File file;
    private static final Gson gson = new Gson();

    public static void loadChests() {
        file = new File(NationWar.getInstance().getDataFolder(), "teamchest.json");
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Map<String, String> loaded = gson.fromJson(reader, HashMap.class);
                if (loaded != null) chestData = loaded;
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public static void saveChests() {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(chestData, writer);
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ItemStack 배열 -> Base64 문자열
    public static String toBase64(ItemStack[] items) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            dataOutput.writeInt(items.length);
            for (ItemStack item : items) dataOutput.writeObject(item);
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) { return ""; }
    }

    // Base64 문자열 -> ItemStack 배열
    public static ItemStack[] fromBase64(String data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) items[i] = (ItemStack) dataInput.readObject();
            return items;
        } catch (Exception e) { return new ItemStack[54]; }
    }

    public static Map<String, String> getChestData() { return chestData; }
}