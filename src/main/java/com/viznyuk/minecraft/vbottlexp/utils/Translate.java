package com.viznyuk.minecraft.vbottlexp.utils;

import com.viznyuk.minecraft.vbottlexp.VBottleXP;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Translate {
    protected String lang;
    protected File language_file;
    protected YamlConfiguration yml;
    protected VBottleXP plugin;
    private static Translate instance;



    public static void init(VBottleXP plugin, String lang) {
        instance = new Translate(plugin, lang);
    }

    private static Translate getInstance() {
        return instance;
    }
    public static TranslatePlaceholder getTranslate(OfflinePlayer player) {
        return new TranslatePlaceholder(player, getInstance().lang, getInstance().yml, getInstance().plugin);
    }


    protected Translate(VBottleXP plugin, String lang) {
        this.lang = lang;
        this.plugin = plugin;
        this.language_file = new File(plugin.getDataFolder() +  "/lang_" + lang + ".yml");
        yml = YamlConfiguration.loadConfiguration(language_file);
    }





}
