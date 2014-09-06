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
