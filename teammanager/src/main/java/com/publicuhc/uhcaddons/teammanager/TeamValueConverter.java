package com.publicuhc.uhcaddons.teammanager;

import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.ValueConverter;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamValueConverter implements ValueConverter<Team>
{
    private final Scoreboard scoreboard;

    @Inject
    protected TeamValueConverter(Scoreboard scoreboard)
    {
        this.scoreboard = scoreboard;
    }

    @Override
    public Team convert(String s)
    {
        return scoreboard.getTeam(s);
    }

    @Override
    public Class<Team> valueType()
    {
        return Team.class;
    }

    @Override
    public String valuePattern()
    {
        return "Team Name";
    }
}
