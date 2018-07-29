package com.c010ur1355.bookshelfgui;

import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin{
    @Override
    public void onEnable(){
        getCommand("bsgui").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        loadConfig();
    }

    public void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}