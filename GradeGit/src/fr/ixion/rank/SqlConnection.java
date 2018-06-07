package fr.ixion.rank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.ixion.rank.data.DataPlayer;
import fr.ixion.rank.rank.Rank;

public class SqlConnection {
	private Connection connection;
	private String urlbase, host, database, user, pass;
	private Main main;

	public SqlConnection(Main main, String urlbase, String host, String database, String user, String pass) {
		this.urlbase = urlbase;
		this.host = host;
		this.database = database;
		this.user = user;
		this.main = main;
		this.pass = pass;
	}

	public void connection() {
		if (!isConnected()) {
			try {
				connection = DriverManager.getConnection(urlbase + host + "/" + database, user, pass);
				System.out.println("Connecte a la base MySql");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void disconnect() {
		try {
			connection.close();
			System.out.println("Déconnecta de la base MySql");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * INSERT UPDATE DELETE SELECT
	 * 
	 * PREPARER ?,? REMPLACER LES ? PAR DES VALEURS EXECUTE
	 */

	public boolean isConnected() {
		return connection != null;
	}
	public boolean hasAccount(Player player) {
		try {
			PreparedStatement rs = connection.prepareStatement("SELECT pseudo FROM users WHERE pseudo = ?");
			rs.setString(1, player.getName().toString());
			ResultSet resultats = rs.executeQuery();
			if(resultats.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean isOnline(Player player) {
		return main.dataPlayers.containsKey(player);
	}

	public void setRank(Player player, Rank rank) {
		if(main.dataPlayers.containsKey(player)) {
			DataPlayer dataP = main.dataPlayers.get(player);
			dataP.setRank(rank);
			main.dataPlayers.remove(player);
			main.dataPlayers.put(player, dataP);
		}
	}

	public Rank getRank(Player player) {
		if(main.dataPlayers.containsKey(player)) {
			DataPlayer dataP = main.dataPlayers.get(player);
			return dataP.getRank();
		}
		return null;
	}
	
	public DataPlayer setPlayerDate(Player player) {
		try {
			PreparedStatement rs = connection.prepareStatement("SELECT grade  FROM users WHERE pseudo = ?");
			rs.setString(1, player.getName().toString());
			ResultSet resultats = rs.executeQuery();
			Rank rank = Rank.JOUEUR;
			while(resultats.next()) {
				rank = Rank.powerToRank(resultats.getInt("grade"));
			}
			DataPlayer dataP = new DataPlayer();
			dataP.setRank(rank);
			return dataP;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new DataPlayer();
		
	}
	
	public void updatePlayerData(Player player) {
		if(main.dataPlayers.containsKey(player)) {
			
			DataPlayer dataP = main.dataPlayers.get(player);
			Rank rank = dataP.getRank();
			int power = rank.getPower();
			
			try {
				PreparedStatement rs = connection.prepareStatement("UPDATE users SET grade = ? WHERE pseudo = ?");
				rs.setInt(1, power);
				rs.setString(2, player.getName().toString());
				rs.executeUpdate();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
}
