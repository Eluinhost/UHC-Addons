package com.publicuhc.uhcaddons.scatter;

import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.api.UHCAddonModule;
import com.publicuhc.ultrahardcore.api.UHCFeature;
import com.publicuhc.ultrahardcore.framework.shaded.inject.multibindings.Multibinder;

public class ScatterModule extends UHCAddonModule {
    @Override
    protected void registerFeatures(Multibinder<UHCFeature> binder) {
        //no features
    }

    @Override
    protected void registerCommands(Multibinder<Command> binder) {
        binder.addBinding().to(ScatterCommand.class);
    }
}
