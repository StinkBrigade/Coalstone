package kimera.really.works.coalstone.common.inventory.container.slots;

import kimera.really.works.coalstone.common.tileentities.CoalstoneCondenserTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class MachineOutputSlot extends Slot
{
	public MachineOutputSlot(IInventory inventoryIn, int index, int x, int y)
	{
		super(inventoryIn, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return CoalstoneCondenserTileEntity.isItemValidForOutputSlot(stack);
	}
}
