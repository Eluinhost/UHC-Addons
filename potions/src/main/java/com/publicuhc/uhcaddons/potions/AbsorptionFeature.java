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