/*
 * DefaultPlayerHeadProvider.java
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

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class DefaultPlayerHeadProvider implements PlayerHeadProvider {

    public static final String HEAD_NAME = ChatColor.GOLD + "Golden Head";

    @Override
    public ItemStack getPlayerHead(String name)
    {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1);
        //3 is a player skull
        is.setDurability((short) 3);

        SkullMeta meta = (SkullMeta) is.getItemMeta();
        meta.setOwner(name);
        is.setItemMeta(meta);
        return is;
    }

    @Override
    public ItemStack getPlayerHead(Player player)
    {
        return getPlayerHead(player.getName());
    }

    @Override
    public ItemStack getGoldenHead()
    {
        ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(HEAD_NAME);

        //add it's lore
        List<String> lore = Lists.newArrayList("Some say consuming the head of a", "fallen foe strengthens the blood");
        meta.setLore(lore);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    @Override
    public boolean isGoldenHead(ItemStack itemStack)
    {
        if (itemStack.getType() == Material.GOLDEN_APPLE) {
            ItemMeta im = itemStack.getItemMeta();

            if (im.hasDisplayName() && im.getDisplayName().equals(HEAD_NAME)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void addPlayerLore(ItemStack itemStack, String name)
    {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(ChatColor.AQUA + "Made from the head of: " + name);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void addPlayerLore(ItemStack itemStack, Player player)
    {
        addPlayerLore(itemStack, player.getName());
    }
}
