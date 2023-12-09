package com.viznyuk.minecraft.vbottlexp.commands;

import com.viznyuk.minecraft.vbottlexp.VBottleXP;
import com.viznyuk.minecraft.vbottlexp.ui.BottleInventory;
import com.viznyuk.minecraft.vbottlexp.utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class XPBottle implements CommandExecutor {
    private final VBottleXP plugin;
    public XPBottle(VBottleXP plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
//        ItemStack bottle = new ItemStack();

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only player can use the command!");
            return true;
        }
        Player p = (Player) commandSender;
        if (!p.hasPermission("com.viznyuk.vbottlexp.command")) {
            p.sendMessage(Translate.getTranslate(p).get("no-perm-command", true));
            return true;
        }
        BottleInventory.display(plugin, p);
        return true;
    }
}
