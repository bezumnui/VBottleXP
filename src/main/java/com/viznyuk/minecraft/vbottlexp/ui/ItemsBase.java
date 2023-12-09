package com.viznyuk.minecraft.vbottlexp.ui;

import com.viznyuk.minecraft.vbottlexp.VBottleXP;
import com.viznyuk.minecraft.vbottlexp.utils.Translate;
import com.viznyuk.minecraft.vbottlexp.utils.TranslatePlaceholder;
import com.viznyuk.minecraft.vbottlexp.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.Arrays;
import java.util.concurrent.Callable;

public abstract class ItemsBase extends AbstractItem {

    protected ItemBuilder itemBuilder;
    private final OfflinePlayer player;
    private final TranslatePlaceholder translate;
    private String lore;
    private String name;
    private final VBottleXP plugin;

    public TranslatePlaceholder getTranslate() {
        return translate;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }




    protected ItemsBase(VBottleXP plugin, OfflinePlayer player, String id, ItemStack itemStack) {
        this.plugin = plugin;
        this.player = player;
        this.translate = Translate.getTranslate(player);
        setId(itemStack, id);
        this.itemBuilder = new ItemBuilder(itemStack);
    }

    public ItemsBase setDisplayedName(String name) {
        if (name != null) {
            this.name = name;
            itemBuilder.setDisplayName(name);
        }
        return this;
    }
    public ItemsBase setTranslatedName(String name) {
        setDisplayedName(this.getTranslate().getName(name));
        return this;
    }
    public ItemsBase setLore(String name) {
        lore = name;
        if (name != null) {
            itemBuilder.setLegacyLore(Arrays.asList(name.split("\\;")));
        }

        return this;
    }

    public ItemsBase setTranslatedLore(String name) {

        setLore(this.getTranslate().getLore(name));
        return this;
    }
    public ItemsBase replaceLore(String key, String value) {
        setLore(lore.replace(key, value));
        return this;
    }
    public ItemsBase replaceName(String key, String value) {
        setDisplayedName(name.replace(key, value));
        return this;
    }
    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    public void setItemBuilder(ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    @Override
    public ItemProvider getItemProvider() {
        return itemBuilder;
    }

    protected void setId(ItemStack item, String id) {
        setPersistent(item, "id", id);
    }

    public void setPersistent(ItemStack item, String key_, String value) {
        setPersistent(plugin, item, key_, value);
    }
    public static void setPersistent(VBottleXP plugin, ItemStack item, String key_, String value) {
        if (item == null) {
            Utils.debug("setId -> item == null");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            Utils.debug("setId -> meta == null");
            return;
        }
        NamespacedKey key = new NamespacedKey(plugin, key_);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }


    abstract public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent, String id);

    public String getId() {
        return getPersistent("id");
    }
    public String getPersistent(String key_) {
        ItemStack item = itemBuilder.get();
        return getPersistent(plugin, item, key_);
    }
    public static String getPersistent(VBottleXP plugin, ItemStack item, String key_) {

        if (item == null) {
            Utils.debug("getId -> item == null");
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            Utils.debug("getId -> meta == null");
            return null;
        }
        NamespacedKey key = new NamespacedKey(plugin, key_);

        String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (id == null) {
            Utils.debug("getId -> id == null");
        }
        return id;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        String id = getId();
        if (id == null) {
            Utils.debug("handleClick -> id == null");
            return;
        }
        handleClick(clickType, player, inventoryClickEvent, id);
    };
}
