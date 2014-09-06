package com.publicuhc.uhcaddons.teammanager;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class BukkitTeamManager implements TeamManager {

    private final Scoreboard scoreboard;

    public BukkitTeamManager(Scoreboard scoreboard)
    {
        this.scoreboard = scoreboard;
    }

    @Override
    public Team getNewTeam() {
        //TODO
        return null;
    }
}
