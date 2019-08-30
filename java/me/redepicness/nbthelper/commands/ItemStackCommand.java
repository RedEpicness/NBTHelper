package me.redepicness.nbthelper.commands;

import me.redepicness.nbthelper.util.PlayerCommand;
import me.redepicness.nbthelper.util.StackManager;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Red_Epicness on 6/10/2016 at 11:43 PM.
 */
public class ItemStackCommand extends PlayerCommand {

    public ItemStackCommand() {
        super("itemstack", "nbthelper.command.itemstack", "istack", "stack", "is");
        setUsage("/itemstack <args>");
        setShortDescription("ADD ME :)");
    }

    @Override
    public void execute(Player p, String[] args) {
        Validate.isTrue(args.length > 0, "Not enough arguments! Do /is help");
        ItemStack s = p.getInventory().getItemInMainHand();
        Validate.isTrue(s != null && !s.getType().equals(Material.AIR), "You must have an item in your hand to do this!");
        StackManager manager = new StackManager(s);
        String operation = args[0];
        String[] remainingArgs = new String[args.length-1];
        System.arraycopy(args, 1, remainingArgs, 0, args.length - 1);
        String msg = "";
        switch (operation){
            case "n":
            case "name":
                msg = handleName(manager, remainingArgs);
                break;
            case "l":
            case "lore":
                msg = handleLore(manager, remainingArgs);
                break;
            case "d":
            case "destroy":
            case "candestroy":
                msg = handleCanDestroy(manager, remainingArgs);
                break;
            case "c":
            case "count":
                msg = handleCount(manager, remainingArgs);
                break;
            case "u":
            case "unbreakable":
                msg = handleUnbreakable(manager, remainingArgs);
                break;
            case "e":
            case "ench":
            case "enchant":
                msg = handleEnchant(manager, remainingArgs);
                break;
            case "f":
            case "flags":
            case "hideflags":
                msg = handleHideFlags(manager, remainingArgs);
                break;
            case "a":
            case "attr":
            case "attributes":
                msg = handleAttributes(manager, remainingArgs);
                break;
            case "r":
            case "repaircost":
                msg = handleRepairCost(manager, remainingArgs);
                break;
            case "h":
            case "?":
            case "wtf":
            case "help":
                handleHelp(p, remainingArgs);
                return;
            default:
                throw new IllegalArgumentException("Invalid argument '"+operation+"'! Do /is help");
        }
        p.getInventory().setItemInMainHand(manager.get());
        p.sendMessage(ChatColor.GREEN+msg);
    }

    public String handleName(StackManager manager, String[] args){
        Validate.isTrue(args.length > 0, "Not enough arguments! Usage: /is name <name>");
        StringBuilder b = new StringBuilder();
        for(String s : args){
            b.append(s).append(" ");
        }
        String name = ChatColor.translateAlternateColorCodes('&', b.toString().trim());
        manager.setName(name);
        return "Set the name to: "+name;
    }

    public String handleLore(StackManager manager, String[] args){
        Validate.isTrue(args.length > 0, "Not enough arguments! Usage: /is lore <add/remove/clear>");
        String operation = args[0];
        switch (operation){
            case "add": {
                Validate.isTrue(args.length > 1, "Not enough arguments! Usage: /is lore add <line>");
                StringBuilder b = new StringBuilder();
                for (String s : args) {
                    b.append(s).append(" ");
                }
                String line = ChatColor.translateAlternateColorCodes('&', b.toString().substring(4).trim());
                List<String> lore = manager.getLore();
                lore.add(line);
                manager.setLore(lore);
                return "Added line: " + line;
            }
            case "remove":{
                Validate.isTrue(args.length > 1, "Not enough arguments! Usage: /is lore remove <index>");
                int index = Integer.valueOf(args[1]);
                List<String> lore = manager.getLore();
                Validate.isTrue(lore.size() > index, "Index out of bounds! Lore size: "+lore.size());
                String remove = lore.remove(index);
                manager.setLore(lore);
                return "Removed line: "+remove;
            }
            case "clear":{
                manager.setLore();
                return "Cleared lore!";
            }
            default:
                throw new IllegalArgumentException("Invalid argument '"+operation+"'!");
        }
    }

    public String handleCanDestroy(StackManager manager, String[] args){
        Validate.isTrue(args.length > 0, "Not enough arguments! Usage: /is destroy <add/remove/clear>");
        String operation = args[0];
        switch (operation){
            case "add": {
                Validate.isTrue(args.length > 1, "Not enough arguments! Usage: /is destroy add <block>");
                String block = args[1];
                List<String> destroy = manager.getCanDestroy();
                destroy.add(block);
                manager.setCanDestroy(destroy);
                return "Added block: " + block;
            }
            case "remove":{
                Validate.isTrue(args.length > 1, "Not enough arguments! Usage: /is destroy remove <block>");
                String block = args[1];
                List<String> destroy = manager.getCanDestroy();
                Validate.isTrue(destroy.contains(block), "The block '"+block+"' was not found!");
                destroy.remove(block);
                manager.setCanDestroy(destroy);
                return "Removed block: "+block;
            }
            case "clear":{
                manager.setCanDestroy();
                return "Cleared CanDestroy!";
            }
            default:
                throw new IllegalArgumentException("Invalid argument '"+operation+"'!");
        }
    }

    public String handleCount(StackManager manager, String[] args){
        Validate.isTrue(args.length > 0, "Not enough arguments! Usage: /is count <count>");
        int count = Integer.valueOf(args[0]);
        manager.setCount(count);
        return "Set the count to: "+count;
    }

    public String handleUnbreakable(StackManager manager, String[] args){
        Validate.isTrue(args.length > 0, "Not enough arguments! Usage: /is unbreakable <true/false");
        boolean unbreakable = Boolean.valueOf(args[0]);
        manager.setUnbreakable(unbreakable);
        return "Set unbreakable to: "+unbreakable;
    }

    public String handleEnchant(StackManager manager, String[] args){
        Validate.isTrue(args.length > 0, "Not enough arguments! Usage: /is enchant <add/remove/clear>");
        String operation = args[0];
        switch (operation){
            case "add": {
                Validate.isTrue(args.length > 2, "Not enough arguments! Usage: /is enchant add <enchantment> <level>");
                Enchantment ench = Enchantment.getByName(args[1].toUpperCase());
                int level = Integer.valueOf(args[2]);
                manager.addEnchantment(ench, level);
                return "Added flag: " + ench.toString();
            }
            case "remove":{
                Validate.isTrue(args.length > 1, "Not enough arguments! Usage: /is enchant remove <enchantment>");
                Enchantment ench = Enchantment.getByName(args[1].toUpperCase());
                manager.removeEnchantment(ench);
                return "Removed enchantment: "+ench.toString();
            }
            case "clear":{
                manager.clearEnchantments();
                return "Cleared enchantments!";
            }
            case "all":{
                for(Enchantment e : Enchantment.values()){
                    manager.addEnchantment(e, 32767);
                }
                return "Added all enchantments!";
            }
            case "list":{
                return "Enchantments: "+ Arrays.toString(Enchantment.values());
            }
            default:
                throw new IllegalArgumentException("Invalid argument '"+operation+"'!");
        }
    }

    public String handleHideFlags(StackManager manager, String[] args){
        Validate.isTrue(args.length > 0, "Not enough arguments! Usage: /is flags <add/remove/clear/all/list>");
        String operation = args[0];
        switch (operation){
            case "add": {
                Validate.isTrue(args.length > 1, "Not enough arguments! Usage: /is flags add <flag>");
                ItemFlag flag = ItemFlag.valueOf(args[1].toUpperCase());
                manager.addHideFlags(flag);
                return "Added flag: " + flag.toString();
            }
            case "remove":{
                Validate.isTrue(args.length > 1, "Not enough arguments! Usage: /is flags remove <flag>");
                ItemFlag flag = ItemFlag.valueOf(args[1].toUpperCase());
                manager.removeHideFlags(flag);
                return "Removed flag: "+flag.toString();
            }
            case "clear":{
                manager.clearHideFlags();
                return "Cleared HideFlags!";
            }
            case "all":{
                manager.addHideFlags(ItemFlag.values());
                return "Added all flags!";
            }
            case "list":{
                return "Flags: "+ Arrays.toString(ItemFlag.values());
            }
            default:
                throw new IllegalArgumentException("Invalid argument '"+operation+"'!");
        }
    }

    public String handleAttributes(StackManager manager, String[] args){
        Validate.isTrue(args.length > 0, "Not enough arguments! Usage: /is attributes <add/remove/clear>");
        return "Not yet implemented!";
    }

    private String handleRepairCost(StackManager manager, String[] args) {
        Validate.isTrue(args.length > 0, "Not enough arguments! Usage: /is repaircost <cost>");
        return "Not yet implemented!";
    }

    public void handleHelp(Player p, String[] args){
        p.sendMessage("No help :3");
    }

}
