package at.zweisicht.AdvancedItems.Items;

import at.zweisicht.AdvancedItems.AdvancedItems;
import at.zweisicht.AdvancedItems.ItemManager;
import org.bukkit.entity.*;
import org.bukkit.util.*;
import org.bukkit.inventory.*;
import org.bukkit.*;


public class Hook
{
	
	AdvancedItems plugin;
	ItemManager itemManager;
	
	ItemStack itemStack = new ItemStack(Material.FISHING_ROD);
	
	public Hook(AdvancedItems plugin){
		
		this.plugin = plugin;
		itemManager = plugin.itemManager;
		itemStack = itemManager.getItem(itemStack);
		
	}


	public ItemStack getHook()
	{
		return itemStack;
	}


	//https://bukkit.org/threads/grapple-hook.230488/
	public void useHook(Player p, Location loc){

		Location pLoc = p.getLocation();

		pLoc.setY(pLoc.getY()+0.5);
		p.teleport(pLoc);

		double g = -0.08;
		double d = loc.distance(pLoc);
		double t = d;
		double v_x = (1.0+0.07*t) * (loc.getX() - pLoc.getX())/t;
		double v_y = (1.0+0.03*t) * (loc.getY() - pLoc.getY())/t -0.5*g*t;
		double v_z = (1.0+0.07*t) * (loc.getZ() - pLoc.getZ())/t;

		Vector v = p.getVelocity();
		v.setX(v_x);
		v.setY(v_y);
		v.setZ(v_z);
		p.setVelocity(v);

	}
}
