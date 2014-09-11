/*
 * TeamCreateCommand.java
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

package com.publicuhc.uhcaddons.teammanager.commands;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.publicuhc.uhcaddons.teammanager.TeamManager;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandOptions;
import com.publicuhc.ultrahardcore.framework.routing.annotation.OptionsMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.PermissionRestriction;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class TeamCreateCommand implements Command
{

    public static final String TEAM_CREATE_PERMISSION = "UHC.teams.create";

    private final Scoreboard scoreboard;
    private final TeamManager teamManager;
    private final Translate translate;

    @Inject
    protected TeamCreateCommand(Scoreboard scoreboard, TeamManager teamManager, Translate translate)
    {
        this.scoreboard = scoreboard;
        this.teamManager = teamManager;
        this.translate = translate;
    }

    @CommandMethod("teamm create")
    @CommandOptions({"[arguments]", "n"})
    @PermissionRestriction(TEAM_CREATE_PERMISSION)
    public void createTeam(OptionSet set, CommandSender sender, List<String> nonOptions, Integer n)
    {
        if(set.has("n")) {
            if(n <= 0) {
                translate.sendMessage("greater than zero", sender);
                return;
            }

            for(int i = 0; i < n; i++) {
                teamManager.getNewTeam(set.has("r"));
            }

            translate.sendMessage("created", sender, n);
            return;
        }

        if(nonOptions.isEmpty()) {
            translate.sendMessage("supply one team name", sender);
            return;
        }

        List<String> invalid = Lists.newArrayList();

        for(String name : nonOptions) {
            Optional<Team> team = teamManager.createTeam(name, set.has("r"));

            if(!team.isPresent()) {
                invalid.add(name);
            }
        }

        translate.sendMessage("created", sender, n);

        if(!invalid.isEmpty()) {
            translate.sendMessage("failed to create", sender, Joiner.on(",").join(invalid));
        }
    }

    @OptionsMethod
    public void createTeam(OptionDeclarer declarer)
    {
        declarer.accepts("r", "Generate random display names");
        declarer.accepts("n", "Number of auto-named teams to create").withRequiredArg().ofType(Integer.class);
    }
}
