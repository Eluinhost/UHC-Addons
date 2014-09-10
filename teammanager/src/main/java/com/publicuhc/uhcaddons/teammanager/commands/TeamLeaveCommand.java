/*
 * TeamLeaveCommand.java
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

import com.publicuhc.uhcaddons.teammanager.TeamManager;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.*;
import com.publicuhc.ultrahardcore.framework.routing.converters.OnlinePlayerValueConverter;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Set;

public class TeamLeaveCommand implements Command
{
    private Translate translate;
    private Scoreboard scoreboard;
    private TeamManager manager;

    public static final String TEAM_LEAVE_PERMISSION = "UHC.teams.leave";
    public static final String TEAM_REMOVE_PERMISSION = "UHC.teams.remove";

    @Inject
    protected TeamLeaveCommand(Translate translate, Scoreboard scoreboard, TeamManager manager)
    {
        this.translate = translate;
        this.scoreboard = scoreboard;
        this.manager = manager;
    }

    private boolean removePlayerFromTeam(Player player)
    {
        Team team = scoreboard.getPlayerTeam(player);

        if(null == team) {
            translate.sendMessage("not in team", player);
            return false;
        }

        team.removePlayer(player);
        translate.sendMessage("removed from team", player);
        manager.sendToTeamTranslated(team, "player left", player.getName());

        return true;
    }

    @CommandMethod("teamm leave")
    @SenderRestriction(Player.class)
    @PermissionRestriction(TEAM_LEAVE_PERMISSION)
    public void leaveTeam(OptionSet set, Player player)
    {
        removePlayerFromTeam(player);
    }

    @CommandMethod("teamm remove")
    @PermissionRestriction(TEAM_REMOVE_PERMISSION)
    @CommandOptions("[arguments]")
    public void removeFromTeam(OptionSet set, CommandSender sender, List<Player[]> args)
    {
        Set<Player> players = OnlinePlayerValueConverter.recombinePlayerLists(args);

        if(players.isEmpty()) {
            translate.sendMessage("supply one player name", sender);
            return;
        }

        int count = 0;

        for(Player player : players) {
            boolean removed = removePlayerFromTeam(player);

            if(removed) {
                count++;
            }
        }

        translate.sendMessage("removed from teams", sender, count);
    }

    @OptionsMethod
    public void removeFromTeam(OptionDeclarer declarer)
    {
        declarer.nonOptions()
                .withValuesConvertedBy(new OnlinePlayerValueConverter(true))
                .describedAs("Players to remove from their team (or * for all)");
    }
}
