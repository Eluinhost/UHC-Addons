package com.publicuhc.uhcaddons.teammanager;

import org.bukkit.scoreboard.Team;

public interface TeamManager {

    /**
     * @return a new team using whatever naming scheme is implemented
     */
    Team getNewTeam();
}
