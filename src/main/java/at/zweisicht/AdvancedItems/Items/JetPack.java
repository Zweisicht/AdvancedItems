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

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JetPack
{
	AdvancedItems plugin;
	ItemManager itemManager;
	HashMap<UUID, BukkitTask> flyMode = new HashMap<>();
	
	private ItemStack itemStack = new ItemStack(Material.FIREWORK, 1);
	
	public JetPack(AdvancedItems plugin){

		this.plugin = plugin;
		itemManager = plugin.itemManager;
		itemStack = itemManager.getItem(itemStack);
		
	}

	public boolean isUser(Player p){
		return flyMode.containsKey(p.getUniqueId());
	}

	public ItemStack getJetPack(){
		return itemStack;
	}

	public void disableJetPack(Player p){

		if(flyMode.containsKey(p.getUniqueId())){
			flyMode.get(p.getUniqueId()).cancel();
			flyMode.remove(p.getUniqueId());
		}
	}

	public void useJetPack(Player p){

		ItemStack jetpack = p.getInventory().getChestplate();

		if(!jetpack.hasItemMeta()) return;

		ItemMeta meta = jetpack.getItemMeta();
		List<String> lore = meta.getLore();

		if(!itemStack.getItemMeta().getDisplayName().contains(meta.getDisplayName())) return;

		String name = meta.getDisplayName();

		if(flyMode.containsKey(p.getUniqueId())){
			flyMode.get(p.getUniqueId()).cancel();
			flyMode.remove(p.getUniqueId());
		}else{

			for(int i = 0; i < meta.getLore().size(); i++){

				if(lore.get(i).contains("Durability:")){

					int dura = Integer.parseInt(lore.get(i).substring(12));
					lore.set(i,"Durability: " + (dura -1));

					if(dura == 0){
						p.sendMessage(plugin.getConfig().getString("Settings.Language.destory").replace("&","§").replace("<item>", name));
						p.getInventory().setChestplate(null);
						p.updateInventory();
						return;
					}
				}
			}
			p.sendMessage(plugin.getConfig().getString("Settings.Language.jetpack").replace("&","§"));
			flyMode.put(p.getUniqueId(), new FlyMode(p).runTaskTimer(plugin, 1, 1));
		}

		meta.setLore(lore);
		jetpack.setItemMeta(meta);
		p.updateInventory();
		
	}

	public class FlyMode extends BukkitRunnable
	{

		int fuel = 39;
		final public Player p;

		public FlyMode (Player p){
			this.p = p;
		}

		//@Override
		public void run(){

			Block b = p.getLocation().getBlock();
			if(b.isLiquid() || p.isOnGround()){
				disableJetPack(p);
				this.cancel();
				return;
			}

			//Treibstoff verbrauchen.
			if(fuel == 40){

				if(!itemManager.isItem(itemManager.JETPACK, p.getInventory().getChestplate())){
					disableJetPack(p);
					this.cancel();
					return;
				}

				int fuel_slot = p.getInventory().first(Material.REDSTONE);

				if(fuel_slot != -1){

					ItemStack fuel_is = p.getInventory().getItem(fuel_slot);
					fuel_is.setAmount(fuel_is.getAmount() -1);
					p.getInventory().setItem(fuel_slot, fuel_is);
					p.updateInventory();

				}else{
					p.sendMessage(plugin.getConfig().getString("Settings.Language.out_of_fuel").replace("&","§"));
					useJetPack(p);
				}

				fuel = 0;
			}

			fuel++;
			p.setVelocity(p.getLocation().getDirection());
		}
	}

}
