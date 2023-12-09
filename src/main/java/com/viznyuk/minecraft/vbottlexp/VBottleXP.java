package com.viznyuk.minecraft.vbottlexp;

import com.viznyuk.minecraft.vbottlexp.commands.XPBottle;
import com.viznyuk.minecraft.vbottlexp.events.OnXpBottle;
import com.viznyuk.minecraft.vbottlexp.ui.BottleInventory;
import com.viznyuk.minecraft.vbottlexp.ui.ItemObject;
import com.viznyuk.minecraft.vbottlexp.utils.Translate;
import com.viznyuk.minecraft.vbottlexp.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.Set;

public final class VBottleXP extends JavaPlugin {

    @Override
    public void onEnable() {
        initResources();
        Translate.init(this, getConfig().getString("lang"));
        Utils.init(this);
        registerPermissions();
        Objects.requireNonNull(getCommand("xpbottle")).setExecutor(new XPBottle(this));
        Bukkit.getPluginManager().registerEvents(new OnXpBottle(this), this);

        // Plugin startup logic
    }

    private void registerPermissions() {
        ConfigurationSection cs = Utils.getItems().getConfigurationSection("items");
        assert cs != null;
        Set<String> itemKeys = cs.getKeys(true);

        for (String item_key : itemKeys) {
            if (item_key.contains(".")) continue;
            ConfigurationSection iterItem = cs.getConfigurationSection(item_key);
            if (iterItem == null) continue;
            ItemObject itemObject = new ItemObject(iterItem, item_key);
            if (itemObject.permission == null) continue;
            Bukkit.getPluginManager().addPermission(new Permission(itemObject.permission));
        }
    }


    private void initResources() {
        if (!new File(getDataFolder(), "lang_en.yml").exists()) {
            saveResource("lang_en.yml", true);
        }
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveResource("config.yml", true);
        }
        if (!new File(getDataFolder(), "items.yml").exists()) {
            saveResource("items.yml", true);
        }
        if (!new File(getDataFolder(), "lang_ru.yml").exists()) {
            saveResource("lang_ru.yml", true);
        }
        if (!new File(getDataFolder(), "lang_ukr.yml").exists()) {
            saveResource("lang_ukr.yml", true);
        }
    }

}
