package com.publicuhc.uhcaddons.scatter;

import com.publicuhc.ultrahardcore.UltraHardcore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UhcScatterPlugin extends JavaPlugin {

    @Override
    public void onEnable()
    {
        UltraHardcore uhc = (UltraHardcore) Bukkit.getPluginManager().getPlugin("UltraHardcore");

        uhc.registerAddon(new ScatterModule());
    }
}
