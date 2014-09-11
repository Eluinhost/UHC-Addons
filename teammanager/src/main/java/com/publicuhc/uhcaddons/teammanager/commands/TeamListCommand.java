/*
 * TeamListCommand.java
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
import com.google.common.collect.Lists;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandOptions;
import com.publicuhc.ultrahardcore.framework.routing.annotation.OptionsMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.PermissionRestriction;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class TeamListCommand implements Command
{
    public static final String TEAM_LIST_PERMISSION = "UHC.teams.list";
    private Translate translate;
    private Scoreboard scoreboard;

    protected TeamListCommand(Translate translate, Scoreboard scoreboard)
    {
        this.translate = translate;
        this.scoreboard = scoreboard;
    }

    @CommandMethod("teamm list")
    @PermissionRestriction(TEAM_LIST_PERMISSION)
    @CommandOptions
    public void onListCommand(OptionSet set, CommandSender sender)
    {
        Set<Team> teams = scoreboard.getTeams();

        if(!set.has("a")) {
            Iterator<Team> i = teams.iterator();
            while(i.hasNext()) {
                if(i.next().getPlayers().isEmpty()) {
                    i.remove();
                }
            }
        }

        if(teams.isEmpty()) {
            translate.sendMessage("no teams", sender);
            return;
        }

        translate.sendMessage("teams header", sender, teams.size());

        for(Team team : teams) {
            Set<OfflinePlayer> players = team.getPlayers();

            Collection<String> playerNames = Lists.newArrayList();

            for(OfflinePlayer p : players) {
                playerNames.add(p.getName());
            }

            translate.sendMessage("teams row", sender, team.getDisplayName(), team.getName(), Joiner.on(",").skipNulls().join(playerNames));
        }
    }

    @OptionsMethod
    public void onListCommand(OptionDeclarer declarer)
    {
        declarer.accepts("a", "Show all teams (including empty)");
    }
}
