package fr.ixion.rank.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import fr.ixion.rank.Main;
import fr.ixion.rank.rank.Rank;

public class SetRank implements CommandExecutor {
private Main main;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("setRank")) {
			//Bukkit.broadcastMessage(sender.getEffectivePermissions() + "");
			if (sender.hasPermission("setRank.use")) {
				if (args.length == 0 || args.length > 3) {
					sender.sendMessage("§cLa commande est /setRank [player] [rank]");
					return false;
				}

				if (args.length == 2) {
					String playerName = args[0];
					Player playerSelect = Bukkit.getPlayer(playerName);
					
					
					if (main.sql.hasAccount(playerSelect)) {
						Rank rank = Rank.getRankByName(args[1]);
						if (rank != null) {
							main.sql.setRank(playerSelect, rank);
							sender.sendMessage("§6Vous venez de passer §a" + playerSelect.getName() + "§6 au grade " + rank.getName());
							main.setPermission(playerSelect, rank);
							if(main.sql.isOnline(playerSelect)) {
								playerSelect.sendMessage("§c"+sender.getName() + "§6 vous a passé " + rank.getName() );							}
							return true;
						} else {
							sender.sendMessage("§cLa commande est /setRank [player] [rank]");
							sender.sendMessage(
									"§cCe grade n'est pas un grade valide. Les grades valides sont : admin , dev, modo , builder , joueur");
							return false;
						}
					} else {
						sender.sendMessage("§cLa commande est /setRank [player] [rank]");
						sender.sendMessage("§cCe joueurs n'existe pas");
						return false;
					}
				}

			}
		}
		return false;
	}
	
	public void getMain(Main main) {
		this.main = main;
	}
}
