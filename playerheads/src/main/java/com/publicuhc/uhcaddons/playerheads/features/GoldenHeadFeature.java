/*
 * GoldenHeadFeature.java
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
import com.publicuhc.uhcaddons.playerheads.PlayerHeadProvider;
import com.publicuhc.uhcaddons.playerheads.RecipeChecker;
import com.publicuhc.ultrahardcore.api.UHCFeature;
import com.publicuhc.ultrahardcore.framework.configuration.Configurator;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;

public class GoldenHeadFeature extends UHCFeature
{
    public static final int TICKS_PER_HALF_HEART = 25;

    private final PlayerHeadProvider provider;
    private int amountTotal;
    private ShapedRecipe recipe;
    private RecipeChecker checker;

    @Inject
    protected GoldenHeadFeature(Configurator configurator, PlayerHeadProvider provider, RecipeChecker checker)
    {
        Optional<FileConfiguration> config = configurator.getConfig("main");

        if(!config.isPresent()) {
            throw new IllegalStateException("Main config file is unavailable, unable to load feature");
        }
        this.amountTotal = config.get().getInt("golden heads heal total");
        this.provider = provider;
        this.checker = checker;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerEatEvent(PlayerItemConsumeEvent event)
    {
        if(isEnabled() && provider.isGoldenHead(event.getItem())) {
            event.getPlayer().addPotionEffect(getPotionEffect());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPreCraftEvent(PrepareItemCraftEvent event) {
        if(isEnabled() && checker.areSimilar(event.getRecipe(), recipe)) {
            //get the name of the used head
            String name = "INVALID PLAYER";
            for (ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack.getType() == Material.SKULL_ITEM) {
                    SkullMeta sm = (SkullMeta) itemStack.getItemMeta();
                    name = sm.getOwner();
                }
            }

            provider.addPlayerLore(event.getInventory().getResult(), name);
        }
    }

    @Override
    protected void enableCallback() {
        //Make a recipe that will return a golden apple when the right shape is made
        ItemStack headStack = provider.getGoldenHead();

        //make the recipe
        ShapedRecipe goldenHead = new ShapedRecipe(headStack);

        //8 gold ingots surrounding an apple
        goldenHead.shape("AAA", "ABA", "AAA");

        //noinspection deprecation
        goldenHead.setIngredient('A', Material.GOLD_INGOT)
                .setIngredient('B', Material.SKULL_ITEM, 3); //TODO deprecated but no alternative?

        recipe = goldenHead;

        //add recipe to bukkit
        Bukkit.addRecipe(goldenHead);
    }

    @Override
    protected void disableCallback() {
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        while (recipeIterator.hasNext()) {
            //if it's a golden apple named like ours remove it
            if(provider.isGoldenHead(recipeIterator.next().getResult())) {
                recipeIterator.remove();
            }
        }
    }

    public int getAmountTotal()
    {
        return amountTotal;
    }

    public void setAmountTotal(int amount)
    {
        this.amountTotal = amount;
    }

    public PotionEffect getPotionEffect()
    {
        return new PotionEffect(
                PotionEffectType.REGENERATION,
                TICKS_PER_HALF_HEART * amountTotal,
                1
        );
    }

    @Override
    public String getFeatureID()
    {
        return "GoldenHeads";
    }

    @Override
    public String getDescription()
    {
        return "Golden heads can be crafted for extra bonuses";
    }
}
