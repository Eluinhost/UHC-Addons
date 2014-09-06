package com.publicuhc.uhcaddons.teammanager;

import com.google.common.base.Optional;
import org.bukkit.scoreboard.Team;

public interface TeamManager {

    /**
     * @param randomName if true gives a random name from the TeamNameGenerator, if false uses the team name
     * @return the generated team
     */
    Team getNewTeam(boolean randomName);

    /**
     * Create a team with the given name
     *
     * @param name the name to use
     * @param randomName whether to give it a random display name or not
     * @return the created team if created
     */
    Optional<Team> createTeam(String name, boolean randomName);
}
