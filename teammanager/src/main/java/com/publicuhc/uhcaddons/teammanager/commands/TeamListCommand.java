package com.publicuhc.uhcaddons.teammanager.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandOptions;
import com.publicuhc.ultrahardcore.framework.routing.annotation.OptionsMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.PermissionRestriction;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class TeamListCommand implements Command
{
    private Translate translate;
    private Scoreboard scoreboard;

    public static final String TEAM_LIST_PERMISSION = "UHC.teams.list";

    protected TeamListCommand(Translate translate, Scoreboard scoreboard)
    {
        this.translate = translate;
        this.scoreboard = scoreboard;
    }

    @CommandMethod("teamm list")
    @PermissionRestriction(TEAM_LIST_PERMISSION)
    @CommandOptions
    public void onListCommand(OptionSet set, CommandSender sender)
    {
        Set<Team> teams = scoreboard.getTeams();

        if(!set.has("a")) {
            Iterator<Team> i = teams.iterator();
            while(i.hasNext()) {
                if(i.next().getPlayers().isEmpty()) {
                    i.remove();
                }
            }
        }

        if(teams.isEmpty()) {
            translate.sendMessage("no teams", sender);
            return;
        }

        translate.sendMessage("teams header", sender, teams.size());

        for(Team team : teams) {
            Set<OfflinePlayer> players = team.getPlayers();

            Collection<String> playerNames = Lists.newArrayList();

            for(OfflinePlayer p : players) {
                playerNames.add(p.getName());
            }

            translate.sendMessage("teams row", sender, team.getDisplayName(), team.getName(), Joiner.on(",").skipNulls().join(playerNames));
        }
    }

    @OptionsMethod
    public void onListCommand(OptionDeclarer declarer)
    {
        declarer.accepts("a", "Show all teams (including empty)");
    }
}
