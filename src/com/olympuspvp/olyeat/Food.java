package com.olympuspvp.olyeat;
import org.bukkit.Material;

public enum Food{
	BAKED_POTATO(Material.BAKED_POTATO, 1.5, "Baked Potato"),
	CAKE(Material.CAKE, 4, "Cake"),
	CARROT(Material.CARROT_ITEM, 1, "Carrot"),
	COOKED_CHICKEN(Material.COOKED_CHICKEN, 2.5, "Cooked Chicken"),
	COOKED_FISH(Material.COOKED_FISH, 2.5, "Cooked Fish"),
	GRILLED_PORK(Material.GRILLED_PORK, 3, "Cooked Pork"),
	COOKIE(Material.COOKIE, .5, "Cookie"),
	GOLDEN_APPLE(Material.GOLDEN_APPLE, 2, "Golden Apple"),
	MELON(Material.MELON, .5, "Melon"),
	PUMPKIN_PIE(Material.PUMPKIN_PIE, 3, "Pumpkin Pie"),
	APPLE(Material.APPLE, 1.5, "Apple"),
	COOKED_BEEF(Material.COOKED_BEEF, 3, "Cooked Beef"),
	BREAD(Material.BREAD, 1.5, "Bread");
	
	public final Material material;
	final double numberOfHearts;
	public final String name;
	
	private Food(Material m, double hearts, String itemname){
		material = m;
		numberOfHearts = hearts;
		name = itemname;
	}
	
	public double getNumberOfHearts(){
		return numberOfHearts;
	}
	
	public int getHealthBoost(){
		return (int)Math.round(numberOfHearts * 2);
	}
	
	public static Food getFood(Material m){
		Food food = null;
		for(Food f : Food.values()){
			if(f.material.equals(m)){
				food = f;
				break;
			}
		}return food;
	}
	
}
