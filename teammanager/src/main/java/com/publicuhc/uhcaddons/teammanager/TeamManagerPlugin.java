/*
 * TeamManagerPlugin.java
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Graham Howden <graham_howden1 at yahoo.co.uk>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
