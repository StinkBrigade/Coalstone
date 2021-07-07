package kimera.really.works.coalstone.common.inventory.container.slots;

import kimera.really.works.coalstone.common.tileentities.CoalstoneObliteratorTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class MachineGrinderSlot extends Slot
{
    public MachineGrinderSlot(IInventory inventoryIn, int index, int x, int y)
    {
        super(inventoryIn, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return CoalstoneObliteratorTileEntity.isItemValidForGrinderSlot(stack);
    }
}