package kimera.really.works.coalstone.common.tileentities;

import kimera.really.works.coalstone.Coalstone;
import kimera.really.works.coalstone.common.blocks.BlockRegistry;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegistry
{
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Coalstone.MODID);
	
	public static final RegistryObject<TileEntityType<CoalstoneFurnaceTileEntity>> COALSTONE_FURNACE = TILE_ENTITIES.register("coalstone_furnace", () -> TileEntityType.Builder.create(CoalstoneFurnaceTileEntity::new, BlockRegistry.coalstoneFurnace.get()).build(null));
	public static final RegistryObject<TileEntityType<CoalstoneCondenserTileEntity>> COALSTONE_CONDENSER = TILE_ENTITIES.register("coalstone_condenser", () -> TileEntityType.Builder.create(CoalstoneCondenserTileEntity::new, BlockRegistry.coalstoneCondenser.get()).build(null));
}
