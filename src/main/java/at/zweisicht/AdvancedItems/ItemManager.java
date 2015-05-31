package at.zweisicht.AdvancedItems;

import at.zweisicht.AdvancedItems.Items.Glider;
import at.zweisicht.AdvancedItems.Items.Hook;
import at.zweisicht.AdvancedItems.Items.JetPack;
import org.bukkit.Location;
import org.bukkit.inventory.*;
import org.bukkit.configuration.file.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.entity.*;
import java.util.*;

public class ItemManager
{
	
	AdvancedItems plugin;
	
	JetPack jetPack;
	Hook hook;
	Glider glider;

	final public String JETPACK = "Jetpack";
	final public String GLIDER = "Glider";
	final public String HOOK = "Hook";

	public ItemManager(AdvancedItems plugin){
		this.plugin = plugin;
	}

	public void loadItems(){

		jetPack = new JetPack(plugin);
		hook = new Hook(plugin);
		glider = new Glider(plugin);

	}

	public boolean isItem(String type, ItemStack item){

		if(item == null) return false;

		ItemStack control_item = null;

		switch(type){
			case "Jetpack":
				control_item = getJetPack();
				break;
			case "Glider":
				control_item = getGlider();
				break;
			case "Hook":
				control_item = getHook();
				break;
			default: return false;

		}

		if(!item.hasItemMeta()) return false;
		if(!item.getItemMeta().hasDisplayName()) return false;
		if(item.getItemMeta().getDisplayName().equals(control_item.getItemMeta().getDisplayName())) return true;

		return false;
	}

	public ItemStack getItem(ItemStack itemStack){
		
		String name = null;
		
		switch(itemStack.getType().toString()){

			case "FIREWORK": name = "Jetpack";
				break;
			case "FISHING_ROD": name = "Hook";
				break;
			case "CARPET": name = "Glider";
			
	}


		
	FileConfiguration config = plugin.getConfig();

	ItemMeta meta = itemStack.getItemMeta();
	List<String> lore = config.getStringList(name + ".Lore");
		
	for(int i = 0; i < lore.size(); i++)
		lore.set(i,lore.get(i).replaceAll("&","§"));

	meta.setDisplayName(config.getString(name + ".Name"));
	meta.setLore(lore);

	itemStack.setItemMeta(meta);

	return itemStack;
	}
	
	public ItemStack getJetPack(){
		return jetPack.getJetPack();
	}
	
	public ItemStack getGlider(){
		return glider.getGlider();
	}
	
	public ItemStack getHook(){
		return hook.getHook();
	}
	
	public void useJetPack(Player p){
		jetPack.useJetPack(p);
	}
	
	public void useHook(Player p, Location target){
		hook.useHook(p,target);
	}

	public void useGlider(Player p){
		glider.useGlider(p);
	}
}
