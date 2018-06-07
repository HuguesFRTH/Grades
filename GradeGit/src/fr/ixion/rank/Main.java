package fr.ixion.rank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ixion.rank.commands.AddPerm;
import fr.ixion.rank.commands.SetRank;
import fr.ixion.rank.data.DataManager;
import fr.ixion.rank.data.DataPlayer;
import fr.ixion.rank.rank.Rank;

public class Main extends JavaPlugin implements Listener {
	public static Main instance;

	public static Main getInstance() {
		return instance;
	}

	public SqlConnection sql;
	public DataManager dataManager = new DataManager(this);
	public Map<Player, DataPlayer> dataPlayers = new HashMap<>();

	public void onEnable() {
		saveDefaultConfig();

		sql = new SqlConnection(this, "jdbc:mysql://", "localhost", "membre", "root", "");
		sql.connection();
		getServer().getPluginManager().registerEvents(this, this);
		SetRank setRank = new SetRank();
		setRank.getMain(this);
		getCommand("setRank").setExecutor(setRank);
		AddPerm addPerm = new AddPerm();
		addPerm.getMain(this);
		getCommand("setPerm").setExecutor(addPerm);
		Player[] playersOnline = Bukkit.getOnlinePlayers();
		for (Player player : playersOnline) {
			dataManager.loadPlayerDate(player);
		}
		FileConfiguration Config = this.getConfig();
		for (Rank rank : Rank.getAllRanks()) {
			List<String> permissionList = Config.getStringList("Persmission." + rank.getConfigLine());
			for (String permission : permissionList) {
				System.out.println(permission);
			}
			System.out.println("end");
		}
		

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		dataManager.loadPlayerDate(e.getPlayer());
		DataPlayer dataP = dataPlayers.get(e.getPlayer());
		setPermission(e.getPlayer(), dataP.getRank());
		for (PermissionAttachmentInfo string : e.getPlayer().getEffectivePermissions()) {
			Bukkit.broadcastMessage(string.getPermission() + "");
		}
	}

	public void setPermission(Player player, Rank rank) {
		Set<PermissionAttachmentInfo> permissionsP = player.getEffectivePermissions();
		for (PermissionAttachmentInfo permission : permissionsP) {
			player.addAttachment(this).setPermission(permission.toString(), false);
		}
		FileConfiguration Config = this.getConfig();
		List<String> permissionList = Config.getStringList("Persmission." + rank.getConfigLine());
		for (String permission : permissionList) {
			player.addAttachment(this).setPermission(permission, true);
			System.out.println(permission);
		}
		System.out.println("end");
	}

	@EventHandler
	public void onQuiet(PlayerQuitEvent e) {
		dataManager.savePlayerDate(e.getPlayer());
		dataManager.deletePlayerDate(e.getPlayer());
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player player = e.getPlayer();
		if (sql.hasAccount(player) == false) {
			e.setKickMessage(ChatColor.RED + "Votre compte n'est pas enregistré");
			e.setResult(Result.KICK_OTHER);
			System.out.println(
					"Le joueur " + player.getName().toString() + " a essaye de se connecter sans s'etre enregistré");
		}
	}

	@EventHandler
	public void chat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		Rank rank = sql.getRank(player);
		e.setFormat(rank.getSuf() + rank.getColorChat() + player.getName().toString() + " §8: " + rank.getColorChat()
				+ e.getMessage());
	}

	public void onDisable() {
		Player[] playersOnline = Bukkit.getOnlinePlayers();
		for (Player player : playersOnline) {
			dataManager.savePlayerDate(player);
		}
		sql.disconnect();
	}
}
