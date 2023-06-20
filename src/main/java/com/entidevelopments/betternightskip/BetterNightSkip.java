package com.entidevelopments.betternightskip;

import org.bukkit.plugin.java.JavaPlugin;

public final class BetterNightSkip extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        PlayerInBedListener listener = new PlayerInBedListener();
        getServer().getPluginManager().registerEvents(listener, this);
        getCommand("bns").setExecutor(listener);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
