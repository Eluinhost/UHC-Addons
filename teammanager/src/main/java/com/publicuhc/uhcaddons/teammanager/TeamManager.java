package com.publicuhc.uhcaddons.teammanager;

import org.bukkit.scoreboard.Team;

public interface TeamManager {

    /**
     * @param randomName if true gives a random name from the TeamNameGenerator, if false uses the team name
     * @return the generated team
     */
    Team getNewTeam(boolean randomName);
}
