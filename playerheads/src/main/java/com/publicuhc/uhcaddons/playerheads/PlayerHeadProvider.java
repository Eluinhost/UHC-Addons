package com.publicuhc.uhcaddons.playerheads;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PlayerHeadProvider {

    /**
     * Get a player head for the given name
     *
     * @param name the name of the player the head belongs to
     * @return the head set for the player name given
     */
    ItemStack getPlayerHead(String name);

    /**
     * Get a player head for the given player
     *
     * @param player the player the head belongs to
     * @return the head set for the player
     */
    ItemStack getPlayerHead(Player player);
}
