package com.viznyuk.minecraft.vbottlexp.ui;

import com.viznyuk.minecraft.vbottlexp.VBottleXP;
import com.viznyuk.minecraft.vbottlexp.utils.Translate;
import com.viznyuk.minecraft.vbottlexp.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.window.Window;

import java.util.*;

public class BottleInventory extends ItemsBase{
    private ItemObject itemObject;

    public static void display(VBottleXP plugin, Player player) {
        int height = Utils.getItems().getInt("structure.height");
        Gui gui = Gui.empty(9, height);
        gui.applyStructure(generate(plugin, player));
        String title = plugin.getConfig().getString("ui-title");
        if (title == null) {
            throw new RuntimeException("title not found at config.yml");
        }
        Window window = Window.single()
                .setViewer(player)
                .setTitle(title)
                .setGui(gui)
                .build();

        window.open();
    }

    private static Structure generate(VBottleXP plugin, Player player) {
        List<String> rows = Utils.getItems().getStringList("structure.rows");
        Structure structure = new Structure(rows.toArray(new String[0]));
        ConfigurationSection cs = Utils.getItems().getConfigurationSection("items");
        assert cs != null;
        Set<String> itemKeys = cs.getKeys(true);

        for (String item_key : itemKeys) {
            if (item_key.contains(".")) continue;
            ConfigurationSection iterItem = cs.getConfigurationSection(item_key);
            if (iterItem == null) continue;
            ItemObject itemObject = new ItemObject(iterItem, item_key);
            BottleInventory item;
            if (Objects.equals(itemObject.type, "fill")) {
                item = new BottleInventory(plugin, player, itemObject.type, new ItemStack(itemObject.material));
            } else if (Objects.equals(itemObject.type, "bottle")) {
                ItemStack itemStack = new ItemStack(Material.EXPERIENCE_BOTTLE);
                BottleInventory.setPersistent(plugin, itemStack, "level", String.valueOf(itemObject.level));
                item = new BottleInventory(plugin, player, itemObject.type, itemStack);
            } else {
                Utils.debug(player, "unknown type: " + itemObject.type);
                throw new RuntimeException("unknown type: " + itemObject.type);
            }
            item.setDisplayedName(PlaceholderAPI.setPlaceholders(player, itemObject.name));
            if (itemObject.lore != null) {
                item.setLore(PlaceholderAPI.setPlaceholders(player, itemObject.lore));
            }
            item.itemObject = itemObject;
            structure.addIngredient(itemObject.key.charAt(0), item);

        }
        return structure;
    }
    public static int getEXPRequired(int level){
        if (1 <= level && level <= 16)
            return (int) (Math.pow(level, 2) + 6 * level);
        else if (17 <= level && level <= 31)
            return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
        else
            return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
    }

    protected BottleInventory(VBottleXP plugin, OfflinePlayer player, String id, ItemStack itemStack) {
        super(plugin, player, id, itemStack);
    }

    public static void setExp(Player player, int xp) {
        player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0);
        player.setTotalExperience(0);
        player.giveExp(xp);
    }
    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent, String id) {
        if (!id.equals("bottle")) {
            return;
        }
        if (!player.hasPermission("com.viznyuk.vbottlexp.create.*") &&
        !player.hasPermission(itemObject.permission)) {
            player.sendMessage(Translate.getTranslate(player).get("no-perm-create", true));
            return;
        }
        int level = Integer.parseInt(itemObject.level);
        int required = getEXPRequired(level);
        if (player.getTotalExperience() < required) {
            player.sendMessage(getTranslate().get("not-enough-xp", true));
            return;
        }
        int xp = player.getTotalExperience() - required;
        setExp(player, xp);
        setLore(itemObject.finalLore);
        player.getInventory().addItem(itemBuilder.get());

    }
}
