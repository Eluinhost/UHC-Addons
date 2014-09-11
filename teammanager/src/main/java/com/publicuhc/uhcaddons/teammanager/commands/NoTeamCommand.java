/*
 * NoTeamCommand.java
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
import com.publicuhc.ultrahardcore.framework.routing.annotation.PermissionRestriction;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.List;

public class NoTeamCommand implements Command
{
    public static final String NO_TEAM_PERMISSION = "UHC.teams.noteam";
    private final Translate translate;
    private final Scoreboard scoreboard;

    protected NoTeamCommand(Translate translate, Scoreboard scoreboard)
    {
        this.translate = translate;
        this.scoreboard = scoreboard;
    }

    @CommandMethod("noteam")
    @PermissionRestriction(NO_TEAM_PERMISSION)
    public void noteam(OptionSet set, CommandSender sender)
    {
        Player[] online = Bukkit.getOnlinePlayers();

        List<Player> noteam = Lists.newArrayList();

        for(Player player : online) {
            if(scoreboard.getPlayerTeam(player) == null) {
                noteam.add(player);
            }
        }

        if(noteam.isEmpty()) {
            translate.sendMessage("all in teams", sender);
            return;
        }

        Collection<String> playerNames = Lists.newArrayList();

        for(Player p : noteam) {
            playerNames.add(p.getName());
        }

        translate.sendMessage("no team", sender, Joiner.on(",").join(playerNames));
    }
}
