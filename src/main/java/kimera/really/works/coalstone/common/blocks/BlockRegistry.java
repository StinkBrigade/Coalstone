package kimera.really.works.coalstone.common.blocks;

import java.util.function.ToIntFunction;

import kimera.really.works.coalstone.Coalstone;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Coalstone.MODID);
	
	public static final RegistryObject<Block> coalstoneBricks = BLOCKS.register("coalstone_bricks", () -> new Block(Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 6.0F)));
	public static final RegistryObject<Block> coalstoneBrickStairs = BLOCKS.register("coalstone_brick_stairs", () -> new StairsBlock(coalstoneBricks.get().getDefaultState(), Properties.from(coalstoneBricks.get())));
	public static final RegistryObject<Block> coalstoneBrickSlab = BLOCKS.register("coalstone_brick_slab", () -> new SlabBlock(Properties.from(coalstoneBricks.get())));
	public static final RegistryObject<Block> coalstoneTiles = BLOCKS.register("coalstone_tiles", () -> new Block(Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 6.0F)));
	public static final RegistryObject<Block> coalstoneTileStairs = BLOCKS.register("coalstone_tile_stairs", () -> new StairsBlock(coalstoneTiles.get().getDefaultState(), Properties.from(coalstoneBricks.get())));
	public static final RegistryObject<Block> coalstoneTileSlab = BLOCKS.register("coalstone_tile_slab", () -> new SlabBlock(Properties.from(coalstoneTiles.get())));
	
	public static final RegistryObject<Block> coalstoneFurnace = BLOCKS.register("coalstone_furnace", () -> new CoalstoneFurnaceBlock(Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 6.0F).setLightLevel(getLightValueLit(13))));
	public static final RegistryObject<Block> coalstoneCondenser = BLOCKS.register("coalstone_condenser", () -> new CoalstoneCondenserBlock(Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 6.0F).setLightLevel(getLightValueLit(13))));
	
	private static ToIntFunction<BlockState> getLightValueLit(int lightValue)
	{
		return (state) ->
	    {
	    	return state.get(BlockStateProperties.LIT) ? lightValue : 0;
	    };
	}
}
