package fr.ixion.rank.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import fr.ixion.rank.Main;

public class AddPerm implements CommandExecutor {
	private Main main;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setPerm")) {
			if (args.length == 0 || args.length > 3) {
				sender.sendMessage("§cLa commande est /setRank [player] [rank]");
				return false;
			}

			if (args.length == 3) {
				String playerName = args[0];
				Player playerSelect = Bukkit.getPlayer(playerName);
				boolean statut = Boolean.parseBoolean(args[2]);
				playerSelect.addAttachment(main).setPermission(args[1], statut); 
				Bukkit.broadcastMessage(sender.getName() + " à modifié la permission de "  + playerSelect.getName() +" sur " + args[1] +" sur " + statut);
				for (PermissionAttachmentInfo string : playerSelect.getEffectivePermissions()) {
					Bukkit.broadcastMessage(string.getPermission() + "");
				}
				
			}
		}
		return false;

	}
	public void getMain(Main main) {
		this.main = main;
	}
}
