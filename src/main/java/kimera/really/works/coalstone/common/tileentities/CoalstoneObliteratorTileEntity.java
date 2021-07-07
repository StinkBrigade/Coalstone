package kimera.really.works.coalstone.common.tileentities;

import kimera.really.works.coalstone.common.blocks.CoalstoneObliteratorBlock;
import kimera.really.works.coalstone.common.inventory.container.CoalstoneObliteratorContainer;
import kimera.really.works.coalstone.common.recipes.CoalstoneObliteratorRecipe;
import kimera.really.works.coalstone.common.recipes.RecipeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

public class CoalstoneObliteratorTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity
{
    public static final int FUEL_SLOT_COUNT = 1;
    public static final int GRINDER_SLOT_COUNT = 1;
    public static final int INPUT_SLOT_COUNT = 2;
    public static final int OUTPUT_SLOT_COUNT = 2;
    public static final int SLOT_COUNT = FUEL_SLOT_COUNT + GRINDER_SLOT_COUNT + INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT;

    public static final int FIRST_FUEL_SLOT_INDEX = 0;
    public static final int FIRST_GRINDER_SLOT_INDEX = FIRST_FUEL_SLOT_INDEX + FUEL_SLOT_COUNT;
    public static final int FIRST_INPUT_SLOT_INDEX = FIRST_GRINDER_SLOT_INDEX + GRINDER_SLOT_COUNT;
    public static final int FIRST_OUTPUT_SLOT_INDEX = FIRST_INPUT_SLOT_INDEX + INPUT_SLOT_COUNT;

    // SLOTS_UP: Slot 2 - 3 (input slots)
    // SLOTS_DOWN: Slots 4 - 5 & 0 - 1 (reverse order, prioritise extracting output, then if there's anything extractable in the fuel / grinder slots)
    // SLOTS_HORIZONTAL: Slots 0 - 1 (fuel & grinder slots), with front face prioritising grinder slot instead
    private static final int[] SLOTS_UP = { 2, 3 };
    private static final int[] SLOTS_DOWN = { 4, 5, 0, 1 };
    private static final int[][] SLOTS_HORIZONTAL =
    {
            {1, 0},
            {0, 1},
            {0, 1},
            {0, 1}
    };

    protected NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    private int currentBurnTime;
    private int totalBurnTime;
    private int currentGrindAmount;
    private int totalGrindAmount;
    private int currentCookTime;
    private int totalCookTime = 200;

    protected final IIntArray obliteratorData = new IIntArray()
    {
        @Override
        public int get(int index)
        {
            switch(index)
            {
                case 0:
                    return CoalstoneObliteratorTileEntity.this.currentBurnTime;
                case 1:
                    return CoalstoneObliteratorTileEntity.this.totalBurnTime;
                case 2:
                    return CoalstoneObliteratorTileEntity.this.currentGrindAmount;
                case 3:
                    return CoalstoneObliteratorTileEntity.this.totalGrindAmount;
                case 4:
                    return CoalstoneObliteratorTileEntity.this.currentCookTime;
                case 5:
                    return CoalstoneObliteratorTileEntity.this.totalCookTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value)
        {
            switch(index)
            {
                case 0:
                    CoalstoneObliteratorTileEntity.this.currentBurnTime = value;
                    break;
                case 1:
                    CoalstoneObliteratorTileEntity.this.totalBurnTime = value;
                    break;
                case 2:
                    CoalstoneObliteratorTileEntity.this.currentGrindAmount = value;
                    break;
                case 3:
                    CoalstoneObliteratorTileEntity.this.totalGrindAmount = value;
                    break;
                case 4:
                    CoalstoneObliteratorTileEntity.this.currentCookTime = value;
                    break;
                case 5:
                    CoalstoneObliteratorTileEntity.this.totalCookTime = value;
                    break;
            }
        }

        @Override
        public int size()
        {
            return 6;
        }
    };

    public CoalstoneObliteratorTileEntity()
    {
        super(TileEntityRegistry.COALSTONE_OBLITERATOR.get());
    }

    public boolean isBurning()
    {
        return this.currentBurnTime > 0;
    }

    public boolean isGrinding()
    {
        return this.currentGrindAmount > 0;
    }

    public boolean isCooking()
    {
        return this.totalCookTime > 0;
    }

    public void setBurnTime(int burnTime)
    {
        this.currentBurnTime = burnTime;
        this.totalBurnTime = burnTime;
    }

    public void setGrindTime(int grindTime)
    {
        this.currentGrindAmount = grindTime;
        this.totalGrindAmount = grindTime;
    }

    public void setCookTime(int cookTime)
    {
        this.currentCookTime = 0;
        this.totalCookTime = cookTime;
    }

    @Override
    public ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("container.coalstone.coalstone_obliterator");
    }

    @Override @Nullable
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return new SUpdateTileEntityPacket(this.pos, getTileEntityId(), nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SUpdateTileEntityPacket packet)
    {
        BlockState blockState = world.getBlockState(pos);
        read(blockState, packet.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        this.read(blockState, parentNBTTagCompound);
    }

    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        super.read(blockState, parentNBTTagCompound);

        this.items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(parentNBTTagCompound, items);

        this.currentBurnTime = parentNBTTagCompound.getInt("currentBurnTime");
        this.totalBurnTime = parentNBTTagCompound.getInt("totalBurnTime");

        this.currentGrindAmount = parentNBTTagCompound.getInt("currentGrindAmount");
        this.totalGrindAmount = parentNBTTagCompound.getInt("totalGrindAmount");

        this.currentCookTime = parentNBTTagCompound.getInt("curentCookTime");
        this.totalCookTime = parentNBTTagCompound.getInt("totalCookTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound);

        ItemStackHelper.saveAllItems(parentNBTTagCompound, items);

        parentNBTTagCompound.putInt("currentBurnTime", this.currentBurnTime);
        parentNBTTagCompound.putInt("totalBurnTime", this.totalBurnTime);

        parentNBTTagCompound.putInt("currentGrindAmount", this.currentGrindAmount);
        parentNBTTagCompound.putInt("totalGrindAmount", this.totalGrindAmount);

        parentNBTTagCompound.putInt("curentCookTime", this.currentCookTime);
        parentNBTTagCompound.putInt("totalCookTime", this.totalCookTime);

        return parentNBTTagCompound;
    }

    @Override
    public void tick()
    {
        if(!this.hasWorld()) return;
        World world = this.world;
        if(world.isRemote) return;

        boolean wasBurningFlag = this.isBurning();
        boolean stateChangeFlag = false;

        if(this.isBurning())
        {
            currentBurnTime--;
        }

        // If the obliterator is currently burning fuel, or if it has an item in a fuel / input slot
        if(this.isBurning() || !isSlotRangeEmpty(FIRST_FUEL_SLOT_INDEX, FIRST_OUTPUT_SLOT_INDEX))
        {
            IRecipe<?> recipe = this.world.getRecipeManager().getRecipe(RecipeRegistry.COALSTONE_OBLITERATOR_RECIPE_TYPE, this, this.world).orElse(null);
            boolean canObliterate = this.canObliterate(recipe);

            if(!this.isBurning() && canObliterate)
            {
                this.setBurnTime(ForgeHooks.getBurnTime(this.getStackInSlot(0)));
                if(this.isBurning())
                {
                    this.decrStackSize(0, 1);

                    stateChangeFlag = true;
                }
            }

            if(this.isBurning() && canObliterate)
            {
                this.currentCookTime++;
                if(this.currentCookTime >= this.totalCookTime && recipe instanceof CoalstoneObliteratorRecipe)
                {
                    final CoalstoneObliteratorRecipe obliteratorRecipe = (CoalstoneObliteratorRecipe) recipe;

                    this.setCookTime(this.getRecipeCookTime());

                    this.consumeIngredients();
                    this.addOutput(obliteratorRecipe.getCraftingResult(this));

                    //TODO: Add stat for finishing an Obliterator recipe
                }
            }
            else
            {
                this.currentCookTime = 0;
            }
        }
        else if(!this.isBurning() && this.currentCookTime > 0)
        {
            this.currentCookTime = MathHelper.clamp(this.currentCookTime - 2, 0, this.totalCookTime);
        }

        if(wasBurningFlag != this.isBurning())
        {
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(CoalstoneObliteratorBlock.LIT, Boolean.valueOf(this.isBurning())), 3);

            stateChangeFlag = true;
        }

        if(stateChangeFlag)
        {
            this.markDirty();
        }
    }

    private int getRecipeCookTime()
    {
        return this.world.getRecipeManager().getRecipe(RecipeRegistry.COALSTONE_OBLITERATOR_RECIPE_TYPE, this, this.world).map(CoalstoneObliteratorRecipe::getCookTime).orElse(200);
    }

    private void addOutput(ItemStack output)
    {
        for(int outputIndex = FIRST_OUTPUT_SLOT_INDEX; outputIndex < FIRST_OUTPUT_SLOT_INDEX + OUTPUT_SLOT_COUNT; outputIndex++)
        {
            switch(this.isSlotTaken(outputIndex, output))
            {
                case 1:
                    ItemStack outputSlotItemStack = this.items.get(outputIndex);
                    outputSlotItemStack.grow(output.getCount());
                    return;
                case 2:
                    this.items.set(outputIndex, output);
                    return;
            }
        }
    }

    private void consumeIngredients()
    {
        for(int inputIndex = FIRST_INPUT_SLOT_INDEX; inputIndex < FIRST_OUTPUT_SLOT_INDEX; inputIndex++)
        {
            ItemStack inputSlotStack = this.items.get(inputIndex);
            if(inputSlotStack.getCount() > 0)
            {
                if(inputSlotStack.getItem() != Items.BUCKET && inputSlotStack.getItem() instanceof BucketItem)
                {
                    this.items.set(inputIndex, new ItemStack(Items.BUCKET));
                }
                else
                {
                    this.decrStackSize(inputIndex, 1);
                }
            }
        }
    }

    private boolean canObliterate(@Nullable IRecipe<?> recipe)
    {
        // Is recipe valid - does it exist, is it of a valid type, and can it produce an output?
        if(recipe == null || !(recipe instanceof CoalstoneObliteratorRecipe) || !((CoalstoneObliteratorRecipe)recipe).matches(this, this.world))
        {
            return false;
        }
        ItemStack recipeOutputStack = recipe.getRecipeOutput();
        if(recipeOutputStack.isEmpty())
        {
            return false;
        }
        ItemStack outputSlotStack = this.items.get(FIRST_OUTPUT_SLOT_INDEX);
        if(!outputSlotStack.isEmpty() && recipeOutputStack.getItem() != outputSlotStack.getItem())
        {
            return false;
        }

        // Is there at least one input item?
        int filledInputs = 0;
        for(int inputIndex = FIRST_INPUT_SLOT_INDEX; inputIndex < FIRST_INPUT_SLOT_INDEX + INPUT_SLOT_COUNT; inputIndex++)
        {
            ItemStack inputStack = this.items.get(inputIndex);
            if(!inputStack.isEmpty())
            {
                filledInputs++;
            }
        }
        if(filledInputs < 1)
        {
            return false;
        }

        // 
        int validOutputs = OUTPUT_SLOT_COUNT;
        for(int outputIndex = FIRST_OUTPUT_SLOT_INDEX; outputIndex < FIRST_OUTPUT_SLOT_INDEX + OUTPUT_SLOT_COUNT; outputIndex++)
        {
            if(this.isSlotTaken(outputIndex, recipeOutputStack) < 1)
            {
                validOutputs--;
            }
        }
        if(validOutputs < 1)
        {
            return false;
        }

        return true;
    }

    private int isSlotTaken(int slotIndex, ItemStack itemStack)
    {
        ItemStack slot = this.items.get(slotIndex);
        if(slot.isEmpty())
        {
            return 2;
        }
        else
        {
            if(slot.isItemEqual(itemStack) && !(slot.getCount() + itemStack.getCount() > slot.getMaxStackSize()))
            {
                return 1;
            }
        }

        return 0;
    }

    @Override
    protected Container createMenu(int windowID, PlayerInventory playerInventory)
    {
        return CoalstoneObliteratorContainer.createContainerServerSide(windowID, playerInventory, this, this.obliteratorData);
    }

    @Override
    public int getSizeInventory()
    {
        return this.items.size();
    }

    @Override
    public boolean isEmpty()
    {
        return isSlotRangeEmpty(FIRST_FUEL_SLOT_INDEX, SLOT_COUNT);
    }

    public boolean isSlotRangeEmpty(int min, int max)
    {
        for(int slotIndex = min; slotIndex < max; slotIndex++)
        {
            ItemStack stackInSlot = this.items.get(slotIndex);
            if(stackInSlot.getCount() > 0)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemStack = this.getStackInSlot(index);
        boolean maintainProcessingTime = !itemStack.isEmpty() && itemStack.getItem() == stack.getItem() && ItemStack.areItemStackTagsEqual(stack, itemStack);

        this.items.set(index, stack);
        if(stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if(index >= FIRST_INPUT_SLOT_INDEX && index < FIRST_OUTPUT_SLOT_INDEX && !maintainProcessingTime)
        {
            this.setCookTime(this.getRecipeCookTime());
            this.markDirty();
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        if(this.world.getTileEntity(this.pos) != this) return false;

        final double X_CENTRE_OFFSET = 0.5;
        final double Y_CENTRE_OFFSET = 0.5;
        final double Z_CENTRE_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;

        return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    @Override
    public void clear()
    {
        this.items.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        switch(side)
        {
            case UP:
                return SLOTS_UP;
            case DOWN:
                return SLOTS_DOWN;
            default:
                return SLOTS_HORIZONTAL[getSideDirection(side)];
        }
    }

    private int getSideDirection(Direction side)
    {
        Direction facing = this.getBlockState().get(CoalstoneObliteratorBlock.FACING);
        int direction = side.getHorizontalIndex() - facing.getHorizontalIndex();
        if(direction < 0)
        {
            direction = 3 + direction;
        }
        return direction;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        // If pulling out of a fuel / grinder slot using the bottom face of the Obliterator...
        if(direction == Direction.DOWN && index < FIRST_INPUT_SLOT_INDEX)
        {
            // Return false unless the slot contains an unusable bucket
            Item item = stack.getItem();
            if(item != Items.WATER_BUCKET && item != Items.BUCKET)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return isItemInsertable(index, stack);
    }

    public static boolean isItemInsertable(int index, ItemStack stack)
    {
        if(index < FIRST_GRINDER_SLOT_INDEX)
        {
            return isItemValidForFuelSlot(stack);
        }
        else if(index < FIRST_INPUT_SLOT_INDEX)
        {
            return isItemValidForGrinderSlot(stack);
        }
        else if(index < FIRST_OUTPUT_SLOT_INDEX)
        {
            return isItemValidForInputSlot(stack);
        }

        return isItemValidForOutputSlot(stack);
    }

    public static boolean isItemValidForFuelSlot(ItemStack stack)
    {
        return isFuel(stack);
    }

    public static boolean isItemValidForGrinderSlot(ItemStack stack)
    {
        return isGrinder(stack);
    }

    public static boolean isItemValidForInputSlot(ItemStack stack)
    {
        return true;
    }

    public static boolean isItemValidForOutputSlot(ItemStack stack)
    {
        return false;
    }

    public static boolean isFuel(ItemStack stack)
    {
        return ForgeHooks.getBurnTime(stack) > 0;
    }

    public static boolean isGrinder(ItemStack stack)
    {
        //TODO: Make this function read from some kind of list somewhere, rather than being annoying and hardcoded

        Item item = stack.getItem();
        return item == Items.FLINT;
    }

    public int getTileEntityId()
    {
        return 514;
    }
}
