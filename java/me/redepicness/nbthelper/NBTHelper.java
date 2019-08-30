package me.redepicness.nbthelper;

import me.redepicness.nbthelper.util.PlayerCommand;
import me.redepicness.nbthelper.util.Util;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Red_Epicness on 6/10/2016 at 9:33 PM.
 */
public class NBTHelper extends JavaPlugin {

    @Override
    public void onEnable() {
        Util.setInstance(this);

        PlayerCommand.scanForCommands();
    }

}
