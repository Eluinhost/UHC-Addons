/*
 * AbsorptionFeature.java
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

package com.publicuhc.uhcaddons.potions;

import com.publicuhc.ultrahardcore.api.UHCFeature;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

public class AbsorptionFeature extends UHCFeature
{
    private Plugin plugin;

    @Inject
    protected AbsorptionFeature(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerEatEvent(PlayerItemConsumeEvent pee)
    {
        //if we're not enabled remove the absorption on the next tick
        if(!isEnabled()) {
            //if they ate a golden apple
            ItemStack is = pee.getItem();
            if(is.getType() == Material.GOLDEN_APPLE) {
                //remove the absorption effect for the player on the next tick
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new RemovePotionEffectRunnable(pee.getPlayer().getUniqueId(), PotionEffectType.ABSORPTION));
            }
        }
    }

    @Override
    public String getFeatureID()
    {
        return "Absorption";
    }

    @Override
    public String getDescription()
    {
        return "Allows absorption from eating golden apples";
    }
}