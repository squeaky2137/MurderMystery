package me.squeaky2137.spigotmurdermystery;

import me.squeaky2137.spigotmurdermystery.events.PlayerDeath;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.plugin = this;
        getLogger().info("plugin has been enabled!!");
        getServer().getPluginCommand("murder").setExecutor(new Commands(this));
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
