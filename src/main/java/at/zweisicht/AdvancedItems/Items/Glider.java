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
import java.util.List;
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
		List<String> lore = meta.getLore();


		if(!itemStack.getItemMeta().getDisplayName().contains(meta.getDisplayName())) return;

		if(glideMode.containsKey(p.getUniqueId())){
			glideMode.get(p.getUniqueId()).cancel();
			glideMode.remove(p.getUniqueId());
		}else{

			for(int i = 0; i < meta.getLore().size(); i++){

				if(lore.get(i).contains("Durability:")){

					int dura = Integer.parseInt(lore.get(i).substring(12));
					lore.set(i,"Durability: " + (dura -1));

					if(dura == 0){
						p.setItemInHand(null);
						p.updateInventory();
						return;
					}
				}
			}

			glideMode.put(p.getUniqueId(), new GliderMode(p).runTaskTimer(plugin, 1, 1));
		}

		meta.setLore(lore);
		glider.setItemMeta(meta);
		p.updateInventory();

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
