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
