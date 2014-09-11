/*
 * HeadHealthCommand.java
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

import com.google.common.base.Optional;
import com.publicuhc.uhcaddons.playerheads.features.GoldenHeadFeature;
import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.api.Feature;
import com.publicuhc.ultrahardcore.api.FeatureManager;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.CommandOptions;
import com.publicuhc.ultrahardcore.framework.routing.annotation.OptionsMethod;
import com.publicuhc.ultrahardcore.framework.routing.annotation.PermissionRestriction;
import com.publicuhc.ultrahardcore.framework.shaded.inject.Inject;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionDeclarer;
import com.publicuhc.ultrahardcore.framework.shaded.joptsimple.OptionSet;
import com.publicuhc.ultrahardcore.framework.translate.Translate;
import org.bukkit.command.CommandSender;

public class HeadHealthCommand implements Command
{
    private final Translate translate;
    private final FeatureManager manager;

    public static final String HEAD_HEALTH_PERMISSION = "UHC.heads.health";

    @Inject
    protected HeadHealthCommand(Translate translate, FeatureManager manager)
    {
        this.translate = translate;
        this.manager = manager;
    }

    @CommandMethod("headhealth")
    @PermissionRestriction(HEAD_HEALTH_PERMISSION)
    @CommandOptions("a")
    public void onHeadHealth(OptionSet set, CommandSender sender, Integer amount)
    {
        Optional<Feature> feature = manager.getFeatureByID("GoldenHeads");

        if(!feature.isPresent()) {
            throw new IllegalStateException("Feature not loaded");
        }

        GoldenHeadFeature heads = (GoldenHeadFeature) feature.get();

        if(set.has("a")) {
           heads.setAmountTotal(amount);
        }

        translate.sendMessage("amount total message", sender, heads.getAmountTotal());
    }

    @OptionsMethod
    public void onHeadHealth(OptionDeclarer declarer)
    {
        declarer.accepts("a")
                .withRequiredArg()
                .ofType(Integer.class)
                .describedAs("Amount of half hearts to heal");
    }
}
