package com.publicuhc.uhcaddons.teammanager;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.*;
import com.publicuhc.ultrahardcore.framework.routing.converters.OnlinePlayerValueConverter;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TeamCommands implements Command
{
    private final TeamsUtil m_teamsUtil;

    public static final String TEAM_CREATE_PERMISSION = "UHC.teams.create";
    public static final String TEAM_REMOVE_PERMISSION = "UHC.teams.remove";
    public static final String TEAM_LEAVE_PERMISSION = "UHC.teams.leave";
    public static final String TEAM_LEAVE_OTHER_PERMISSION = "UHC.teams.leave.other";
    public static final String TEAM_JOIN_PERMISSION = "UHC.teams.join";
    public static final String TEAM_JOIN_OTHER_PERMISSION = "UHC.teams.join.other";
    public static final String CLEAR_TEAMS_PERMISSION = "UHC.teams.clear";
    public static final String EMPTY_TEAMS_PERMISSION = "UHC.teams.empty";
    public static final String LIST_TEAMS_PERMISSION = "UHC.teams.list";
    public static final String RANDOM_TEAMS_PERMISSION = "UHC.teams.random";
    public static final String TEAMUP_PERMISSION = "UHC.teams.teamup";
    public static final String NOTEAM_PERMISSION = "UHC.teams.noteam";
    public static final String TEAM_PM_PERMISSION = "UHC.teams.pm";

    private final WordsUtil m_words;
    private final Translate translate;

    @Inject
    private TeamCommands(Translate translate, WordsUtil words, TeamsUtil teamsUtil)
    {
        m_words = words;
        m_teamsUtil = teamsUtil;
        this.translate = translate;
    }

    @CommandMethod("teamm create")
    @PermissionRestriction(TEAM_CREATE_PERMISSION)
    public void createTeamCommand(OptionSet set, CommandSender sender)
    {
        @SuppressWarnings("unchecked")
        List<String> arguments = (List<String>) set.nonOptionArguments();

        if(arguments.isEmpty()) {
            translate.sendMessage("supply one team name", sender);
            return;
        }

        Set<String> created = Sets.newHashSet();

        for(String teamName : arguments) {
            Team team = m_teamsUtil.getTeam(teamName);

            if(team != null) {
                translate.sendMessage("team exists", sender, teamName);
                continue;
            }

            team = m_teamsUtil.registerNewTeam(teamName);
            team.setDisplayName(m_words.getRandomTeamName());

            created.add(teamName);
        }

        translate.sendMessage("created", sender, Joiner.on(",").join(created));
    }

    @CommandMethod("teamm remove")
    @PermissionRestriction(TEAM_REMOVE_PERMISSION)
    public void removeTeamCommand(OptionSet set, CommandSender sender)
    {
        @SuppressWarnings("unchecked")
        List<String> arguments = (List<String>) set.nonOptionArguments();

        if(arguments.isEmpty()) {
            translate.sendMessage("supply one team name", sender);
            return;
        }

        Set<String> removed = Sets.newHashSet();

        for(String teamName : arguments) {
            Team team = m_teamsUtil.getTeam(teamName);

            if(team == null) {
                translate.sendMessage("team not exist", sender, teamName);
                continue;
            }

            for(OfflinePlayer p : team.getPlayers()) {
                Player p1 = p.getPlayer();
                if(p1 != null) {
                    translate.sendMessage("disbanded", p1);
                }
            }
            m_teamsUtil.removeTeam(teamName);
            removed.add(teamName);
        }
        translate.sendMessage("teams removed", sender, Joiner.on(",").join(removed));
    }

    @CommandMethod("teamm leaveself")
    @PermissionRestriction(TEAM_LEAVE_PERMISSION)
    @SenderRestriction(Player.class)
    public void leaveTeamCommand(OptionSet set, Player player)
    {
        boolean stillOnTeam = !m_teamsUtil.removePlayerFromTeam(player, true, true);
        if(stillOnTeam) {
            translate.sendMessage("not in team", player);
        }
    }

    @CommandMethod("teamm leave")
    @CommandOptions("[arguments]")
    public void leaveTeamForce(OptionSet set, CommandSender sender, List<Player[]> args)
    {
        Set<Player> players = OnlinePlayerValueConverter.recombinePlayerLists(args);

        for(Player player : players) {
            boolean stillOnTeam = !m_teamsUtil.removePlayerFromTeam(player, true, true);
            if(stillOnTeam) {
                translate.sendMessage("player not in team", sender, player);
            }
        }

        translate.sendMessage("removed from team", sender);
    }

    @OptionsMethod
    public void leaveTeamForce(OptionDeclarer declarer)
    {
        declarer.nonOptions().withValuesConvertedBy(new OnlinePlayerValueConverter(true));
    }

    @CommandMethod("teamm join")
    public void joinTeamCommand(CommandRequest request)
    {
        OfflinePlayer sender = (OfflinePlayer) request.getSender();
        Team team = m_teamsUtil.getTeam(request.getFirstArg());
        if(team == null) {
            request.sendMessage(translate("teams.not_exist", request.getLocale()));
            return;
        }
        if(m_teamsUtil.getPlayersTeam(sender) != null) {
            m_teamsUtil.removePlayerFromTeam(sender, true, true);
        }
        TeamsUtil.playerJoinTeam(sender, team, true, true);
    }

    @RouteInfo
    public void joinTeamCommandDetails(RouteBuilder builder)
    {
        builder.restrictSenderType(SenderType.PLAYER)
                .restrictPermission(TEAM_JOIN_PERMISSION)
                .restrictArgumentCount(1, 1)
                .restrictCommand("jointeam");
    }

    /**
     * Ran on /jointeam {team} {name}
     *
     * @param request request params
     */
    @CommandMethod
    public void joinTeamOtherCommand(CommandRequest request)
    {
        Team team = m_teamsUtil.getTeam(request.getFirstArg());
        if(team == null) {
            request.sendMessage(translate("teams.not_exist", request.getLocale()));
            return;
        }
        UUID playerID = request.getPlayerUUID(1);
        if(playerID.equals(CommandRequest.INVALID_ID)) {
            request.sendMessage(translate("teams.invalid_player", request.getLocale()));
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerID);
        if(m_teamsUtil.getPlayersTeam(player) != null) {
            m_teamsUtil.removePlayerFromTeam(player, true, true);
        }
        TeamsUtil.playerJoinTeam(player, team, true, true);
    }

    @RouteInfo
    public void joinTeamOtherCommandDetails(RouteBuilder builder)
    {
        builder.restrictPermission(TEAM_JOIN_OTHER_PERMISSION)
                .restrictArgumentCount(2, 2)
                .restrictCommand("jointeam");
    }

    /**
     * Ran on /clearteams
     *
     * @param request request params
     */
    @CommandMethod
    public void clearTeamsCommand(CommandRequest request)
    {
        m_teamsUtil.clearTeams(true);
        request.sendMessage(translate("teams.cleared", request.getLocale()));
    }

    @RouteInfo
    public void clearTeamsCommandDetails(RouteBuilder builder)
    {
        builder.restrictPermission(CLEAR_TEAMS_PERMISSION)
                .restrictCommand("clearteams");
    }

    /**
     * Ran on /emptyteams
     *
     * @param request request params
     */
    @CommandMethod
    public void emptyTeamsCommand(CommandRequest request)
    {
        m_teamsUtil.emptyTeams(true);
        request.sendMessage(translate("teams.emptied", request.getLocale()));
    }

    @RouteInfo
    public void emptyTeamsCommandDetails(RouteBuilder builder)
    {
        builder.restrictPermission(EMPTY_TEAMS_PERMISSION)
                .restrictCommand("emptyteams");
    }

    /**
     * Ran on /listteams {name}
     *
     * @param request request params
     */
    @CommandMethod
    public void listTeamsCommand(CommandRequest request)
    {
        if(request.getArgs().isEmpty()) {
            listTeamsAllCommand(request);
            return;
        }
        Team team = m_teamsUtil.getTeam(request.getFirstArg());
        if(team == null) {
            request.sendMessage(translate("teams.not_exist", request.getLocale()));
            return;
        }
        request.sendMessage(TeamsUtil.teamToString(team));
    }

    @RouteInfo
    public void listTeamsCommandDetails(RouteBuilder builder)
    {
        builder.restrictPermission(LIST_TEAMS_PERMISSION)
                .restrictCommand("listteams")
                .maxMatches(1);
    }

    /**
     * Ran on /listteam *
     *
     * @param request request params
     */
    @CommandMethod
    public void listTeamsAllCommand(CommandRequest request)
    {
        Set<Team> teams = m_teamsUtil.getAllTeams();
        if(teams.isEmpty()) {
            request.sendMessage(translate("teams.no_teams", request.getLocale()));
            return;
        }
        for(Team team : teams) {
            request.sendMessage(TeamsUtil.teamToString(team));
        }
    }

    @RouteInfo
    public void listTeamsAllCommandDetails(RouteBuilder builder)
    {
        builder.restrictPermission(LIST_TEAMS_PERMISSION)
                .restrictStartsWith("*")
                .restrictCommand("listteams");
    }

    /**
     * Ran on /randomteams {number} [world]
     *
     * @param request request params
     */
    @CommandMethod
    public void randomTeamCommand(CommandRequest request)
    {
        List<Player> players = new ArrayList<Player>();
        if(request.getArgs().size() == 2) {
            World world = Bukkit.getWorld(request.getLastArg());
            if(world == null) {
                request.sendMessage(translate("teams.invalid_world", request.getLocale()));
                return;
            }
            players.addAll(world.getPlayers());
        } else {
            players.addAll(Bukkit.getOnlinePlayers());
        }
        Collections.shuffle(players);
        m_teamsUtil.removePlayersInATeam(players);

        int teamsToMake;
        try {
            teamsToMake = Integer.parseInt(request.getFirstArg());
        } catch(NumberFormatException ignored) {
            request.sendMessage(translate("teams.invalid_number_teams", request.getLocale()));
            return;
        }

        if(teamsToMake > players.size()) {
            request.sendMessage(translate("teams.too_many_teams", request.getLocale()));
            return;
        }

        if(teamsToMake <= 0) {
            request.sendMessage(translate("teams.greater_zero", request.getLocale()));
            return;
        }

        List<List<Player>> teams = MathsHelper.split(players, teamsToMake);
        Collection<Team> finalTeams = new ArrayList<Team>();
        for(List<Player> team : teams) {
            if(team == null) {
                continue;
            }
            Team thisteam = m_teamsUtil.getNextAvailableTeam(false);
            finalTeams.add(thisteam);
            for(Player p : team) {
                if(p != null) {
                    TeamsUtil.playerJoinTeam(p, thisteam, true, false);
                }
            }
        }
        for(Team t : finalTeams) {
            Set<OfflinePlayer> teamPlayers = t.getPlayers();
            //TODO translatable
            StringBuilder buffer = new StringBuilder(String.valueOf(ChatColor.GOLD))
                    .append("Your Team (")
                    .append(t.getName())
                    .append(" - ")
                    .append(t.getDisplayName())
                    .append("): ");
            for(OfflinePlayer p : teamPlayers) {
                buffer.append(p.getName()).append(", ");
            }
            buffer.delete(buffer.length() - 2, buffer.length());
            String finalString = buffer.toString();
            for(OfflinePlayer p : teamPlayers) {
                if(p.isOnline()) {
                    Player p2 = Bukkit.getPlayer(p.getUniqueId());
                    p2.sendMessage(finalString);
                }
            }
        }
        request.sendMessage(translate("teams.created_teams", request.getLocale(), "amount", String.valueOf(teamsToMake)));
    }

    @RouteInfo
    public void randomTeamCommandDetails(RouteBuilder builder)
    {
        builder.restrictPermission(RANDOM_TEAMS_PERMISSION)
                .restrictArgumentCount(1, 2)
                .restrictCommand("randomteams");
    }

    @CommandMethod
    public void teamupCommand(CommandRequest request)
    {
        int count = request.getArgs().size();

        Collection<String> notFound = new ArrayList<String>();
        Collection<Player> players = new ArrayList<Player>();

        //get the online players first
        for(int i = 0; i < count; i++) {
            Player player = request.getPlayer(i);
            if(null == player) {
                notFound.add(request.getArg(i));
            }
            players.add(player);
        }

        //get the offline player's UUIDs
        Collection<UUID> uuids = new ArrayList<UUID>();

        if(!notFound.isEmpty()) {
            Map<String, UUID> fetched = new HashMap<String, UUID>();

            UUIDFetcher fetcher = new UUIDFetcher(notFound.toArray(new String[notFound.size()]));
            try {
                fetched = fetcher.call();
            } catch(IOException e) {
                e.printStackTrace();
            } catch(ParseException e) {
                e.printStackTrace();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

            for(String current : notFound) {
                UUID uu = fetched.get(current);
                if(null == uu) {
                    request.sendMessage(translate("teams.player_not_found", request.getLocale(), "name", current));
                    continue;
                }
                uuids.add(uu);
            }
        }

        Team team = m_teamsUtil.getNextAvailableTeam(false);
        Map<String, String> context = new HashMap<String, String>();
        context.put("display", team.getDisplayName());
        context.put("name", team.getName());

        for(Player p : players) {
            team.addPlayer(p);
            p.sendMessage(translate("teams.joined_notification", getTranslator().getLocaleForSender(p), context));
        }

        for(UUID uuid : uuids) {
            team.addPlayer(Bukkit.getOfflinePlayer(uuid));
        }

        request.sendMessage(translate("teams.created", request.getLocale(), context));
    }

    @RouteInfo
    public void teamupCommandDetails(RouteBuilder builder)
    {
        builder.restrictPermission(TEAMUP_PERMISSION)
                .restrictArgumentCount(1, -1)
                .restrictCommand("teamup");
    }

    @CommandMethod
    public void noteamCommand(CommandRequest request)
    {
        int found = 0;
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(m_teamsUtil.getPlayersTeam(p) == null) {
                found++;
                request.sendMessage(ChatColor.GRAY + p.getName());
            }
        }
        if(found == 0) {
            request.sendMessage(translate("teams.all_in_team", request.getLocale()));
        }
    }

    @RouteInfo
    public void noteamCommandDetails(RouteBuilder builder)
    {
        builder.restrictPermission(NOTEAM_PERMISSION)
                .restrictCommand("noteam");
    }

    @CommandMethod
    public void teamMessageCommand(CommandRequest request)
    {
        Player player = (Player) request.getSender();
        Team team = m_teamsUtil.getPlayersTeam(player);

        if(null == team) {
            request.sendMessage(translate("teams.not_in_team", request.getLocale()));
            return;
        }

        StringBuilder builder = new StringBuilder();
        for(String part : request.getArgs()) {
            builder.append(part).append(" ");
        }
        String message = builder.toString();

        Map<String, String> context = new HashMap<String, String>();
        context.put("message", message);
        context.put("sender", player.getName());

        for(OfflinePlayer member : team.getPlayers()) {
            if(member.isOnline()) {
                Player onlinePlayer = (Player) member;
                onlinePlayer.sendMessage(translate("teams.pm_message", request.getLocale(), context));
            }
        }
    }

    @RouteInfo
    public void teamMessageCommandDetails(RouteBuilder builder)
    {
        builder.restrictPermission(TEAM_PM_PERMISSION)
                .restrictSenderType(SenderType.PLAYER)
                .restrictCommand("pmteam");
    }
}