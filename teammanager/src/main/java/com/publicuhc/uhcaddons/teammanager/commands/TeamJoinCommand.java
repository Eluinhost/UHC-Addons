package com.publicuhc.uhcaddons.teammanager.commands;

import com.publicuhc.uhcaddons.teammanager.TeamManager;
import com.publicuhc.uhcaddons.teammanager.TeamValueConverter;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.*;
import com.publicuhc.ultrahardcore.framework.routing.converters.OnlinePlayerValueConverter;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Set;

public class TeamJoinCommand implements Command
{
    private Translate translate;
    private TeamManager manager;
    private TeamValueConverter teamValueConverter;
    private Scoreboard scoreboard;

    public static final String TEAM_JOIN_PERMISSION = "UHC.teams.join";
    public static final String TEAM_ADD_PERMISSION = "UHC.teams.add";

    @Inject
    protected TeamJoinCommand(Translate translate, Scoreboard scoreboard, TeamManager manager, TeamValueConverter teamValueConverter)
    {
        this.translate = translate;
        this.scoreboard = scoreboard;
        this.manager = manager;
        this.teamValueConverter = teamValueConverter;
    }

    private void addPlayerToTeam(Player player, Team team)
    {
        Team existing = scoreboard.getPlayerTeam(player);
        if(existing != null) {
            existing.removePlayer(player);
            translate.sendMessage("removed from team", player);
            manager.sendToTeamTranslated(existing, "player left", player.getName());
        }

        translate.sendMessage("joined team", player, team);
        manager.sendToTeamTranslated(team, "player joined", player.getName());
        team.addPlayer(player);
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

            addPlayerToTeam(player, t);
            return;
        }

        translate.sendMessage("supply one team name", player);
    }

    @OptionsMethod
    public void joinTeam(OptionDeclarer declarer)
    {
        declarer.nonOptions().describedAs("Team to join");
    }

    @CommandMethod("teamm add")
    @PermissionRestriction(TEAM_ADD_PERMISSION)
    @CommandOptions({"t", "[arguments]"})
    public void addToTeam(OptionSet set, CommandSender sender, Team team, List<Player[]> args)
    {
        Set<Player> players = OnlinePlayerValueConverter.recombinePlayerLists(args);

        if(players.isEmpty()) {
            translate.sendMessage("supply one player name", sender);
            return;
        }

        for(Player player : players) {
            addPlayerToTeam(player, team);
        }

        translate.sendMessage("added players", sender, players.size());
    }

    @OptionsMethod
    public void addToTeam(OptionDeclarer declarer)
    {
        declarer.nonOptions()
                .withValuesConvertedBy(new OnlinePlayerValueConverter(true))
                .describedAs("Player names to add to the team");
        declarer.accepts("t")
                .withRequiredArg()
                .required()
                .describedAs("Team to add players to")
                .withValuesConvertedBy(teamValueConverter);
    }
}
