/*
 * BukkitTeamManager.java
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

import com.google.common.base.Optional;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Set;

public class BukkitTeamManager implements TeamManager {

    private final Scoreboard scoreboard;
    private final TeamNameGenerator nameGenerator;
    private final Translate translate;

    public BukkitTeamManager(Scoreboard scoreboard, TeamNameGenerator nameGenerator, Translate translate)
    {
        this.scoreboard = scoreboard;
        this.nameGenerator = nameGenerator;
        this.translate = translate;
    }

    @Override
    public Team getNewTeam(boolean randomName)
    {
        String nextName = nameGenerator.getNextAvailableTeamName();
        Team newTeam = scoreboard.registerNewTeam(nextName);

        newTeam.setDisplayName(randomName ? nameGenerator.getRandomTeamDisplayName() : nextName);
        return newTeam;
    }

    @Override
    public Optional<Team> createTeam(String name, boolean randomName)
    {
        try {
            Team team  = scoreboard.registerNewTeam(name);
            team.setDisplayName(randomName ? nameGenerator.getRandomTeamDisplayName() : name);
            return Optional.of(team);
        } catch (Exception ex) {
            return Optional.absent();
        }
    }

    @Override
    public int deleteAllTeams()
    {
        Set<Team> allTeams = scoreboard.getTeams();

        for(Team team : allTeams) {
            deleteTeam(team);
        }

        return allTeams.size();
    }

    @Override
    public void deleteTeam(Team team)
    {
        Set<OfflinePlayer> players = team.getPlayers();

        for(OfflinePlayer player : players) {
            if(player.isOnline()) {
                translate.sendMessage("disbanded", (Player) player);
            }
        }

        team.unregister();
    }

    @Override
    public Optional<Team> getTeamByName(String name) {
        return Optional.fromNullable(scoreboard.getTeam(name));
    }

    @Override
    public void sendToTeam(Team team, String message)
    {
        for(OfflinePlayer player : team.getPlayers()) {
            if(player.isOnline()) {
                ((Player) player).sendMessage(message);
            }
        }
    }

    @Override
    public void sendToTeamTranslated(Team team, String key, Object... params)
    {
        for(OfflinePlayer player : team.getPlayers()) {
            if(player.isOnline()) {
                translate.sendMessage(key, (Player) player, params);
            }
        }
    }
}
