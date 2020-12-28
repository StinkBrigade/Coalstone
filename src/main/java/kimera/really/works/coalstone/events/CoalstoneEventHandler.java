package kimera.really.works.coalstone.events;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CoalstoneEventHandler
{
	@SubscribeEvent
	public void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event)
	{
		ItemStack fuelItemStack = event.getItemStack();
		Item fuelItem = fuelItemStack.getItem();

		String registryNamespace = fuelItem.getRegistryName().getNamespace();
		String registryPath = fuelItem.getRegistryName().getPath();
		
		if(registryNamespace.equals("coalstone"))
		{
			switch(registryPath)
			{
				case "coalstone_brick_slab":
				case "coalstone_tile_slab":
					event.setBurnTime(800);
					break;
				case "coalstone_clump":
				case "coalstone_shard":
				case "coalstone_sword":
				case "coalstone_shovel":
				case "coalstone_pickaxe":
				case "coalstone_axe":
				case "coalstone_hoe":
				case "coalstone_brick_stairs":
				case "coalstone_tile_stairs":
					event.setBurnTime(1600);
					break;
				case "coalstone_bricks":
				case "coalstone_tiles":
				case "coalstone_furnace":
					event.setBurnTime(6400);
					break;
			}
		}
	}
}
