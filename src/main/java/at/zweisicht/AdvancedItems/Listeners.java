package at.zweisicht.AdvancedItems;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Listeners  implements Listener
{
	AdvancedItems plugin;
	ItemManager itemManager;

	public Listeners(AdvancedItems plugin){
		this.plugin = plugin;
		itemManager = plugin.itemManager;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){

		if(e.getAction() == Action.RIGHT_CLICK_AIR || 
			e.getAction() == Action.RIGHT_CLICK_BLOCK){

				Player p = e.getPlayer();

			if(plugin.itemManager.isItem(itemManager.JETPACK, p.getItemInHand())){

				if(!e.getPlayer().hasPermission("MythicCraft.JetPack")) return;

				if(p.getInventory().getChestplate() != null){
					ItemStack chestplate = p.getInventory().getChestplate().clone();
					p.getEquipment().setChestplate(e.getPlayer().getItemInHand());
					p.setItemInHand(chestplate);
				}else{
					p.getEquipment().setChestplate(e.getPlayer().getItemInHand());
					p.setItemInHand(null);
				}

				p.updateInventory();
				p.setAllowFlight(true);
				e.setCancelled(true);
			}

			if(plugin.itemManager.isItem(itemManager.GLIDER, p.getItemInHand())){

				if(!e.getPlayer().hasPermission("MythicCraft.Glider")) return;

				itemManager.useGlider(p);
				e.setCancelled(true);
			}

		}
	}

	@EventHandler
	public void onPlayerEquip(InventoryClickEvent e){

		if(e.getSlotType() == InventoryType.SlotType.ARMOR){

			if(itemManager.jetPack.isUser((Player) e.getWhoClicked())){
				ItemStack itemStack = e.getCurrentItem();
				e.setCancelled(plugin.itemManager.isItem(itemManager.JETPACK,itemStack));
			}else{
				((Player) e.getWhoClicked()).setAllowFlight(false);
			}
		}
	}

	@EventHandler
	public void onBlockPleace(BlockPlaceEvent e){
		if(plugin.itemManager.isItem(itemManager.GLIDER, e.getPlayer().getItemInHand()))
			e.setCancelled(true);
	}


	@EventHandler
	public void onDeath(PlayerDeathEvent e){

		if(plugin.itemManager.isItem(itemManager.JETPACK, e.getEntity().getInventory().getChestplate())){
			e.getEntity().setAllowFlight(false);
		}

	}

	@EventHandler
	public void onFish(PlayerFishEvent e){

		if(e.getState() != PlayerFishEvent.State.FAILED_ATTEMPT && e.getState() != PlayerFishEvent.State.IN_GROUND) return;


		Player p = e.getPlayer();


		if(plugin.itemManager.isItem(itemManager.HOOK, p.getItemInHand())){

			if(!e.getPlayer().hasPermission("MythicCraft.Hook")) return;

			Location b_below = e.getHook().getLocation().subtract(new Vector(0,1,0));
			if(b_below.getBlock().getType() != Material.AIR){

				itemManager.useHook(p, e.getHook().getLocation());

			}
		}



	}

	@EventHandler
	public void onToggleFly(PlayerToggleFlightEvent e){

		if(plugin.itemManager.isItem(itemManager.JETPACK, e.getPlayer().getInventory().getChestplate())){
			plugin.itemManager.useJetPack(e.getPlayer());
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e){

		if(plugin.itemManager.isItem(itemManager.JETPACK, e.getPlayer().getInventory().getChestplate())){
			e.getPlayer().setAllowFlight(true);
		}
		
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){

		itemManager.glider.disableGlider(e.getPlayer());
		itemManager.jetPack.disableJetPack(e.getPlayer());
		
	}

	
}
