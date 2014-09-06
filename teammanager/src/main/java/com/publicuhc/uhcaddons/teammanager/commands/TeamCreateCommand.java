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

public class TeamCreateCommand implements Command {

    public static final String TEAM_CREATE_PERMISSION = "UHC.teams.create";

    private final Scoreboard scoreboard;
    private final TeamManager teamManager;
    private final Translate translate;

    @Inject
    protected TeamCreateCommand(Scoreboard scoreboard, TeamManager teamManager, Translate translate)
    {
        this.scoreboard = scoreboard;
        this.teamManager = teamManager;
        this.translate = translate;
    }

    @CommandMethod("teamm create")
    @CommandOptions({"[arguments]", "n"})
    @PermissionRestriction(TEAM_CREATE_PERMISSION)
    public void createTeam(OptionSet set, CommandSender sender, List<String> nonOptions, Integer n)
    {
        if(set.has("n")) {
            if(n <= 0) {
                translate.sendMessage("greater than zero", sender);
                return;
            }

            for(int i = 0; i < n; i++) {
                teamManager.getNewTeam(set.has("r"));
            }

            translate.sendMessage("created", sender, n);
            return;
        }

        List<String> invalid = Lists.newArrayList();

        for(String name : nonOptions) {
            Optional<Team> team = teamManager.createTeam(name, set.has("r"));

            if(!team.isPresent()) {
                invalid.add(name);
            }
        }

        translate.sendMessage("created", sender, n);

        if(!invalid.isEmpty()) {
            translate.sendMessage("failed to create", sender, Joiner.on(",").join(invalid));
        }
    }

    @OptionsMethod
    public void createTeam(OptionDeclarer declarer)
    {
        declarer.accepts("r", "Generate random display names");
        declarer.accepts("n", "Number of auto-named teams to create").withRequiredArg().ofType(Integer.class);
    }
}
