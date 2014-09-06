package com.publicuhc.uhcaddons.teammanager;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class BukkitTeamManager implements TeamManager {

    private final Scoreboard scoreboard;
    private final TeamNameGenerator nameGenerator;

    public BukkitTeamManager(Scoreboard scoreboard, TeamNameGenerator nameGenerator)
    {
        this.scoreboard = scoreboard;
        this.nameGenerator = nameGenerator;
    }

    @Override
    public Team getNewTeam()
    {
        Team newTeam = scoreboard.registerNewTeam(nameGenerator.getNextAvailableTeamName());
        newTeam.setDisplayName(nameGenerator.getRandomTeamDisplayName());
        return newTeam;
    }
}
