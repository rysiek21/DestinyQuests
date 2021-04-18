package me.rysiek21.destinyquests;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PluginListeners implements Listener {
	
	@EventHandler
	private void PlayerJoin(PlayerJoinEvent e) throws SQLException {
		Player p = e.getPlayer();
		DatabaseConnect db = new DatabaseConnect();
		db.AddNewPlayer(String.valueOf(p.getUniqueId()));
	}
	
	@EventHandler
	void RightClickOnCitizen(NPCRightClickEvent e) {
		FileConfiguration config = Main.getConfigFile();
		DatabaseConnect db = new DatabaseConnect();
		Player p = e.getClicker();
		NPC npc = e.getNPC();
		config.getConfigurationSection("quests").getKeys(false).forEach(quest ->{
			for(int i=1;i<=config.getInt("quests." + quest + ".Stages");i++) {
				try {
					String questStage = db.GetPlayerQuest(String.valueOf(p.getUniqueId()), quest);
					if (i == Integer.parseInt(questStage)) {
						if(npc.getId() == config.getInt("quests." + quest + "." + i + ".NPC-ID")) {
							db.SetPlayerQuest(String.valueOf(p.getUniqueId()), quest, String.valueOf(Integer.parseInt(questStage)+1));
							List<String> messages = config.getStringList("quests." + quest + "." + i + ".Messages");
							List<String> allMess = config.getStringList("quests." + quest + "." + i + ".Messages");
							new BukkitRunnable() {
									
								@Override
								public void run() {
									String actionBarMessage = messages.get(0);
									actionBarMessage = actionBarMessage.replace('&', '§');
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBarMessage));
									messages.remove(0);
									if(messages.size() == 0) {
										for (String x : allMess) {
											p.sendMessage(ChatColor.translateAlternateColorCodes('&', x));
											if(x==allMess.get(allMess.size()-1)) {
												cancel();
											}
										}
									}
								}
							}.runTaskTimer(Main.getPlugin(), 0L ,config.getInt("quests." + quest + "." + i + ".delay"));
						}	
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
