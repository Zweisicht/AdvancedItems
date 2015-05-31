
package at.zweisicht.AdvancedItems;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.*;

public class AdvancedItems extends JavaPlugin
{
	
	public ItemManager itemManager;
	
	public void onEnable(){

		this.saveDefaultConfig();
		this.reloadConfig();
		itemManager = new ItemManager(this);
		itemManager.loadItems();
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		
	}
	
	public void onDisable(){


		
	}


	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("AdvancedItems") || cmd.getName().equalsIgnoreCase("AI")){

			if (args.length > 0) {

				Player p = (Player) sender;

				switch(args[0]){
					case "jetpack": p.getInventory().addItem(itemManager.getJetPack());
									break;
					case "glider":	p.getInventory().addItem(itemManager.getGlider());
									break;
					case "hook":	p.getInventory().addItem(itemManager.getHook());

				}

			}

			return true;
		}
		return false;
	}


}
