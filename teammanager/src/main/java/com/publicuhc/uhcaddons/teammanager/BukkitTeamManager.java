package com.publicuhc.uhcaddons.teammanager;

import com.google.common.base.Optional;
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
}
