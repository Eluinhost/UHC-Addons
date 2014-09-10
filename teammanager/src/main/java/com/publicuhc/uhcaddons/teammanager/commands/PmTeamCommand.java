/*
 * PmTeamCommand.java
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
import com.publicuhc.uhcaddons.teammanager.TeamManager;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.*;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class PmTeamCommand implements Command
{
    private final Translate translate;
    private final TeamManager manager;
    private final Scoreboard scoreboard;

    public static final String PM_TEAM_PERMISSION = "UHC.teams.pm";

    @Inject
    protected PmTeamCommand(Translate translate, Scoreboard scoreboard, TeamManager manager)
    {
        this.translate = translate;
        this.manager = manager;
        this.scoreboard = scoreboard;
    }

    @CommandMethod("pmt")
    @PermissionRestriction(PM_TEAM_PERMISSION)
    @CommandOptions("[arguments]")
    @SenderRestriction(Player.class)
    public void pmTeam(OptionSet set, Player player, List<String> message)
    {
        Team team = scoreboard.getPlayerTeam(player);

        if(null == team) {
            translate.sendMessage("not in team", player);
            return;
        }

        manager.sendToTeamTranslated(team, "pm", player.getName(), Joiner.on(" ").join(message));
    }

    @OptionsMethod
    public void pmTeam(OptionDeclarer declarer)
    {
        declarer.nonOptions().describedAs("Message to send");
    }

}
