package com.publicuhc.uhcaddons.potions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class RemovePotionEffectRunnable implements Runnable
{

    private final UUID playerID;
    private final PotionEffectType[] effects;

    public RemovePotionEffectRunnable(UUID playerID, PotionEffectType... potions)
    {
        this.playerID = playerID;
        effects = potions;
    }

    @Override
    public void run()
    {
        Player p = Bukkit.getPlayer(playerID);
        if(p != null) {
            for(PotionEffectType type : effects) {
                p.removePotionEffect(type);
            }
        }
    }
}