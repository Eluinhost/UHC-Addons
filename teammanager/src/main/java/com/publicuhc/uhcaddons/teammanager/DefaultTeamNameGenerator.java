/*
 * DefaultTeamNameGenerator.java
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
import com.publicuhc.ultrahardcore.framework.configuration.Configurator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Random;

public class DefaultTeamNameGenerator implements TeamNameGenerator {

    private final Scoreboard scoreboard;
    private final FileConfiguration config;
    private final Random random;

    protected DefaultTeamNameGenerator(Scoreboard scoreboard, Configurator configurator, Random random)
    {
        this.scoreboard = scoreboard;
        this.random = random;

        Optional<FileConfiguration> config = configurator.getConfig("teamnames");

        if(!config.isPresent()) {
            throw new IllegalStateException("Cannot find the teamnames.yml file, cannot generate names");
        }

        this.config = config.get();
    }

    @Override
    public String getNextAvailableTeamName() {
        int count = 0;
        while(true) {
            String teamName = config.getString("prefix") + count;

            Team thisteam = scoreboard.getTeam(teamName);
            if(thisteam == null) {
                return teamName;
            }

            count++;
        }
    }

    @Override
    public String getRandomTeamDisplayName() {
        List<String> adjectives = config.getStringList("adjectives");
        List<String> nouns = config.getStringList("nouns");

        String randomAdjective = adjectives.get(random.nextInt(adjectives.size()));
        String randomNoun = nouns.get(random.nextInt(nouns.size()));

        return "The " + randomAdjective + " " + randomNoun;
    }
}
