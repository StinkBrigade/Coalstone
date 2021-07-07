package kimera.really.works.coalstone.common.inventory.container;

import org.apache.logging.log4j.Level;

import kimera.really.works.coalstone.Coalstone;
import kimera.really.works.coalstone.common.inventory.container.slots.MachineFuelSlot;
import kimera.really.works.coalstone.common.inventory.container.slots.MachineInputSlot;
import kimera.really.works.coalstone.common.inventory.container.slots.MachineOutputSlot;
import kimera.really.works.coalstone.common.tileentities.CoalstoneCondenserTileEntity;
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

public class CoalstoneCondenserContainer extends Container
{
	public static CoalstoneCondenserContainer createContainerServerSide(int id, PlayerInventory playerInventory, IInventory condenserInventory, IIntArray progressData)
	{
		return new CoalstoneCondenserContainer(id, playerInventory, condenserInventory, progressData);
	}
	
	public static CoalstoneCondenserContainer createContainerClientSide(int id, PlayerInventory playerInventory, PacketBuffer extraData)
	{
		// Create a dummy inventory, since the TileEntity doesn't exist on the client :(
		IInventory condenserInventory = new Inventory(CoalstoneCondenserTileEntity.SLOT_COUNT);
		IIntArray progressData = new IntArray(4);
		
		return new CoalstoneCondenserContainer(id, playerInventory, condenserInventory, progressData);
	}
	
	// SLOTS!!!
	// 0 - 8: Player's Hotbar
	// 9 - 35: Player's Inventory
	// 36: Condenser Fuel Slot(s)
	// 37 - 41: Condenser Input Slot(s)
	// 42: Condenser Output Slot(s)
	
	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INV_ROW_COUNT = 3;
	private static final int PLAYER_INV_COLUMN_COUNT = 9;
	private static final int PLAYER_INV_SLOT_COUNT = PLAYER_INV_ROW_COUNT * PLAYER_INV_COLUMN_COUNT;
	private static final int PLAYER_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INV_SLOT_COUNT;

	private static final int CONDENSER_FUEL_SLOT_COUNT = CoalstoneCondenserTileEntity.FUEL_SLOT_COUNT;
	private static final int CONDENSER_INPUT_SLOT_COUNT = CoalstoneCondenserTileEntity.INPUT_SLOT_COUNT;
	private static final int CONDENSER_OUTPUT_SLOT_COUNT = CoalstoneCondenserTileEntity.OUTPUT_SLOT_COUNT;
	private static final int CONDENSER_SLOT_COUNT = CoalstoneCondenserTileEntity.SLOT_COUNT;
	
	private static final int PLAYER_FIRST_SLOT_INDEX = 0;
	private static final int HOTBAR_FIRST_SLOT_INDEX = PLAYER_FIRST_SLOT_INDEX;
	private static final int PLAYER_INV_FIRST_SLOT_INDEX = HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT;
	private static final int CONDENSER_FIRST_FUEL_SLOT_INDEX = PLAYER_INV_FIRST_SLOT_INDEX + PLAYER_INV_SLOT_COUNT;
	private static final int CONDENSER_FIRST_INPUT_SLOT_INDEX = CONDENSER_FIRST_FUEL_SLOT_INDEX + CONDENSER_FUEL_SLOT_COUNT;
	private static final int CONDENSER_FIRST_OUTPUT_SLOT_INDEX = CONDENSER_FIRST_INPUT_SLOT_INDEX + CONDENSER_INPUT_SLOT_COUNT;
	
	private enum SlotZone
	{
		FUEL(CONDENSER_FIRST_FUEL_SLOT_INDEX, CONDENSER_FUEL_SLOT_COUNT),
		INPUT(CONDENSER_FIRST_INPUT_SLOT_INDEX, CONDENSER_INPUT_SLOT_COUNT),
		OUTPUT(CONDENSER_FIRST_OUTPUT_SLOT_INDEX, CONDENSER_OUTPUT_SLOT_COUNT),
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
		
		public static SlotZone getZoneFromIndex(int slotIndex)
		{
			for(int i = 0; i < SlotZone.values().length; i++)
			{
				SlotZone slotZone = SlotZone.values()[i];
				if(slotIndex >= slotZone.firstIndex && slotIndex < slotZone.nextZoneFirstIndex)
				{
					return slotZone;
				}
			}
			
			throw new IndexOutOfBoundsException("Index " + slotIndex + " out of bounds.");
		}
	}

	private static final int[][] CONDENSER_SLOTS_XPOS =
	{
		{ 8 },
		{ 55, 37, 55, 73, 55 },
		{ 133 }
	};
	private static final int[][] CONDENSER_SLOTS_YPOS =
	{
		{ 53 },
		{ 17, 35, 35, 35, 53 },
		{ 35 }
	};
	private static final int PLAYER_INV_XPOS = 8;
	private static final int PLAYER_INV_YPOS = 84;
	private static final int PLAYER_HOTBAR_YPOS = 142;
	
	private final IInventory condenserInventory;
	private final IIntArray progressData;
	
	protected CoalstoneCondenserContainer(int id, PlayerInventory playerInventory, IInventory condenserInventory, IIntArray progressData)
	{
		super(ContainerRegistry.COALSTONE_CONDENSER.get(), id);
		
		assertInventorySize(condenserInventory, CONDENSER_SLOT_COUNT);
		assertIntArraySize(progressData, 4);
		
		this.condenserInventory = condenserInventory;
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
		
		for(int condenserFuelSlotIndex = 0; condenserFuelSlotIndex < CONDENSER_FUEL_SLOT_COUNT; condenserFuelSlotIndex++)
		{
			int slotNumber = condenserFuelSlotIndex;
			addSlot(new MachineFuelSlot(condenserInventory, slotNumber, CONDENSER_SLOTS_XPOS[0][condenserFuelSlotIndex], CONDENSER_SLOTS_YPOS[0][condenserFuelSlotIndex]));
		}
		
		for(int condenserInputSlotIndex = 0; condenserInputSlotIndex < CONDENSER_INPUT_SLOT_COUNT; condenserInputSlotIndex++)
		{
			int slotNumber = CONDENSER_FIRST_INPUT_SLOT_INDEX - PLAYER_SLOT_COUNT + condenserInputSlotIndex;
			addSlot(new MachineInputSlot(condenserInventory, slotNumber, CONDENSER_SLOTS_XPOS[1][condenserInputSlotIndex], CONDENSER_SLOTS_YPOS[1][condenserInputSlotIndex]));
		}
		
		for(int condenserOutputSlotIndex = 0; condenserOutputSlotIndex < CONDENSER_OUTPUT_SLOT_COUNT; condenserOutputSlotIndex++)
		{
			int slotNumber = CONDENSER_FIRST_OUTPUT_SLOT_INDEX - PLAYER_SLOT_COUNT + condenserOutputSlotIndex;
			addSlot(new MachineOutputSlot(condenserInventory, slotNumber, CONDENSER_SLOTS_XPOS[2][condenserOutputSlotIndex], CONDENSER_SLOTS_YPOS[2][condenserOutputSlotIndex]));
		}
		
		this.trackIntArray(progressData);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return this.condenserInventory.isUsableByPlayer(playerIn);
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
			
			SlotZone sourceZone = SlotZone.getZoneFromIndex(sourceSlotIndex);
			
			switch(sourceZone)
			{
				case OUTPUT:
					transferSuccessful = this.mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, true);
					if(!transferSuccessful)
					{
						transferSuccessful = this.mergeInto(SlotZone.PLAYER_INV, sourceItemStack, true);
					}
					if(transferSuccessful)
					{
						sourceSlot.onSlotChange(sourceItemStack, sourceItemStackBeforeMerge);
					}
					break;
					
				case FUEL:
				case INPUT:
					transferSuccessful = this.mergeInto(SlotZone.PLAYER_INV, sourceItemStack, false);
					if(!transferSuccessful)
					{
						transferSuccessful = this.mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, false);
					}
					break;
				case PLAYER_HOTBAR:
				case PLAYER_INV:
					if(CoalstoneCondenserTileEntity.isFuel(sourceItemStack))
					{
						transferSuccessful = this.mergeInto(SlotZone.FUEL, sourceItemStack, false);
					}
					if(!transferSuccessful)
					{
						transferSuccessful = this.mergeInto(SlotZone.INPUT, sourceItemStack, false);
					}
					if(!transferSuccessful)
					{
						if(sourceZone == SlotZone.PLAYER_HOTBAR)
						{
							transferSuccessful = this.mergeInto(SlotZone.PLAYER_INV, sourceItemStack, false);
						}
						else
						{
							transferSuccessful = this.mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, false);
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
	
	private boolean mergeInto(SlotZone destinationZone, ItemStack sourceItemStack, boolean fillFromEnd)
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
	
	public double getCookTimeFraction()
	{
		if(progressData.get(2) == 0) return 0;
		double fraction = ((double) progressData.get(2)) / ((double) progressData.get(3));
		return MathHelper.clamp(fraction, 0.0, 1.0);
	}
	
	public double getRemainingCookTimeSeconds()
	{
		if(progressData.get(2) == 0) return 0;
		return ((double) (progressData.get(3) - progressData.get(2))) / 20.0;
	}
}
