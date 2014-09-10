/*
 * Tier2NerfFeature.java
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
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;

public class Tier2NerfFeature extends UHCFeature
{
    public static final String DENY_IMPROVED = "UHC.disableTier2";

    private final Translate translate;

    protected Tier2NerfFeature(Translate translate)
    {
        this.translate = translate;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent ice) {
        //if we're enabled
        if (isEnabled()) {
            //if it's not a brewing stand skip
            if (ice.getInventory().getType() != InventoryType.BREWING) {
                return;
            }

            InventoryView iv = ice.getView();
            boolean cancel = false;

            //if the player is shift clicking
            if (ice.isShiftClick()) {
                //if tier 2 is disabled and they're clicking glowstone and don't have permission
                if (ice.getCurrentItem().getType() == Material.GLOWSTONE_DUST && ice.getWhoClicked().hasPermission(DENY_IMPROVED)) {
                    cancel = true;
                }
            }

            //if its the fuel slot that was clicked
            if (ice.getSlotType() == InventoryType.SlotType.FUEL) {
                //if tier 2 disabled and glowstone is on the cursor and no permission
                if (iv.getCursor().getType() == Material.GLOWSTONE_DUST && ice.getWhoClicked().hasPermission(DENY_IMPROVED)) {
                    cancel = true;
                }
            }

            //if they didn't have permission for action
            if (cancel) {
                //cancel the event
                ice.setCancelled(true);

                //close the inventory
                ice.getWhoClicked().closeInventory();

                //send the player a message saying they can't do it
                translate.sendMessage("no permission", (CommandSender) ice.getWhoClicked());
            }
        }
    }

    @EventHandler
    public void onInventoryMoveItemEvent(InventoryMoveItemEvent imie) {
        //if we're enabled
        if (isEnabled()) {
            //if the item is being moved into a brewing stand
            if (imie.getDestination().getType() == InventoryType.BREWING) {
                //cancel glowstone
                if (imie.getItem().getType() == Material.GLOWSTONE_DUST) {
                    imie.setCancelled(true);
                }
            }
        }
    }

    @Override
    public String getFeatureID() {
        return "Tier2PotionNerf";
    }

    @Override
    public String getDescription() {
        return "Removes crafting of tier 2 potions";
    }
}
