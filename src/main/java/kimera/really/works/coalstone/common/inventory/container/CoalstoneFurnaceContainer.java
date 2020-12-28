package kimera.really.works.coalstone.common.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.util.IIntArray;

public class CoalstoneFurnaceContainer extends AbstractFurnaceContainer
{
	public CoalstoneFurnaceContainer(int id, PlayerInventory playerInventoryIn)
	{
		super(ContainerRegistry.COALSTONE_FURNACE.get(), IRecipeType.SMELTING, RecipeBookCategory.FURNACE, id, playerInventoryIn);
	}

	public CoalstoneFurnaceContainer(int id, PlayerInventory playerInventoryIn, IInventory furnaceInventoryIn, IIntArray p_i50083_4_)
	{
		super(ContainerRegistry.COALSTONE_FURNACE.get(), IRecipeType.SMELTING, RecipeBookCategory.FURNACE, id, playerInventoryIn, furnaceInventoryIn, p_i50083_4_);
	}
}