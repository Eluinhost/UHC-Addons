package com.publicuhc.uhcaddons.teammanager;

import com.publicuhc.ultrahardcore.framework.shaded.inject.AbstractModule;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class TeamManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TeamManager.class).to(BukkitTeamManager.class);
        bind(Scoreboard.class).toInstance(Bukkit.getScoreboardManager().getMainScoreboard());
    }
}
