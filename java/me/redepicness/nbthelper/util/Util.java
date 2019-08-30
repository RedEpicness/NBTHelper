package me.redepicness.nbthelper.util;

import me.redepicness.nbthelper.NBTHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**`1e
 * Created by Red_Epicness on 1/20/2016 at 10:55 PM.
 */
public class Util {

    private static NBTHelper pluginInstance;

    public static void log(String msg){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY+msg);
    }

    public static void setInstance(NBTHelper goalHunterPluginInstance) {
        pluginInstance = goalHunterPluginInstance;
    }

    public static NBTHelper thisPlugin(){
        return pluginInstance;
    }

    public static World defaultWorld(){
        return Bukkit.getWorld("world");
    }

    public static BukkitTask schedule(BukkitRunnable runnable, int delay){
        return runnable.runTaskLater(thisPlugin(), delay);
    }

    public static BukkitTask schedule(BukkitRunnable runnable, int delay, int period){
        return runnable.runTaskTimer(thisPlugin(), delay, period);
    }

}
