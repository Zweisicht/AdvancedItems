package at.zweisicht.AdvancedItems.Items;

import at.zweisicht.AdvancedItems.AdvancedItems;
import at.zweisicht.AdvancedItems.ItemManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class Glider
{

	AdvancedItems plugin;
	ItemManager itemManager;

	ItemStack itemStack = new ItemStack(Material.CARPET);
	HashMap<UUID, BukkitTask> glideMode = new HashMap<>();

	public Glider(AdvancedItems plugin){
		
		this.plugin = plugin;
		itemManager = plugin.itemManager;
		itemStack = itemManager.getItem(itemStack);

	}

	public ItemStack getGlider()
	{
		return itemStack;
	}


	public void disableGlider(Player p){
		if(glideMode.containsKey(p.getUniqueId())){
			glideMode.get(p.getUniqueId()).cancel();
			glideMode.remove(p.getUniqueId());
		}
	}

	public void useGlider(Player p){

		ItemStack glider = p.getItemInHand();

		if(!glider.hasItemMeta()) return;

		ItemMeta meta = glider.getItemMeta();

		if(!itemStack.getItemMeta().getDisplayName().contains(meta.getDisplayName())) return;

		if(glideMode.containsKey(p.getUniqueId())){
			glideMode.get(p.getUniqueId()).cancel();
			glideMode.remove(p.getUniqueId());
		}else{

			itemManager.setDurability(p, glider);
			p.sendMessage(plugin.getConfig().getString("Settings.Language.use_glider").replace("&", "§"));
			glideMode.put(p.getUniqueId(), new GliderMode(p).runTaskTimer(plugin, 1, 1));

		}
	}

	public class GliderMode extends BukkitRunnable
	{

		final public Player p;

		public GliderMode (Player p){
			this.p = p;
		}

		@Override
		public void run(){

			Vector velocity = p.getLocation().getDirection();
			velocity.setY(-0.1);
			p.setVelocity(velocity);
			p.setFallDistance(0);

			Block b = p.getLocation().getBlock();

			if(b.isLiquid() || p.isOnGround()){
				useGlider(p);
				this.cancel();
			}

			}

	}

}
