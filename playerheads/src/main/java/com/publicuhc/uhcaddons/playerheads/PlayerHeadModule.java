package com.publicuhc.uhcaddons.playerheads;

import com.publicuhc.ultrahardcore.framework.shaded.inject.AbstractModule;

public class PlayerHeadModule extends AbstractModule {
    @Override
    protected void configure()
    {
        bind(PlayerHeadProvider.class).to(DefaultPlayerHeadProvider.class);
    }
}
