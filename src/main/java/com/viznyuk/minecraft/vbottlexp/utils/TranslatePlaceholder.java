package com.viznyuk.minecraft.vbottlexp.utils;

import com.viznyuk.minecraft.vbottlexp.VBottleXP;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class TranslatePlaceholder {
    OfflinePlayer player;
    protected String lang;
    protected YamlConfiguration yml;
    @NotNull protected VBottleXP plugin;


    public TranslatePlaceholder(OfflinePlayer player, String lang, YamlConfiguration yml, @NotNull VBottleXP plugin) {
        this.plugin = plugin;
        this.player = player;
        this.lang = lang;
        this.yml = yml;
    }

    public String get(String path) {
        String res = yml.getString(path);
        if (res == null) {
            Throwable t = new Throwable();
            StackTraceElement[] frames = t.getStackTrace();

            return String.format("§4Please inform to administrator:§f\n§3\"%s\"§f wasn't found in the language (§c%s§f) config. " +
                    "\n§c%s§f -> §9%s§f", path, lang, frames[1].getFileName(), frames[1].getClassName());
        }
        return PlaceholderAPI.setPlaceholders(player, res).replace("&", "§");
    }

    public String get(String path, boolean usePrefix) {
        if (usePrefix) {
            String prefix = this.plugin.getConfig().getString("prefix");
            assert prefix != null;
            return prefix.replace("&", "§") + get(path);
        }
        return get(path);
    }

    public String getName(String path) {
        Object res = yml.get(path);
        if (res != null && yml.get(path + ".name") == null) {
            return null;
        }
        return get(path + ".name");

    }
    public String getLore(String path) {
        Object res = yml.get(path);
        if (res != null && yml.get(path + ".lore") == null) {
            return null;
        }
        return get(path + ".lore");
    }
}
