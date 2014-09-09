package com.publicuhc.uhcaddons.playerheads;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class DefaultPlayerHeadProvider implements PlayerHeadProvider {

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
        return null;
    }
}
