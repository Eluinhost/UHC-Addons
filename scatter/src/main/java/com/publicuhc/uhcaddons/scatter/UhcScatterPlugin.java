package com.publicuhc.uhcaddons.scatter;

import com.publicuhc.ultrahardcore.UltraHardcore;
import com.publicuhc.ultrahardcore.api.exceptions.FeatureIDConflictException;
import com.publicuhc.ultrahardcore.framework.routing.exception.CommandParseException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UhcScatterPlugin extends JavaPlugin {

    @Override
    public void onEnable()
    {
        UltraHardcore uhc = (UltraHardcore) Bukkit.getPluginManager().getPlugin("UltraHardcore");

        try {
            uhc.registerAddon(new ScatterModule());
        } catch (FeatureIDConflictException e) {
            e.printStackTrace();
        } catch (CommandParseException e) {
            e.printStackTrace();
        }
    }
}
