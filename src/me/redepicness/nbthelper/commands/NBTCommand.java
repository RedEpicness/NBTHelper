package me.redepicness.nbthelper.commands;

import me.redepicness.nbthelper.util.PlayerCommand;
import me.redepicness.nbthelper.util.Util;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedHashSet;

/**
 * Created by Red_Epicness on 6/14/2016 at 2:29 PM.
 */
public class NBTCommand extends PlayerCommand {

    public NBTCommand() {
        super("nbt", "", "nbthelper", "wtf");
        setUsage("/nbt <help/ver>");
        setShortDescription("Displays various info about this plugin!");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(ChatColor.GOLD + "NBT Helper by " + ChatColor.GREEN + "Red_Epicness" + ChatColor.GOLD + ".");
            return;
        }
        switch (args[0]){
            case "v":
            case "ver":
            case "version":{
                p.sendMessage(ChatColor.GOLD+"This server is running NBT Helper "+ChatColor.AQUA+Util.thisPlugin().getDescription().getVersion()+ChatColor.GOLD+"!");
                break;
            }
            case "?":
            case "help":
            case "welp":
            case "wtf":{
                if(args.length == 1){
                    p.sendMessage(ChatColor.GOLD+"Help for NBT Helper commands:");
                    for(PlayerCommand c : new LinkedHashSet<>(customCommands.values())){
                        if (!p.hasPermission(c.getPermission()))
                            continue;
                        p.sendMessage(ChatColor.RED+"• "+ChatColor.GOLD+"/"+c.getLabel()+ChatColor.AQUA+" - "+c.getShortDescription());
                    }
                    p.sendMessage(ChatColor.GRAY+"Do /nbt help <command> for more info about each command.");
                    break;
                }
                PlayerCommand c = customCommands.getOrDefault(args[1], null);
                Validate.notNull(c, "Invalid command '"+args[1]+"'");
                if(!p.hasPermission(c.getPermission()))
                    Validate.isTrue(true, "Invalid command '"+args[1]+"'");
                p.sendMessage(ChatColor.GOLD+"Displaying help for "+ChatColor.AQUA+c.getLabel()+ChatColor.GOLD+":");
                p.sendMessage(ChatColor.WHITE+"Usage: "+ChatColor.GREEN+c.getUsage());
                p.sendMessage(ChatColor.WHITE+"Description: "+ChatColor.GRAY+c.getLongDescription());
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid argument '"+args[0]+"'!");
        }
    }
}
