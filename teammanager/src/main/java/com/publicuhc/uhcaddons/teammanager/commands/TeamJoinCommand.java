package com.publicuhc.uhcaddons.teammanager.commands;

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

public class TeamJoinCommand implements Command
{
    private Translate translate;
    private TeamManager manager;
    private Scoreboard scoreboard;

    public static final String TEAM_JOIN_PERMISSION = "UHC.teams.join";

    @Inject
    protected TeamJoinCommand(Translate translate, Scoreboard scoreboard, TeamManager manager)
    {
        this.translate = translate;
        this.scoreboard = scoreboard;
        this.manager = manager;
    }

    @CommandMethod("teamm join")
    @PermissionRestriction(TEAM_JOIN_PERMISSION)
    @SenderRestriction(Player.class)
    @CommandOptions("[arguments]")
    public void joinTeam(OptionSet set, Player player, List<String> teams)
    {
        for(String team : teams) {
            Team t = scoreboard.getTeam(team);

            if(null == t) {
                continue;
            }

            translate.sendMessage("joined team", player, team);
            manager.sendToTeamTranslated(t, "player joined", player.getName());
            t.addPlayer(player);
            return;
        }

        translate.sendMessage("supply one team name", player);
    }

    @OptionsMethod
    public void joinTeam(OptionDeclarer declarer)
    {
        declarer.nonOptions().describedAs("Team to join");
    }
}
