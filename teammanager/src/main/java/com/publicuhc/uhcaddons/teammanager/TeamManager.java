/*
 * TeamManager.java
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
import org.bukkit.scoreboard.Team;

public interface TeamManager
{

    /**
     * @param randomName if true gives a random name from the TeamNameGenerator, if false uses the team name
     * @return the generated team
     */
    Team getNewTeam(boolean randomName);

    /**
     * Create a team with the given name
     *
     * @param name       the name to use
     * @param randomName whether to give it a random display name or not
     * @return the created team if created
     */
    Optional<Team> createTeam(String name, boolean randomName);

    /**
     * Removes all registered teams from the scoreboard and tells the players in the team about it's deletion
     *
     * @return the amount of teams deleted
     */
    int deleteAllTeams();

    /**
     * Deletes the given team and tells all online players in the team about it
     *
     * @param team the team to delete
     */
    void deleteTeam(Team team);

    /**
     * Get the team with the given name
     *
     * @param name the name of the team
     * @return the team if it exists
     */
    Optional<Team> getTeamByName(String name);

    /**
     * Send a message to all online players in the given team
     *
     * @param message the message to send
     * @param team    the team to send to
     */
    void sendToTeam(Team team, String message);

    /**
     * Sends a message to all online players in the given team translated
     *
     * @param team   the team to send to
     * @param key    the key to look up
     * @param params the replacement vars for the translation
     */
    void sendToTeamTranslated(Team team, String key, Object... params);
}
