package com.sajan.caveairesp;

import com.sajan.caveairesp.modules.CaveAirESP;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.Category;

public class CaveAirESPAddon extends MeteorAddon {
    public static final Category CATEGORY = new Category("Cave Air");

    @Override
    public void onInitialize() {
        Modules.get().add(new CaveAirESP());
        Modules.get().add(new ChatBlock());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.sajan.caveairesp";
    }

    @Override
    public String getWebsite() {
        return "https://github.com/";
    }

    @Override
    public String getName() {
        return "Cave Air ESP";
    }
}
