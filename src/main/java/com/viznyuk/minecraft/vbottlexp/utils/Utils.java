package com.viznyuk.minecraft.vbottlexp.utils;

import com.viznyuk.minecraft.vbottlexp.VBottleXP;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class Utils {
    private VBottleXP plugin;

    private static Utils instance;
    public static void init(VBottleXP plugin) {
        if (instance != null) return;
        instance = new Utils(plugin);
    }
    Utils(VBottleXP plugin) {
        this.plugin = plugin;
    }

    public static YamlConfiguration getItems() {
        File language_file = new File(instance.plugin.getDataFolder(), "items.yml");
        return YamlConfiguration.loadConfiguration(language_file);
    }

    public static void debug(String text) {
        if (!instance.plugin.getConfig().getBoolean("debug")) {
            return;
        }
        Throwable t = new Throwable();
        StackTraceElement[] frames = t.getStackTrace();
        System.out.print(String.format("DEBUG: line %s: %s -> %s: ", frames[0].getLineNumber(), frames[1].getFileName(), frames[1].getClassName()));
        System.out.println(text);
    }

    public static void debug(Player p, String text) {
        if (!instance.plugin.getConfig().getBoolean("debug")) {
            return;
        }
        Throwable t = new Throwable();
        StackTraceElement[] frames = t.getStackTrace();
        p.sendMessage(String.format("DEBUG: line %s: %s -> %s: ", frames[0].getLineNumber(), frames[1].getFileName(), frames[1].getClassName()));
        p.sendMessage(text);
        debug(text);
    }
}
