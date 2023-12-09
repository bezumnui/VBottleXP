package com.viznyuk.minecraft.vbottlexp.events;

import com.viznyuk.minecraft.vbottlexp.VBottleXP;
import com.viznyuk.minecraft.vbottlexp.ui.BottleInventory;
import com.viznyuk.minecraft.vbottlexp.ui.ItemsBase;
import com.viznyuk.minecraft.vbottlexp.utils.Translate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EventListener;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class OnXpBottle implements EventListener, Listener {
    VBottleXP plugin;

    public OnXpBottle(VBottleXP p) {
        plugin = p;
    }

    @EventHandler
    public void itemUse(PlayerInteractEvent interactEntityEvent) {
        if (!interactEntityEvent.getAction().equals(RIGHT_CLICK_BLOCK) &&
        !interactEntityEvent.getAction().equals(RIGHT_CLICK_AIR)) {
            return;
        }
        ItemStack item = interactEntityEvent.getItem();
        Player player = interactEntityEvent.getPlayer();
        if (item == null) return;
        if (item.getType() != Material.EXPERIENCE_BOTTLE) return;
        String level = ItemsBase.getPersistent(plugin, item, "level");
        if (level == null) return;
        int lvl = Integer.parseInt(level);
        int exp = BottleInventory.getEXPRequired(lvl);
        interactEntityEvent.setCancelled(true);
        if (!player.hasPermission("com.viznyuk.vbottlexp.use")) {
            player.sendMessage(Translate.getTranslate(player).get("no-perm-create", true));
            return;
        }
        item.setAmount(item.getAmount() - 1);
        player.updateInventory();
        BottleInventory.setExp(player, player.getTotalExperience() + exp);
    }
    @EventHandler
    public void onXpBottle(ExpBottleEvent event) {
        ItemStack item = event.getEntity().getItem();
        String level = ItemsBase.getPersistent(plugin, item, "level");
        if (level == null) return;
        int lvl = Integer.parseInt(level);
        int exp = BottleInventory.getEXPRequired(lvl);
        event.setExperience(exp);
    }
}
