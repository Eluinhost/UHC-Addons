package com.publicuhc.uhcaddons.teammanager.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.publicuhc.uhcaddons.teammanager.TeamManager;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandOptions;
import com.publicuhc.ultrahardcore.framework.routing.annotation.OptionsMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.PermissionRestriction;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomTeamsCommand implements Command
{
    private Translate translate;
    private TeamManager manager;
    private Scoreboard scoreboard;

    public static final String RANDOM_TEAMS_PERMISSION = "UHC.teams.random";

    @Inject
    protected RandomTeamsCommand(Translate translate, TeamManager manager, Scoreboard scoreboard)
    {
        this.translate = translate;
        this.manager = manager;
        this.scoreboard = scoreboard;
    }

    /**
     * Splits the list into even sized lists
     *
     * @param list the list to split
     * @param count the number of lists to make
     * @param <T> type
     * @return a list of the split lists
     */
    public static <T> List<List<T>> split(List<T> list, int count) {
        Validate.isTrue(list.size() >= count, "List must be >= to the amount of requested lists");

        int amountPerList = (int) Math.ceil((double) list.size() / (double) count);

        return Lists.partition(list, amountPerList);
    }

    @CommandMethod("randomteams")
    @PermissionRestriction(RANDOM_TEAMS_PERMISSION)
    @CommandOptions("n")
    public void onRandomTeams(OptionSet set, CommandSender sender, Integer amount)
    {
        if(amount < 1) {
            translate.sendMessage("greater than zero", sender);
            return;
        }

        List<Player> players = Lists.newArrayList(Bukkit.getOnlinePlayers());

        Collections.shuffle(players);

        Iterator<Player> playerIterator = players.iterator();
        while(playerIterator.hasNext()) {
            if(scoreboard.getPlayerTeam(playerIterator.next()) != null) {
                playerIterator.remove();
            }
        }

        if(amount <= players.size()) {
            translate.sendMessage("not enough players online", sender, players.size());
            return;
        }

        List<List<Player>> splitup = split(players, amount);

        for(List<Player> teamGroup : splitup) {
            Team newTeam = manager.getNewTeam(true);

            Collection<String> playerNames = Lists.newArrayList();

            for(OfflinePlayer p : teamGroup) {
                playerNames.add(p.getName());
            }

            for(Player p : teamGroup) {
                newTeam.addPlayer(p);
                translate.sendMessage("your team", sender, Joiner.on(",").join(playerNames));
            }
        }

        translate.sendMessage("created", sender, splitup.size());
    }

    @OptionsMethod
    public void onRandomTeams(OptionDeclarer declarer)
    {
        declarer.accepts("n")
                .withRequiredArg()
                .ofType(Integer.class)
                .describedAs("Amount of teams to create")
                .defaultsTo(4);
    }
}
