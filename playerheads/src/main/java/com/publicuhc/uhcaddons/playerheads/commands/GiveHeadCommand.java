/*
 * GiveHeadCommand.java
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

package com.publicuhc.uhcaddons.playerheads.commands;

import com.google.common.base.Joiner;
import com.publicuhc.uhcaddons.playerheads.PlayerHeadProvider;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandOptions;
import com.publicuhc.ultrahardcore.framework.routing.annotation.OptionsMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.PermissionRestriction;
import com.publicuhc.ultrahardcore.framework.routing.converters.OnlinePlayerValueConverter;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GiveHeadCommand implements Command
{

    public static final String GIVE_HEAD_PERMISSION = "UHC.heads.give";
    private PlayerHeadProvider provider;
    private Translate translate;

    @Inject
    public GiveHeadCommand(PlayerHeadProvider provider, Translate translate)
    {
        this.translate = translate;
        this.provider = provider;
    }

    @CommandMethod("givehead")
    @PermissionRestriction(GIVE_HEAD_PERMISSION)
    @CommandOptions({"[arguments]", "p", "n"})
    public void onGiveHead(OptionSet set, CommandSender sender, List<String> args, Player[] p, Integer n)
    {
        if(args.isEmpty()) {
            translate.sendMessage("at least one", sender);
            return;
        }

        if(n <= 0) {
            translate.sendMessage("greater than zero", sender);
            return;
        }

        Player sendTo;

        if(set.has("p")) {
            sendTo = p[0];
        } else {
            if(!(sender instanceof Player)) {
                translate.sendMessage("must be player", sender);
                return;
            }
            sendTo = (Player) sender;
        }

        if(args.isEmpty()) {
            translate.sendMessage("at least one", sender);
            return;
        }

        boolean golden = set.has("g");

        for(String s : args) {
            ItemStack toAdd;
            if(golden) {
                toAdd = provider.getGoldenHead();
                provider.addPlayerLore(toAdd, s);
            } else {
                toAdd = provider.getPlayerHead(s);
            }

            toAdd.setAmount(n);
            sendTo.getInventory().addItem(toAdd);
        }

        translate.sendMessage("given heads", sendTo, Joiner.on(",").join(args));

        if(set.has("p")) {
            translate.sendMessage("gave heads", sender);
        }
    }

    @OptionsMethod
    public void onGiveHead(OptionDeclarer declarer)
    {
        declarer.accepts("p")
                .withRequiredArg()
                .withValuesConvertedBy(new OnlinePlayerValueConverter(false))
                .describedAs("Player to give the head to");
        declarer.accepts("g", "Give golden heads to the player instead");
        declarer.accepts("n")
                .withRequiredArg()
                .ofType(Integer.class)
                .describedAs("Number of heads to give")
                .defaultsTo(1);
        declarer.nonOptions().describedAs("List of names of players to create heads from");
    }
}
