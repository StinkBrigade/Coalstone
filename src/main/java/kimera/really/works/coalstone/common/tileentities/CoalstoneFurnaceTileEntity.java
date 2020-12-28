package kimera.really.works.coalstone.common.tileentities;

import kimera.really.works.coalstone.common.inventory.container.CoalstoneFurnaceContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CoalstoneFurnaceTileEntity extends AbstractFurnaceTileEntity
{
	public CoalstoneFurnaceTileEntity()
	{
		super(TileEntityRegistry.COALSTONE_FURNACE.get(), IRecipeType.SMELTING);
	}

	@Override
	protected ITextComponent getDefaultName() 
	{
		return new TranslationTextComponent("container.coalstone.coalstone_furnace");
	}
	
	@Override
	protected int getBurnTime(ItemStack stack)
	{
		return (int)(super.getBurnTime(stack) * 1.25F);
	}
	
	@Override
	protected int getCookTime()
	{
		return (int)(super.getCookTime() / 1.2F);
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player)
	{
		return new CoalstoneFurnaceContainer(id, player, this, this.furnaceData);
	}
}
