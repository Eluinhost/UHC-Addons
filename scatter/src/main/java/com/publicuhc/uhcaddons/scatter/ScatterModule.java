package com.publicuhc.uhcaddons.scatter;

import com.publicuhc.ultrahardcore.api.Command;
import com.publicuhc.ultrahardcore.framework.shaded.inject.AbstractModule;
import com.publicuhc.ultrahardcore.framework.shaded.inject.multibindings.Multibinder;

/**
 * Project: UHC-Addons
 * Package: com.publicuhc.uhcaddons.scatter
 * Created by Eluinhost on 13:05 25/08/2014 2014.
 */
public class ScatterModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Command> binder = Multibinder.newSetBinder(binder(), Command.class);
        binder.addBinding().to(ScatterCommand.class);
    }
}
