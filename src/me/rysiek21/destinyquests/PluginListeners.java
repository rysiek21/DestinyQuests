package me.rysiek21.destinyquests;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;


import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PluginListeners implements Listener {
	
	@EventHandler
	void RightClickOnCitizen(NPCRightClickEvent e) {
		FileConfiguration config = Main.getConfigFile();
		Player p = e.getClicker();
		NPC npc = e.getNPC();
		config.getConfigurationSection("quests").getKeys(false).forEach(quest ->{
			for(int i=1;i<=config.getInt("quests." + quest + ".Stages");i++) {
				if(npc.getId() == config.getInt("quests." + quest + "." + i + ".NPC-ID")) {
					List<String> messages = config.getStringList("quests." + quest + "." + i + ".Messages");
					new BukkitRunnable() {
							
						@Override
						public void run() {
							String actionBarMessage = messages.get(0);
							actionBarMessage.replace('&', '§');
							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBarMessage));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.get(0)));
							messages.remove(0);
							if(messages.size() == 0) {
								cancel();
							}
						}
					}.runTaskTimer(Main.getPlugin(), 0L ,config.getInt("quests." + quest + "." + i + ".delay"));
				}
			}
		});
	}
}
