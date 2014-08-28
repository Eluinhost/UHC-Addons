package com.publicuhc.uhcaddons.scatter;

import com.publicuhc.scatterlib.DefaultScatterer;
import com.publicuhc.scatterlib.Scatterer;
import com.publicuhc.scatterlib.exceptions.ScatterLocationException;
import com.publicuhc.scatterlib.logic.StandardScatterLogic;
import com.publicuhc.scatterlib.zones.CircularDeadZoneBuilder;
import com.publicuhc.scatterlib.zones.DeadZone;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.configuration.Configurator;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandOptions;
import com.publicuhc.ultrahardcore.framework.routing.annotation.OptionsMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.PermissionRestriction;
import com.publicuhc.ultrahardcore.framework.routing.converters.LocationValueConverter;
import com.publicuhc.ultrahardcore.framework.routing.converters.OnlinePlayerValueConverter;
import com.publicuhc.ultrahardcore.framework.shaded.javax.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionParser;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class ScatterCommand implements Command {

    private final List<Material> mats = new ArrayList<Material>();
    private final int maxAttempts;

    @Inject
    public ScatterCommand(Configurator configurator, PluginLogger logger)
    {
        FileConfiguration config = configurator.getConfig("main");
        List<String> stringMats = config.getStringList("allowed blocks");
        for(String stringMat : stringMats) {
            Material mat = Material.matchMaterial(stringMat);
            if(null == mat)
                logger.severe("Unknown material " + stringMat);
            else
                mats.add(mat);
        }
        maxAttempts = config.getInt("max attempts per location");
    }

    @CommandMethod("sct")
    @PermissionRestriction("UHC.scatter.command")
    @CommandOptions({"t", "r", "minradius", "min", "c", "[arguments]"})
    public void onScatterCommand(OptionSet set, CommandSender sender, StandardScatterLogic logic, Double r, Double minradius, Double min, Location centre, List<Player[]> args)
    {
        boolean asTeams = set.has("teams");

        Set<Player> toScatter = OnlinePlayerValueConverter.recombinePlayerLists(args);

        logic.setCentre(centre);
        logic.setMaxAttempts(maxAttempts);
        logic.setRadius(r);

        if(!mats.isEmpty())
            logic.setMaterials(mats);

        CircularDeadZoneBuilder deadZoneBuilder = new CircularDeadZoneBuilder(minradius);

        List<DeadZone> baseDeadZones = new ArrayList<DeadZone>();
        if(min > 0) {
            //add dead zones for all not scattered players
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!toScatter.contains(player)) {
                    baseDeadZones.add(deadZoneBuilder.buildForLocation(player.getLocation()));
                }
            }
        }

        if(minradius > 0) {
            CircularDeadZoneBuilder builder = new CircularDeadZoneBuilder(minradius);
            baseDeadZones.add(builder.buildForLocation(centre));
        }

        Scatterer scatterer = new DefaultScatterer(logic, baseDeadZones, deadZoneBuilder);

        HashMap<String, List<Player>> teams = new HashMap<String, List<Player>>();
        if(asTeams) {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            Iterator<Player> players = toScatter.iterator();
            while(players.hasNext()) {
                Player player = players.next();
                Team team = scoreboard.getPlayerTeam(player);
                if(null != team) {
                    List<Player> playerList = teams.get(team.getName());
                    if(null == playerList) {
                        playerList = new ArrayList<Player>();
                        teams.put(team.getName(), playerList);
                    }
                    playerList.add(player);
                    players.remove();
                }
            }
        }

        int amount = toScatter.size() + teams.size();
        List<Location> locations;
        try {
            locations = scatterer.getScatterLocations(amount);
        } catch (ScatterLocationException e) {
            sender.sendMessage(ChatColor.RED + "Couldn't find valid locations for all players");
            return;
        }

        Collections.shuffle(locations);
        Bukkit.broadcastMessage(ChatColor.GOLD + "Starting scatter of " + amount + " players/teams");

        Iterator<Location> locationIterator = locations.iterator();
        int count = 1;

        for(Map.Entry<String, List<Player>> team : teams.entrySet()) {
            Location location = locationIterator.next();
            location.add(0, 1, 0);

            for(Player p : team.getValue()) {
                p.teleport(location);
            }

            Bukkit.broadcastMessage(ChatColor.GREEN + "[" + count++ + "/" + amount + "] Team " + team.getKey() + " scattered");
        }

        for(Player p : toScatter) {
            Location location = locationIterator.next();
            location.add(0, 1, 0);

            p.teleport(location);
            Bukkit.broadcastMessage(ChatColor.GREEN + "[" + count++ + "/" + amount + "] " + p.getName() + " scattered");
        }

        Bukkit.broadcastMessage(ChatColor.GOLD + "Scatter complete");
    }

    @OptionsMethod
    public void onScatterCommand(OptionParser parser)
    {
        parser.nonOptions().withValuesConvertedBy(new OnlinePlayerValueConverter(true));
        parser.accepts("t")
                .withRequiredArg()
                .required()
                .withValuesConvertedBy(new ScatterLogicValueConverter())
                .describedAs("Type of scatter to use");
        parser.accepts("teams", "Scatter players as teams");
        parser.accepts("c")
                .withRequiredArg()
                .required()
                .withValuesConvertedBy(new LocationValueConverter())
                .describedAs("World/coordinates for the center");
        parser.accepts("r")
                .withRequiredArg()
                .ofType(Double.class)
                .required()
                .describedAs("Radius for scatter");
        parser.accepts("min")
                .withRequiredArg()
                .ofType(Double.class)
                .defaultsTo(0D)
                .describedAs("Minimum distance between players after scatter");
        parser.accepts("minradius")
                .withRequiredArg()
                .ofType(Double.class)
                .defaultsTo(0D)
                .describedAs("Minimum radius from the center of the scatter");
    }
}
