package fr.ixion.rank.data;

import org.bukkit.entity.Player;

import fr.ixion.rank.Main;

public class DataManager {
	private Main main;

	public DataManager(Main main) {
		this.main = main;
	}

	public void loadPlayerDate(Player player) {
		if(!main.dataPlayers.containsKey(player)) {
			DataPlayer pData = main.sql.setPlayerDate(player);
			main.dataPlayers.put(player, pData);
		}	
	}

	public void savePlayerDate(Player player) {
		if(main.dataPlayers.containsKey(player)) {
			main.sql.updatePlayerData(player);
		}
		
	}
	public void deletePlayerDate(Player player) {
		if(main.dataPlayers.containsKey(player)) {
			main.dataPlayers.remove(player);
		}
		
	}
}
