package com.publicuhc.uhcaddons.teammanager.commands;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.publicuhc.uhcaddons.teammanager.TeamManager;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandOptions;
import com.publicuhc.ultrahardcore.framework.routing.annotation.OptionsMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.PermissionRestriction;
import com.publicuhc.ultrahardcore.framework.routing.converters.OnlinePlayerValueConverter;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TeamupCommand implements Command
{
    private final Translate translate;
    private final Scoreboard scoreboard;
    private final TeamManager manager;

    public static final String TEAMUP_PERMISSION = "UHC.teams.teamup";

    @Inject
    protected TeamupCommand(Translate translate, Scoreboard scoreboard, TeamManager manager)
    {
        this.translate = translate;
        this.scoreboard = scoreboard;
        this.manager = manager;
    }

    @CommandMethod("teamup")
    @PermissionRestriction(TEAMUP_PERMISSION)
    @CommandOptions({"[arguments]", "n", "t"})
    public void onteamup(OptionSet set, CommandSender sender, List<Player[]> args, String display, String name)
    {
        boolean randomNick = !set.has("n");
        boolean randomTeam = !set.has("t");

        Team team;
        if(randomTeam) {
            team = manager.getNewTeam(randomNick);
            if(!randomNick) {
                team.setDisplayName(display);
            }
        } else {
            Optional<Team> optionalTeam = manager.getTeamByName(name);

            if(!optionalTeam.isPresent()) {
                translate.sendMessage("team not found", sender, name);
                return;
            }

            team = optionalTeam.get();
        }

        Set<Player> players = OnlinePlayerValueConverter.recombinePlayerLists(args);

        if(players.isEmpty()) {
            translate.sendMessage("supply one player name", sender);
            return;
        }

        for(Player p : players) {
            team.addPlayer(p);
        }

        Collection<String> playerNames = Lists.newArrayList();

        for(OfflinePlayer p : team.getPlayers()) {
            playerNames.add(p.getName());
        }

        manager.sendToTeamTranslated(team, "your team", Joiner.on(",").join(playerNames));
        translate.sendMessage("teamed up", sender, players.size());
    }

    @OptionsMethod
    public void onteamup(OptionDeclarer declarer)
    {
        declarer.accepts("n")
                .requiredUnless("t")
                .withRequiredArg()
                .describedAs("Display name for the team");
        declarer.accepts("t")
                .withRequiredArg()
                .describedAs("Name for the team");
    }
}
