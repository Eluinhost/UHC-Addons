/*
 * TeamRequestsCommand.java
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
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TeamRequestsCommand implements Command
{
    public static final String REQUEST_PERMISSION = "UHC.teams.requests.request";
    public static final String ADMIN_PERMISSION = "UHC.teams.requests.admin";
    private final Translate translate;
    private final TeamManager manager;
    private Multimap<String, String> requests = ArrayListMultimap.create();

    @Inject
    protected TeamRequestsCommand(Translate translate, TeamManager manager)
    {
        this.translate = translate;
        this.manager = manager;
    }

    @CommandMethod("reqteam")
    @PermissionRestriction(REQUEST_PERMISSION)
    @CommandOptions("[arguments]")
    public void onTeamRequest(OptionSet set, Player player, List<String> args)
    {
        Set<String> players = Sets.newHashSet(args);
        players.remove(player.getName());

        if(players.isEmpty()) {
            translate.sendMessage("supply one player name", player);
            return;
        }

        requests.replaceValues(player.getName(), players);

        translate.broadcastMessageForPermission(ADMIN_PERMISSION, "new request", player.getName(), Joiner.on(",").join(players));
        translate.sendMessage("request added", player);
    }

    @OptionsMethod
    public void onTeamRequest(OptionDeclarer declarer)
    {
        declarer.nonOptions().describedAs("Players to team with");
    }

    @CommandMethod("reqteam list")
    @PermissionRestriction(ADMIN_PERMISSION)
    public void onRequestList(OptionSet set, CommandSender sender)
    {
        if(requests.size() == 0) {
            translate.sendMessage("no requests", sender);
            return;
        }

        translate.sendMessage("requests list header", sender, requests.size());

        for(Map.Entry<String, Collection<String>> entry : requests.asMap().entrySet()) {
            translate.sendMessage("requests list row", sender, entry.getKey(), Joiner.on(",").join(entry.getValue()));
        }
    }

    @CommandMethod("reqteam accept")
    @PermissionRestriction(ADMIN_PERMISSION)
    @CommandOptions("[arguments]")
    public void onRequestAccept(OptionSet set, CommandSender sender, List<String> args)
    {
        boolean force = set.has("f");

        Set<String> invalids = Sets.newHashSet();

        outer:
        for(String toAccept : args) {
            if(!requests.containsKey(toAccept)) {
                invalids.add(toAccept);
                continue;
            }

            Collection<String> team = requests.get(toAccept);

            Set<Player> players = Sets.newHashSet();

            for(String member : team) {
                Player player = Bukkit.getPlayer(member);

                if(null == player) {
                    if(!force) {
                        invalids.add(toAccept);
                        continue outer;
                    }
                } else {
                    players.add(player);
                }
            }

            Team actualTeam = manager.getNewTeam(true);

            Collection<String> playerNames = Lists.newArrayList();

            for(Player p : players) {
                playerNames.add(p.getName());
                actualTeam.addPlayer(p);
            }

            manager.sendToTeamTranslated(actualTeam, "your team", Joiner.on(",").join(playerNames));
            requests.removeAll(toAccept);
        }

        if(!invalids.isEmpty()) {
            translate.sendMessage("couldnt complete requests", sender, Joiner.on(",").join(invalids));
        }

        if(invalids.size() != args.size()) {
            translate.sendMessage("accepted requests", sender);
        }
    }

    @OptionsMethod
    public void onRequestAccept(OptionDeclarer declarer)
    {
        declarer.accepts("f", "Force create team even if all players are not online");
        declarer.nonOptions().describedAs("List of request IDs to accept");
    }

    @CommandMethod("reqteam deny")
    @PermissionRestriction(ADMIN_PERMISSION)
    @CommandOptions("[arguments]")
    public void onRequestDeny(OptionSet set, CommandSender sender, List<String> args)
    {
        Set<String> invalids = Sets.newHashSet();

        for(String toAccept : args) {
            if(!requests.containsKey(toAccept)) {
                invalids.add(toAccept);
                continue;
            }

            Player requester = Bukkit.getPlayer(toAccept);

            if(requester != null) {
                translate.sendMessage("request denied", sender);
            }

            requests.removeAll(toAccept);
        }

        if(!invalids.isEmpty()) {
            translate.sendMessage("couldnt complete requests", sender, Joiner.on(",").join(invalids));
        }

        if(invalids.size() != args.size()) {
            translate.sendMessage("denied requests", sender);
        }
    }

    @OptionsMethod
    public void onRequestDeny(OptionDeclarer declarer)
    {
        declarer.nonOptions().describedAs("List of request IDs to deny");
    }
}
