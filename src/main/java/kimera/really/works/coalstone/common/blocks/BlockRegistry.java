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
	
	public static final RegistryObject<Block> COALSTONE_BRICKS = BLOCKS.register("coalstone_bricks", () -> new Block(Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 6.0F)));
	public static final RegistryObject<Block> COALSTONE_BRICK_STAIRS = BLOCKS.register("coalstone_brick_stairs", () -> new StairsBlock(COALSTONE_BRICKS.get().getDefaultState(), Properties.from(COALSTONE_BRICKS.get())));
	public static final RegistryObject<Block> COALSTONE_BRICK_SLAB = BLOCKS.register("coalstone_brick_slab", () -> new SlabBlock(Properties.from(COALSTONE_BRICKS.get())));
	public static final RegistryObject<Block> COALSTONE_TILES = BLOCKS.register("coalstone_tiles", () -> new Block(Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 6.0F)));
	public static final RegistryObject<Block> COALSTONE_TILE_STAIRS = BLOCKS.register("coalstone_tile_stairs", () -> new StairsBlock(COALSTONE_TILES.get().getDefaultState(), Properties.from(COALSTONE_BRICKS.get())));
	public static final RegistryObject<Block> COALSTONE_TILE_SLAB = BLOCKS.register("coalstone_tile_slab", () -> new SlabBlock(Properties.from(COALSTONE_TILES.get())));
	
	public static final RegistryObject<Block> COALSTONE_FURNACE = BLOCKS.register("coalstone_furnace", () -> new CoalstoneFurnaceBlock(Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 6.0F).setLightLevel(getLightValueLit(13))));
	public static final RegistryObject<Block> COALSTONE_CONDENSER = BLOCKS.register("coalstone_condenser", () -> new CoalstoneCondenserBlock(Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 6.0F).setLightLevel(getLightValueLit(13))));

	private static ToIntFunction<BlockState> getLightValueLit(int lightValue)
	{
		return (state) -> state.get(BlockStateProperties.LIT) ? lightValue : 0;
	}
}
