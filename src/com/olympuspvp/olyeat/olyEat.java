package com.olympuspvp.olyeat;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class olyEat extends JavaPlugin implements Listener{

	@Override
	public void onDisable(){

	}

	@Override
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(this, this);
		addRecipes();
		System.out.println("[olyEat] Recpies Loaded!");
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerEat(PlayerInteractEvent e){
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if(item == null) return;
		Material m = item.getType();
		Food f = Food.getFood(m);
		if(f == null) return;
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			boolean doHunger = false;
			if(p.getHealth() == 20){
				doHunger = true;
				if(p.getFoodLevel() == 20) return;
			}
			p.setItemInHand(p.getInventory().getItem(0));
			ItemMeta meta = item.getItemMeta();
			if(meta.getEnchantLevel(Enchantment.DIG_SPEED) == 1){
				if(!doHunger){
					e.setCancelled(true);
					int amountToHeal = f.getHealthBoost();
					int health = p.getHealth() + amountToHeal;
					if(health > 20) health = 20;
					p.setHealth(health);
					item.setItemMeta(null);
					item.setType(Material.CROPS);
					p.setItemInHand(item);
					p.updateInventory();
					p.setItemInHand(new ItemStack(Material.AIR));
					p.getWorld().playSound(p.getLocation(), Sound.EAT, 1f, 1f);
				}else{
					e.setCancelled(true);
					int amountToHeal = f.getHealthBoost();
					int hunger = p.getFoodLevel() + amountToHeal;
					if(hunger > 20){
						p.setSaturation(hunger - 20);
						hunger = 20;
					}p.setFoodLevel(hunger);
					item.setItemMeta(null);
					item.setType(Material.CROPS);
					p.setItemInHand(item);
					p.updateInventory();
					p.setItemInHand(new ItemStack(Material.AIR));
					p.getWorld().playSound(p.getLocation(), Sound.EAT, 1f, 1f);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerCraft(CraftItemEvent e){
		Inventory i = e.getInventory();
		try{
			ItemStack item = i.getItem(0);
			Random r = new Random();
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			if(lore.size() != 2) lore.add("r" + r.nextDouble());
			meta.setLore(lore);
			item.setItemMeta(meta);
		}catch(Exception exe){}
	}

	@EventHandler
	public void onPlayerAttemptStack(InventoryClickEvent e){
		if(e.getSlot() == 0){
			ItemStack item = e.getCurrentItem();
			if(!item.hasItemMeta()) return;
			if(item.getItemMeta().hasEnchant(Enchantment.DIG_SPEED)){
				Food food = Food.getFood(item.getType());
				if(food != null){
					if(e.isShiftClick()){
						final Player p = (Player) e.getWhoClicked();
						final Inventory inv = p.getInventory();
						Inventory ei = e.getInventory();
						int amount = 0;

						for(int i = 1; i < 10; i++){
							try{
								amount += ei.getItem(i).getAmount();
								ei.setItem(i, new ItemStack(Material.AIR));
							}catch(Exception exe){}
						}
						e.setResult(Result.DENY);
						int previousOpen = 0;
						for(int i = 0; i < amount; i++){
							int firstSlot = -1;
							for(int j = previousOpen; j <= 35; j++){
								try{
									if(inv.getItem(j).getAmount() == 0){
										firstSlot = j;
										previousOpen = j;
										break;
									}
								}catch(Exception exe){
									firstSlot = j;
								}
							}

							ItemStack toAdd = getEnchantedItemStack(item.getType());
							Random r = new Random();
							ItemMeta meta = toAdd.getItemMeta();
							List<String> lore = meta.getLore();
							if(lore.size() != 2) lore.add("r" + r.nextDouble());
							meta.setLore(lore);
							toAdd.setItemMeta(meta);

							if(firstSlot != -1){
								inv.setItem(firstSlot, toAdd);
							}else{
								p.getWorld().dropItem(p.getLocation(), toAdd);
							}
						}
					}
				}
			}
		}
	}

	public ItemStack getEnchantedItemStack(Material m){
		ItemStack item = new ItemStack(m, 1);
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DIG_SPEED, 1, false);
		ArrayList<String> lore = new ArrayList<String>();
		Food food = Food.getFood(m);
		lore.add("Instantly heals " + food.getNumberOfHearts() + " Hearts");
		meta.setLore(lore);
		meta.setDisplayName("Instant-Eat " + food.name);
		item.setItemMeta(meta);
		return item;
	}

	public void addRecipes(){
		for(Food f : Food.values()){
			Bukkit.addRecipe(new ShapelessRecipe(getEnchantedItemStack(f.material)).addIngredient(f.material));
		}
	}

}