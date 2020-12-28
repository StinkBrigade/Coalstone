package kimera.really.works.coalstone.common.blocks;

import java.util.Random;

import kimera.really.works.coalstone.common.tileentities.CoalstoneFurnaceTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CoalstoneFurnaceBlock extends AbstractFurnaceBlock
{
	public CoalstoneFurnaceBlock(AbstractBlock.Properties properties)
	{
		super(properties);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn)
	{
		return new CoalstoneFurnaceTileEntity();
	}

	@Override
	protected void interactWith(World worldIn, BlockPos pos, PlayerEntity player)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof CoalstoneFurnaceTileEntity)
		{
			player.openContainer((INamedContainerProvider)tileEntity);
	        player.addStat(Stats.INTERACT_WITH_FURNACE);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (stateIn.get(LIT))
		{
			double spawnX = (double)pos.getX() + 0.5D;
			double spawnY = (double)pos.getY();
			double spawnZ = (double)pos.getZ() + 0.5D;
			if (rand.nextDouble() < 0.1D)
			{
				worldIn.playSound(spawnX, spawnY, spawnZ, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
		
			Direction direction = stateIn.get(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			
			double offsetRNG = rand.nextDouble() * 0.6D - 0.3D;
			double offsetX = direction$axis == Direction.Axis.X ? (double)direction.getXOffset() * 0.52D : offsetRNG;
			double offsetY = rand.nextDouble() * 6.0D / 16.0D;
			double offsetZ = direction$axis == Direction.Axis.Z ? (double)direction.getZOffset() * 0.52D : offsetRNG;
			
			worldIn.addParticle(ParticleTypes.SMOKE, spawnX + offsetX, spawnY + offsetY, spawnZ + offsetZ, 0.0D, 0.0D, 0.0D);
			worldIn.addParticle(ParticleTypes.FLAME, spawnX + offsetX, spawnY + offsetY, spawnZ + offsetZ, 0.0D, 0.0D, 0.0D);
		}
	}
}
