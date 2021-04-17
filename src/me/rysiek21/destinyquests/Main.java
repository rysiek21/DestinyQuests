package me.rysiek21.destinyquests;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private static Plugin plugin;
	private static FileConfiguration config;
	
	@Override
	public void onEnable() {
		System.out.println("[DestinyQuests] Running version 1.0");
		plugin = (Plugin) this;
		this.saveDefaultConfig();
		config = this.getConfig();
		registerEvents(this, new PluginListeners());
		DatabaseConnect db = new DatabaseConnect();
		db.MysqlConnect();
	}
	
	public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
		
	public static Plugin getPlugin() {
        return plugin;
    }
	
	public static FileConfiguration getConfigFile() {
		return config;
	}
}