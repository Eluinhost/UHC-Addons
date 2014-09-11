/*
 * HeadDropFeature.java
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Graham Howden <graham_howden1 at yahoo.co.uk>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.publicuhc.uhcaddons.playerheads.features;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.publicuhc.uhcaddons.playerheads.PlayerHeadProvider;
import com.publicuhc.ultrahardcore.api.UHCFeature;
import com.publicuhc.ultrahardcore.framework.configuration.Configurator;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Random;

public class HeadDropFeature extends UHCFeature
{

    public static final String ON_STAKE_PERMISSION = "UHC.heads.onStake";
    public static final String DROP_PERMISSION = "UHC.heads.drop";
    private final PlayerHeadProvider provider;
    private final Scoreboard scoreboard;
    private final FileConfiguration config;
    private final Random random = new Random();

    @Inject
    public HeadDropFeature(PlayerHeadProvider provider, Configurator configurator, Scoreboard scoreboard)
    {
        Optional<FileConfiguration> fileOptional = configurator.getConfig("main");

        if(!fileOptional.isPresent()) {
            throw new IllegalStateException("Main config file is unavailable, unable to load feature");
        }

        this.scoreboard = scoreboard;
        this.config = fileOptional.get();
        this.provider = provider;
    }

    /**
     * Gets the closest non empty block under the block supplied or null if none found
     *
     * @param block Block
     * @return Block
     */
    private static Optional<Block> getClosestGround(Block block)
    {
        Block loopBlock = block;
        //recurse until found
        while(true) {
            //if below the world return null
            if(loopBlock.getY() < 0) {
                return Optional.absent();
            }

            //if it's not empty return this block
            if(!loopBlock.isEmpty()) {
                return Optional.of(block);
            }
            loopBlock = loopBlock.getRelative(BlockFace.DOWN);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if(!isEnabled()) {
            return;
        }

        Player player = event.getEntity();
        if(!player.hasPermission(DROP_PERMISSION)) {
            return;
        }

        if(!isValidDeath(player)) {
            return;
        }

        if(!randomCheck()) {
            return;
        }

        if(config.getBoolean("attempt on stake") && player.hasPermission(ON_STAKE_PERMISSION)) {
            if(putHeadOnStake(player)) {
                return;
            }
        }

        event.getDrops().add(provider.getPlayerHead(player));
    }

    /**
     * @return true if random check passed, false otherwise
     */
    private boolean randomCheck()
    {
        return (random.nextInt(100) >= (100 - config.getInt("chance to drop")));
    }

    /**
     * Checks if the kill was a valid PVP kill
     *
     * @param deadPlayer the dead player
     * @return boolean
     */
    private boolean isValidDeath(Player deadPlayer)
    {

        Player killer = deadPlayer.getKiller();

        if(config.getBoolean("drop on pvp death only")) {
            //get the killer and if there isn't one it wasn't a PVP kill
            if(killer == null) {
                return false;
            }
        }

        //if we're checking that teammember kills don't count
        if(killer != null && config.getBoolean("ignore teamkills")) {
            //get the scoreboard and get the teams of both players
            Team team1 = scoreboard.getPlayerTeam(deadPlayer);
            Team team2 = scoreboard.getPlayerTeam(killer);

            //if they're both in valid teams and its the same team it wasn't a valid kill
            if(team1 != null && team2 != null && team1.getName().equals(team2.getName())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Places a player head on a stake for the player
     *
     * @param p the player
     * @return true if placed, false otherwise
     */
    private boolean putHeadOnStake(Player p)
    {
        //head location
        Location head = p.getEyeLocation();
        //block the player's head is in
        Block headBlock = head.getBlock();

        //don't do anything if below the world
        if(headBlock.getY() < 0) {
            return false;
        }

        //get the closest non air block below the players feet
        Optional<Block> groundOptional = getClosestGround(headBlock.getRelative(BlockFace.DOWN, 2));
        if(!groundOptional.isPresent()) {
            return false;
        }

        Block ground = groundOptional.get();

        //get the block 2 above the ground
        Block skullBlock = ground.getRelative(BlockFace.UP, 2);

        //if it's not empty we can't place the block
        if(skullBlock == null || !skullBlock.isEmpty()) {
            return false;
        }

        //set the skull block to an actual skull block
        provider.setBlockAsHead(p, skullBlock);

        //get the space for a fence and set it if there's nothing there
        Block fenceBlock = ground.getRelative(BlockFace.UP);
        if(fenceBlock != null && fenceBlock.isEmpty()) {
            fenceBlock.setType(Material.NETHER_FENCE);
        }
        //made successfully
        return true;
    }

    @Override
    public String getFeatureID()
    {
        return "PlayerHeads";
    }

    @Override
    public String getDescription()
    {
        return "Players drop heads on death";
    }

    @Override
    public List<String> getStatus()
    {
        return Lists.newArrayList();
    }
}
