package fr.ixion.rank.rank;

import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;

public enum Rank {
	JOUEUR(0,"§7", ChatColor.GRAY ,new String[] {"joueur", "player"} , "§7joueur","Joueur"),
	MODO(10,"§1[§9Modérateur§1]", ChatColor.BLUE,new String[] {"modo","moderateur","modérateur"}, "§9Modérateur","Modo"),
	BUILDER(25,"§2[§aBuilder§2]", ChatColor.GREEN,new String[] {"builder" , "buildeur", "build"}, "§aBuilder","Builder"),
	DEVELOPPEUR(50,"§6[§eDéveloppeur§6]",ChatColor.YELLOW,new String[] {"dev" , "developpeur" , "développeur"}, "§eDéveloppeur","Dev"),
	ADMIN(100,"§4[§cAdministrateur§4]",ChatColor.RED,new String[] {"admin" , "administateur"}, "§cAdministrateur","Admin");

	private int power;
	private String displayName;
	private ChatColor colorTag;
	private String name;
	private String[] names;
	private String configLine;

	public static HashMap<Integer, Rank> grade = new HashMap<>();
	public static HashMap<String, Rank> stringGrade = new HashMap<>();

	Rank(int power, String displayName, ChatColor tag, String[] names ,String name,String configLine) {
		this.power = power;
		this.displayName = displayName;
		this.colorTag = tag;
		this.name = name;
		this.names = names;
		this.configLine = configLine;
	}

	static {
		for (Rank r : Rank.values()) {
			grade.put(r.getPower(), r);
			for (String string : r.getNames()) {
				stringGrade.put(string, r);			}
		}

	}

	public int getPower() {
		return power;
	}

	public String getSuf() {
		return displayName;
	}

	public ChatColor getColorChat() {
		return colorTag;
	}
	public String getName() {
		return name;
	}
	public String[] getNames() {
		return names;
	}
	public String getConfigLine() {
		return configLine;
	}

	public static Rank powerToRank(int power) {
		return grade.get(power);
	}

	public static Rank getRankByName(String string) {
		return stringGrade.get(string);
	}
	public static Rank[] getAllRanks() {
		Rank[] rank = {
				Rank.ADMIN,
				Rank.BUILDER,
				Rank.DEVELOPPEUR,
				Rank.JOUEUR,
				Rank.MODO
				
		};
		return rank; 
	}

}
