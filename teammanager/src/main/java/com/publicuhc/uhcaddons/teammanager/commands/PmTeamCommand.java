package com.publicuhc.uhcaddons.teammanager.commands;

import com.google.common.base.Joiner;
import com.publicuhc.uhcaddons.teammanager.TeamManager;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.*;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class PmTeamCommand implements Command
{
    private final Translate translate;
    private final TeamManager manager;
    private final Scoreboard scoreboard;

    public static final String PM_TEAM_PERMISSION = "UHC.teams.pm";

    @Inject
    protected PmTeamCommand(Translate translate, Scoreboard scoreboard, TeamManager manager)
    {
        this.translate = translate;
        this.manager = manager;
        this.scoreboard = scoreboard;
    }

    @CommandMethod("pmt")
    @PermissionRestriction(PM_TEAM_PERMISSION)
    @CommandOptions("[arguments]")
    @SenderRestriction(Player.class)
    public void pmTeam(OptionSet set, Player player, List<String> message)
    {
        Team team = scoreboard.getPlayerTeam(player);

        if(null == team) {
            translate.sendMessage("not in team", player);
            return;
        }

        manager.sendToTeamTranslated(team, "pm", player.getName(), Joiner.on(" ").join(message));
    }

    @OptionsMethod
    public void pmTeam(OptionDeclarer declarer)
    {
        declarer.nonOptions().describedAs("Message to send");
    }

}
