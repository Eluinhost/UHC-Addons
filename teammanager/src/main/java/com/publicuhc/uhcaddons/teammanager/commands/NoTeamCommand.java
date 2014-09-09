package com.publicuhc.uhcaddons.teammanager.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.PermissionRestriction;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.List;

public class NoTeamCommand implements Command
{
    private final Translate translate;
    private final Scoreboard scoreboard;

    public static final String NO_TEAM_PERMISSION = "UHC.teams.noteam";

    protected NoTeamCommand(Translate translate, Scoreboard scoreboard)
    {
        this.translate = translate;
        this.scoreboard = scoreboard;
    }

    @CommandMethod("noteam")
    @PermissionRestriction(NO_TEAM_PERMISSION)
    public void noteam(OptionSet set, CommandSender sender)
    {
        Player[] online = Bukkit.getOnlinePlayers();

        List<Player> noteam = Lists.newArrayList();

        for(Player player : online) {
            if(scoreboard.getPlayerTeam(player) == null) {
                noteam.add(player);
            }
        }

        if(noteam.isEmpty()) {
            translate.sendMessage("all in teams", sender);
            return;
        }

        Collection<String> playerNames = Lists.newArrayList();

        for(Player p : noteam) {
            playerNames.add(p.getName());
        }

        translate.sendMessage("no team", sender, Joiner.on(",").join(playerNames));
    }
}
