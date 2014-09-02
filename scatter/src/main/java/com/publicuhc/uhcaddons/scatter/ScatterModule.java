package com.publicuhc.uhcaddons.scatter;

import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.api.UHCAddonModule;
import com.publicuhc.ultrahardcore.api.UHCFeature;
import com.publicuhc.ultrahardcore.framework.shaded.inject.multibindings.Multibinder;
import org.bukkit.plugin.Plugin;

public class ScatterModule extends UHCAddonModule {
    protected ScatterModule(Plugin instance)
    {
        super(instance);
    }

    @Override
    protected void registerFeatures(Multibinder<UHCFeature> binder) {
        //no features
    }

    @Override
    protected void registerCommands(Multibinder<Command> binder) {
        binder.addBinding().to(ScatterCommand.class);
    }
}
