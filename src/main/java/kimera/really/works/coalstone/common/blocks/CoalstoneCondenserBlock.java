package kimera.really.works.coalstone.common.blocks;

import java.util.Random;

import kimera.really.works.coalstone.common.tileentities.CoalstoneCondenserTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CoalstoneCondenserBlock extends Block
{
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	   
	public CoalstoneCondenserBlock(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(LIT, Boolean.valueOf(false)));
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new CoalstoneCondenserTileEntity();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if (worldIn.isRemote)
		{
			return ActionResultType.SUCCESS;
		}
		else
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof CoalstoneCondenserTileEntity)
			{
				player.openContainer((INamedContainerProvider)tileEntity);
				//TODO: Add stat for interacting with Condenser
			}
			
			return ActionResultType.CONSUME;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(stack.hasDisplayName())
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof CoalstoneCondenserTileEntity)
			{
				((CoalstoneCondenserTileEntity)tileEntity).setCustomName(stack.getDisplayName());
			}
		}
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(!state.isIn(newState.getBlock()))
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof CoalstoneCondenserTileEntity)
			{
				InventoryHelper.dropInventoryItems(worldIn, pos, (CoalstoneCondenserTileEntity)tileEntity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, LIT);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (stateIn.get(LIT))
		{
			double spawnX = (double)pos.getX() + 0.5D;
			double spawnY = (double)pos.getY() + 0.5D;
			double spawnZ = (double)pos.getZ() + 0.5D;
			if (rand.nextDouble() < 0.1D)
			{
				worldIn.playSound(spawnX, spawnY, spawnZ, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
		
			Direction direction = stateIn.get(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			
			double offsetRNG = rand.nextDouble() * 0.6D - 0.3D;
			double offsetX = direction$axis == Direction.Axis.X ? (double)direction.getXOffset() * 0.52D : offsetRNG;
			double offsetY = rand.nextDouble() * 0.6D - 0.3D;
			double offsetZ = direction$axis == Direction.Axis.Z ? (double)direction.getZOffset() * 0.52D : offsetRNG;
			
			worldIn.addParticle(ParticleTypes.SMOKE, spawnX + offsetX, spawnY + offsetY, spawnZ + offsetZ, 0.0D, 0.0D, 0.0D);
			worldIn.addParticle(ParticleTypes.FLAME, spawnX + offsetX, spawnY + offsetY, spawnZ + offsetZ, 0.0D, 0.0D, 0.0D);
		}
	}
}
