package kimera.really.works.coalstone.common.inventory.container;

import kimera.really.works.coalstone.common.inventory.container.slots.MachineFuelSlot;
import kimera.really.works.coalstone.common.inventory.container.slots.MachineGrinderSlot;
import kimera.really.works.coalstone.common.inventory.container.slots.MachineInputSlot;
import kimera.really.works.coalstone.common.inventory.container.slots.MachineOutputSlot;
import kimera.really.works.coalstone.common.tileentities.CoalstoneCondenserTileEntity;
import kimera.really.works.coalstone.common.tileentities.CoalstoneObliteratorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.MathHelper;

public class CoalstoneObliteratorContainer extends Container
{
    public static CoalstoneObliteratorContainer createContainerServerSide(int id, PlayerInventory playerInventory, IInventory obliteratorInventory, IIntArray progressData)
    {
        return new CoalstoneObliteratorContainer(id, playerInventory, obliteratorInventory, progressData);
    }

    public static CoalstoneObliteratorContainer createContainerClientSide(int id, PlayerInventory playerInventory, PacketBuffer extraData)
    {
        // Create a dummy inventory, since the TileEntity doesn't exist on the client :(
        IInventory obliteratorInventory = new Inventory(CoalstoneObliteratorTileEntity.SLOT_COUNT);
        IIntArray progressData = new IntArray(6);

        return new CoalstoneObliteratorContainer(id, playerInventory, obliteratorInventory, progressData);
    }

    // SLOTS!!!
    // 0 - 8: Player's Hotbar
    // 9 - 35: Player's Inventory
    // 36: Obliterator Fuel Slot(s)
    // 37: Obliterator Grinder Slot(s)
    // 38: Obliterator Input Slot(s)
    // 39 - 40: Obliterator Output Slot(s)

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INV_ROW_COUNT = 3;
    private static final int PLAYER_INV_COLUMN_COUNT = 9;
    private static final int PLAYER_INV_SLOT_COUNT = PLAYER_INV_ROW_COUNT * PLAYER_INV_COLUMN_COUNT;
    private static final int PLAYER_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INV_SLOT_COUNT;

    private static final int OBLITERATOR_FUEL_SLOT_COUNT = CoalstoneObliteratorTileEntity.FUEL_SLOT_COUNT;
    private static final int OBLITERATOR_GRINDER_SLOT_COUNT = CoalstoneObliteratorTileEntity.GRINDER_SLOT_COUNT;
    private static final int OBLITERATOR_INPUT_SLOT_COUNT = CoalstoneObliteratorTileEntity.INPUT_SLOT_COUNT;
    private static final int OBLITERATOR_OUTPUT_SLOT_COUNT = CoalstoneObliteratorTileEntity.OUTPUT_SLOT_COUNT;
    private static final int OBLITERATOR_SLOT_COUNT = CoalstoneObliteratorTileEntity.SLOT_COUNT;

    private static final int PLAYER_FIRST_SLOT_INDEX = 0;
    private static final int HOTBAR_FIRST_SLOT_INDEX = PLAYER_FIRST_SLOT_INDEX;
    private static final int PLAYER_INV_FIRST_SLOT_INDEX = HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT;
    private static final int OBLITERATOR_FIRST_FUEL_SLOT_INDEX = PLAYER_INV_FIRST_SLOT_INDEX + PLAYER_INV_SLOT_COUNT;
    private static final int OBLITERATOR_FIRST_GRINDER_SLOT_INDEX = OBLITERATOR_FIRST_FUEL_SLOT_INDEX + OBLITERATOR_FUEL_SLOT_COUNT;
    private static final int OBLITERATOR_FIRST_INPUT_SLOT_INDEX = OBLITERATOR_FIRST_GRINDER_SLOT_INDEX + OBLITERATOR_GRINDER_SLOT_COUNT;
    private static final int OBLITERATOR_FIRST_OUTPUT_SLOT_INDEX = OBLITERATOR_FIRST_INPUT_SLOT_INDEX + OBLITERATOR_INPUT_SLOT_COUNT;

    private enum SlotZone
    {
        FUEL(OBLITERATOR_FIRST_FUEL_SLOT_INDEX, OBLITERATOR_FUEL_SLOT_COUNT),
        GRINDER(OBLITERATOR_FIRST_GRINDER_SLOT_INDEX, OBLITERATOR_GRINDER_SLOT_COUNT),
        INPUT(OBLITERATOR_FIRST_INPUT_SLOT_INDEX, OBLITERATOR_INPUT_SLOT_COUNT),
        OUTPUT(OBLITERATOR_FIRST_OUTPUT_SLOT_INDEX, OBLITERATOR_OUTPUT_SLOT_COUNT),
        PLAYER_HOTBAR(HOTBAR_FIRST_SLOT_INDEX, HOTBAR_SLOT_COUNT),
        PLAYER_INV(PLAYER_INV_FIRST_SLOT_INDEX, PLAYER_INV_SLOT_COUNT);

        public final int firstIndex;
        public final int slotCount;
        public final int nextZoneFirstIndex;

        SlotZone(int firstIndex, int slotCount)
        {
            this.firstIndex = firstIndex;
            this.slotCount = slotCount;
            this.nextZoneFirstIndex = firstIndex + slotCount;
        }

        public static CoalstoneObliteratorContainer.SlotZone getZoneFromIndex(int slotIndex)
        {
            for(int i = 0; i < CoalstoneObliteratorContainer.SlotZone.values().length; i++)
            {
                CoalstoneObliteratorContainer.SlotZone slotZone = CoalstoneObliteratorContainer.SlotZone.values()[i];
                if(slotIndex >= slotZone.firstIndex && slotIndex < slotZone.nextZoneFirstIndex)
                {
                    return slotZone;
                }
            }

            throw new IndexOutOfBoundsException("Index " + slotIndex + " out of bounds.");
        }
    }

    private static final int[][] OBLITERATOR_SLOTS_XPOS =
            {
                    { 8 },
                    { 55 },
                    { 46, 64 },
                    { 116, 116 }
            };
    private static final int[][] OBLITERATOR_SLOTS_YPOS =
            {
                    { 53 },
                    { 49 },
                    { 21, 21 },
                    { 35, 59 }
            };
    private static final int PLAYER_INV_XPOS = 8;
    private static final int PLAYER_INV_YPOS = 84;
    private static final int PLAYER_HOTBAR_YPOS = 142;

    private final IInventory obliteratorInventory;
    private final IIntArray progressData;

    protected CoalstoneObliteratorContainer(int id, PlayerInventory playerInventory, IInventory obliteratorInventory, IIntArray progressData)
    {
        super(ContainerRegistry.COALSTONE_OBLITERATOR.get(), id);

        assertInventorySize(obliteratorInventory, OBLITERATOR_SLOT_COUNT);
        assertIntArraySize(progressData, 6);

        this.obliteratorInventory = obliteratorInventory;
        this.progressData = progressData;

        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;

        for(int hotbarX = 0; hotbarX < HOTBAR_SLOT_COUNT; hotbarX++)
        {
            int slotNumber = hotbarX;
            addSlot(new Slot(playerInventory, slotNumber, PLAYER_INV_XPOS + (SLOT_X_SPACING * slotNumber), PLAYER_HOTBAR_YPOS));
        }

        for(int playerInvY = 0; playerInvY < PLAYER_INV_ROW_COUNT; playerInvY++)
        {
            for(int playerInvX = 0; playerInvX < PLAYER_INV_COLUMN_COUNT; playerInvX++)
            {
                int slotNumber = HOTBAR_SLOT_COUNT + (playerInvY * PLAYER_INV_COLUMN_COUNT) + playerInvX;
                int xPos = PLAYER_INV_XPOS + (playerInvX * SLOT_X_SPACING);
                int yPos = PLAYER_INV_YPOS + (playerInvY * SLOT_Y_SPACING);
                addSlot(new Slot(playerInventory, slotNumber, xPos, yPos));
            }
        }

        for(int obliteratorFuelSlotIndex = 0; obliteratorFuelSlotIndex < OBLITERATOR_FUEL_SLOT_COUNT; obliteratorFuelSlotIndex++)
        {
            int slotNumber = obliteratorFuelSlotIndex;
            addSlot(new MachineFuelSlot(obliteratorInventory, slotNumber, OBLITERATOR_SLOTS_XPOS[0][obliteratorFuelSlotIndex], OBLITERATOR_SLOTS_YPOS[0][obliteratorFuelSlotIndex]));
        }

        for(int obliteratorGrinderSlotIndex = 0; obliteratorGrinderSlotIndex < OBLITERATOR_GRINDER_SLOT_COUNT; obliteratorGrinderSlotIndex++)
        {
            int slotNumber = OBLITERATOR_FIRST_GRINDER_SLOT_INDEX - PLAYER_SLOT_COUNT + obliteratorGrinderSlotIndex;
            addSlot(new MachineGrinderSlot(obliteratorInventory, slotNumber, OBLITERATOR_SLOTS_XPOS[1][obliteratorGrinderSlotIndex], OBLITERATOR_SLOTS_YPOS[1][obliteratorGrinderSlotIndex]));
        }

        for(int obliteratorInputSlotIndex = 0; obliteratorInputSlotIndex < OBLITERATOR_INPUT_SLOT_COUNT; obliteratorInputSlotIndex++)
        {
            int slotNumber = OBLITERATOR_FIRST_INPUT_SLOT_INDEX - PLAYER_SLOT_COUNT + obliteratorInputSlotIndex;
            addSlot(new MachineInputSlot(obliteratorInventory, slotNumber, OBLITERATOR_SLOTS_XPOS[2][obliteratorInputSlotIndex], OBLITERATOR_SLOTS_YPOS[2][obliteratorInputSlotIndex]));
        }

        for(int obliteratorOutputSlotIndex = 0; obliteratorOutputSlotIndex < OBLITERATOR_OUTPUT_SLOT_COUNT; obliteratorOutputSlotIndex++)
        {
            int slotNumber = OBLITERATOR_FIRST_OUTPUT_SLOT_INDEX - PLAYER_SLOT_COUNT + obliteratorOutputSlotIndex;
            addSlot(new MachineOutputSlot(obliteratorInventory, slotNumber, OBLITERATOR_SLOTS_XPOS[3][obliteratorOutputSlotIndex], OBLITERATOR_SLOTS_YPOS[3][obliteratorOutputSlotIndex]));
        }

        this.trackIntArray(progressData);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return this.obliteratorInventory.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerEntity, int sourceSlotIndex)
    {
        Slot sourceSlot = this.inventorySlots.get(sourceSlotIndex);
        if(sourceSlot != null && sourceSlot.getHasStack())
        {
            ItemStack sourceItemStack = sourceSlot.getStack();
            ItemStack sourceItemStackBeforeMerge = sourceItemStack.copy();
            boolean transferSuccessful = false;

            CoalstoneObliteratorContainer.SlotZone sourceZone = CoalstoneObliteratorContainer.SlotZone.getZoneFromIndex(sourceSlotIndex);

            switch(sourceZone)
            {
                case OUTPUT:
                    transferSuccessful = this.mergeInto(CoalstoneObliteratorContainer.SlotZone.PLAYER_HOTBAR, sourceItemStack, true);
                    if(!transferSuccessful)
                    {
                        transferSuccessful = this.mergeInto(CoalstoneObliteratorContainer.SlotZone.PLAYER_INV, sourceItemStack, true);
                    }
                    if(transferSuccessful)
                    {
                        sourceSlot.onSlotChange(sourceItemStack, sourceItemStackBeforeMerge);
                    }
                    break;

                case FUEL:
                case GRINDER:
                case INPUT:
                    transferSuccessful = this.mergeInto(CoalstoneObliteratorContainer.SlotZone.PLAYER_INV, sourceItemStack, false);
                    if(!transferSuccessful)
                    {
                        transferSuccessful = this.mergeInto(CoalstoneObliteratorContainer.SlotZone.PLAYER_HOTBAR, sourceItemStack, false);
                    }
                    break;
                case PLAYER_HOTBAR:
                case PLAYER_INV:
                    if(CoalstoneObliteratorTileEntity.isFuel(sourceItemStack))
                    {
                        transferSuccessful = this.mergeInto(CoalstoneObliteratorContainer.SlotZone.FUEL, sourceItemStack, false);
                    }
                    if(!transferSuccessful && CoalstoneObliteratorTileEntity.isGrinder(sourceItemStack))
                    {
                        transferSuccessful = this.mergeInto(CoalstoneObliteratorContainer.SlotZone.GRINDER, sourceItemStack, false);
                    }
                    if(!transferSuccessful)
                    {
                        transferSuccessful = this.mergeInto(CoalstoneObliteratorContainer.SlotZone.INPUT, sourceItemStack, false);
                    }
                    if(!transferSuccessful)
                    {
                        if(sourceZone == CoalstoneObliteratorContainer.SlotZone.PLAYER_HOTBAR)
                        {
                            transferSuccessful = this.mergeInto(CoalstoneObliteratorContainer.SlotZone.PLAYER_INV, sourceItemStack, false);
                        }
                        else
                        {
                            transferSuccessful = this.mergeInto(CoalstoneObliteratorContainer.SlotZone.PLAYER_HOTBAR, sourceItemStack, false);
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected source Slot Zone: " + sourceZone);
            }

            if(transferSuccessful)
            {
                if(sourceItemStack.isEmpty())
                {
                    sourceSlot.putStack(ItemStack.EMPTY);
                }
                else
                {
                    sourceSlot.onSlotChanged();
                }

                if(sourceItemStack.getCount() != sourceItemStackBeforeMerge.getCount())
                {
                    sourceSlot.onTake(playerEntity, sourceItemStack);
                    return sourceItemStackBeforeMerge;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    private boolean mergeInto(CoalstoneObliteratorContainer.SlotZone destinationZone, ItemStack sourceItemStack, boolean fillFromEnd)
    {
        return this.mergeItemStack(sourceItemStack, destinationZone.firstIndex, destinationZone.nextZoneFirstIndex, fillFromEnd);
    }

    @Override
    public void onContainerClosed(PlayerEntity playerEntityIn)
    {
        super.onContainerClosed(playerEntityIn);
    }

    public double getBurnTimeFraction()
    {
        if(progressData.get(1) <= 0) return 0;
        double fraction = ((double) progressData.get(0)) / ((double) progressData.get(1));
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

    public double getBurnTimeSeconds()
    {
        if(progressData.get(1) <= 0) return 0;
        return ((double) progressData.get(1)) / 20.0;
    }

    public double getGrindAmountFraction()
    {
        if(progressData.get(3) <= 0) return 0;
        double fraction = ((double) progressData.get(2)) / ((double) progressData.get(3));
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

    public double getCookTimeFraction()
    {
        if(progressData.get(4) == 0) return 0;
        double fraction = ((double) progressData.get(4)) / ((double) progressData.get(5));
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

    public double getRemainingCookTimeSeconds()
    {
        if(progressData.get(4) == 0) return 0;
        return ((double) (progressData.get(5) - progressData.get(4))) / 20.0;
    }
}
