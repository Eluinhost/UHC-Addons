package com.publicuhc.uhcaddons.teammanager;

import com.google.common.collect.Lists;
import com.publicuhc.uhcaddons.teammanager.commands.*;
import com.publicuhc.ultrahardcore.UltraHardcore;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.api.UHCAddonConfiguration;
import com.publicuhc.ultrahardcore.api.UHCFeature;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Module;
import com.publicuhc.ultrahardcore.framework.shaded.inject.multibindings.Multibinder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class TeamManagerPlugin extends JavaPlugin implements UHCAddonConfiguration
{

    @Override
    public void onEnable()
    {
        UltraHardcore uhc = (UltraHardcore) Bukkit.getPluginManager().getPlugin("UltraHardcore");

        List<Module> modules = Lists.newArrayList((Module) new TeamManagerModule());

        uhc.registerAddon(this, this, modules);
    }

    @Override
    public void configureCommands(Multibinder<Command> commandMultibinder)
    {
        commandMultibinder.addBinding().to(NoTeamCommand.class);
        commandMultibinder.addBinding().to(PmTeamCommand.class);
        commandMultibinder.addBinding().to(RandomTeamsCommand.class);
        commandMultibinder.addBinding().to(TeamCreateCommand.class);
        commandMultibinder.addBinding().to(TeamDeleteCommand.class);
        commandMultibinder.addBinding().to(TeamJoinCommand.class);
        commandMultibinder.addBinding().to(TeamLeaveCommand.class);
        commandMultibinder.addBinding().to(TeamRequestsCommand.class);
        commandMultibinder.addBinding().to(TeamupCommand.class);
    }

    @Override
    public void configureFeatures(Multibinder<UHCFeature> uhcFeatureMultibinder)
    {
        //no features enabled
    }
}
