package com.publicuhc.uhcaddons.teammanager;

public interface TeamNameGenerator {

    /**
     * @return name of the next available team (i.e. the team doesn't exist
     */
    String getNextAvailableTeamName();

    /**
     * @return a random team display name
     */
    String getRandomTeamDisplayName();
}
