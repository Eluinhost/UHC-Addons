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
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class TeamDeleteCommand implements Command {

    public static final String TEAM_DELETE_PERMISSION = "UHC.teams.delete";

    private final TeamManager teamManager;
    private final Translate translate;

    @Inject
    protected TeamDeleteCommand(Scoreboard scoreboard, TeamManager teamManager, Translate translate)
    {
        this.teamManager = teamManager;
        this.translate = translate;
    }

    @CommandMethod("teamm delete")
    @CommandOptions("[arguments]")
    @PermissionRestriction(TEAM_DELETE_PERMISSION)
    public void deleteTeam(OptionSet set, CommandSender sender, List<String> nonOptions)
    {
        if(set.has("a")) {
            int count = teamManager.deleteAllTeams();
            translate.sendMessage("deleted teams", sender, count);
            return;
        }

        if(nonOptions.isEmpty()) {
            translate.sendMessage("supply one team name", sender);
            return;
        }

        List<String> invalid = Lists.newArrayList();

        for(String teamName : nonOptions) {
            Optional<Team> team = teamManager.getTeamByName(teamName);

            if(!team.isPresent()) {
                invalid.add(teamName);
                continue;
            }

            teamManager.deleteTeam(team.get());
        }

        translate.sendMessage("deleted teams", sender, nonOptions.size() - invalid.size());

        if(!invalid.isEmpty()) {
            translate.sendMessage("unknown teams", sender, Joiner.on(",").join(invalid));
        }
    }

    @OptionsMethod
    public void deleteTeam(OptionDeclarer declarer)
    {
        declarer.accepts("a", "Delete all teams").withRequiredArg().ofType(Integer.class);
    }
}
