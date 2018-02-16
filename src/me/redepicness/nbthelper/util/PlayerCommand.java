package me.redepicness.nbthelper.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Red_Epicness on 5/10/2016 at 1:46 PM.
 */
public abstract class PlayerCommand implements CommandExecutor {

    public static HashMap<String, PlayerCommand> customCommands = new HashMap<>();
    private static CommandMap cmap = null;

    public static void scanForCommands(){
        Util.log("Scanning for commands in jar...");
        int count = 0;
        int errors = 0;
        try {
            CodeSource src = Util.thisPlugin().getClass().getProtectionDomain().getCodeSource();
            if (src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                while(true) {
                    ZipEntry e;
                    try {
                        e = zip.getNextEntry();
                    } catch (IOException e1) {
                        Util.log("Exception reading zip entry! Corrupted?");
                        e1.printStackTrace();
                        errors++;
                        continue;
                    }
                    if (e == null)
                        break;
                    String name = e.getName();
                    if (name.contains("commands/") && name.endsWith(".class")) {
                        String classname = name.replace("/", ".").replace(".class", "");
                        Class clazz;
                        try {
                            clazz = Class.forName(classname);
                        } catch (ClassNotFoundException e1) {
                            Util.log("Cannot load class for '"+name+"' - "+classname);
                            e1.printStackTrace();
                            errors++;
                            continue;
                        }
                        boolean assign = PlayerCommand.class.isAssignableFrom(clazz);
                        if (assign) {
                            PlayerCommand command;
                            try {
                                command = (PlayerCommand) clazz.newInstance();
                            } catch (InstantiationException | IllegalAccessException e1) {
                                Util.log("Error instantiating command from class "+classname);
                                e1.printStackTrace();
                                errors++;
                                continue;
                            }
                            command.register();
                            count++;
                            Util.log("Registered new command '" + command.getLabel() + "' from " + classname);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Util.log("Error opening jar file!");
            e.printStackTrace();
            return;
        }
        Util.log("Finished scanning for commands!");
        Util.log("Encountered "+errors+" errors, found "+count+" commands!");
    }

    private final String label;
    private String usage = null;
    private final String permission;
    private String shortDescription = null;
    private String longDescription = null;
    private ArrayList<String> aliases = new ArrayList<>();

    public PlayerCommand(String label, String permission, String... aliases){
        this.label = label;
        this.permission = permission;
        Collections.addAll(this.aliases, aliases);
        customCommands.put(label, this);
        for(String s : aliases){
            customCommands.put(s, this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("Not a player");
            return true;
        }
        if(!commandSender.hasPermission(permission)){
            commandSender.sendMessage(ChatColor.RED+"No permission!");
            return true;
        }
        try{
            execute(((Player) commandSender), strings);
        }
        catch (IllegalArgumentException e){
            commandSender.sendMessage(ChatColor.RED+"Error: "+e.getMessage());
        }
        catch (Exception e){
            Util.log("Error executing command '"+label+"'!");
            e.printStackTrace();
        }
        return true;
    }

    public abstract void execute(Player p, String[] args);

    public String getLabel() {
        return label;
    }

    public String getUsage() {
        return usage == null?ChatColor.GRAY+"Unknown":usage;
    }

    public String getShortDescription() {
        return shortDescription == null?ChatColor.GRAY+"Unknown":shortDescription;
    }

    public String getLongDescription() {
        return longDescription == null?ChatColor.GRAY+"Unknown":longDescription;
    }

    public String getPermission() {
        return permission;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        if(longDescription == null) this.longDescription = shortDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public void register(){
        getCommandMap().register(label, "nbt", new Command(label){
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                return onCommand(sender, this, commandLabel, args);
            }
        }.setAliases(aliases));
    }

    CommandMap getCommandMap() {
        if (cmap == null) {
            try {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Error loading commands!", e);
            }
        }
        else return cmap;
    }

}
