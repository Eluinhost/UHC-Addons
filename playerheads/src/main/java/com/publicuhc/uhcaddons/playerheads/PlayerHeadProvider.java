/*
 * PlayerHeadProvider.java
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

package com.publicuhc.uhcaddons.playerheads;

import org.bukkit.block.Block;
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

    /**
     * Gets a golden head itemstack
     *
     * @return the golden head itemstack
     */
    ItemStack getGoldenHead();

    /**
     * Check if the provided ItemStack is a golden head
     *
     * @param itemStack the stack to check
     * @return true if stack is a golden head, false otherwise
     */
    boolean isGoldenHead(ItemStack itemStack);

    /**
     * Adds the player lore to the given itemstack
     *
     * @param name the name to add
     * @param itemStack the itemstack to add the lore to
     */
    void addPlayerLore(ItemStack itemStack, String name);

    /**
     * Adds the player lore to the given itemstack
     *
     * @param itemStack the itemstack to add the lore to
     * @param player the player
     */
    void addPlayerLore(ItemStack itemStack, Player player);

    /**
     * Sets the block given to head of the player facing in the direction the player is facing
     *
     * @param p the player
     * @param headBlock the block to set
     */
    void setBlockAsHead(Player p, Block headBlock);

    /**
     * Sets the block given to head of the player
     *
     * @param name the player name
     * @param direction the direction to face
     * @param headBlock the block to set
     */
    void setBlockAsHead(String name, Block headBlock, BlockFace2D direction);
}
