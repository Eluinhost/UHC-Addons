package com.publicuhc.uhcaddons.scatter;

import com.publicuhc.ultrahardcore.UltraHardcore;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.api.UHCAddonConfiguration;
import com.publicuhc.ultrahardcore.api.UHCFeature;
import com.publicuhc.ultrahardcore.framework.shaded.inject.multibindings.Multibinder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UhcScatterPlugin extends JavaPlugin implements UHCAddonConfiguration
{

    @Override
    public void onEnable()
    {
        UltraHardcore uhc = (UltraHardcore) Bukkit.getPluginManager().getPlugin("UltraHardcore");
        uhc.registerAddon(this, this);
    }

    @Override
    public void configureCommands(Multibinder<Command> commandMultibinder)
    {
        commandMultibinder.addBinding().to(ScatterCommand.class);
    }

    @Override
    public void configureFeatures(Multibinder<UHCFeature> uhcFeatureMultibinder)
    {
        //no features enabled
    }
}
