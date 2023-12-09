package com.viznyuk.minecraft.vbottlexp.ui;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class ItemObject {
    public String key;
    public String type;
    public Material material;
    public String name;
    public String lore;
    public String finalLore;
    public String permission;
    public String level;

    public ItemObject(ConfigurationSection cs, String key) {
        this.key = key;
        this.type = cs.getString("type");
        String mat = cs.getString("material");
        if (mat != null) {
            this.material = Material.matchMaterial(mat);
        }
        this.name = cs.getString("name");
        this.lore = cs.getString("lore");
        this.finalLore = cs.getString("final-lore");
        this.permission = cs.getString("permission");
        this.level = cs.getString("level");
    }

    public boolean hasType(String type) {
        return Objects.equals(this.type, type);
    }

}
