package kimera.really.works.coalstone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kimera.really.works.coalstone.client.ClientSetup;
import kimera.really.works.coalstone.common.blocks.BlockRegistry;
import kimera.really.works.coalstone.common.inventory.container.ContainerRegistry;
import kimera.really.works.coalstone.common.items.ItemRegistry;
import kimera.really.works.coalstone.common.recipes.RecipeRegistry;
import kimera.really.works.coalstone.common.tileentities.TileEntityRegistry;
import kimera.really.works.coalstone.events.CoalstoneEventHandler;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Coalstone.MODID)
public class Coalstone
{
	public static final String MODID = "coalstone";
	
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static IEventBus EVENT_BUS;

    public Coalstone()
    {
    	EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
    	
    	ItemRegistry.ITEMS.register(EVENT_BUS);
    	BlockRegistry.BLOCKS.register(EVENT_BUS);
    	TileEntityRegistry.TILE_ENTITIES.register(EVENT_BUS);
    	ContainerRegistry.CONTAINERS.register(EVENT_BUS);
    	
    	FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, RecipeRegistry::registerRecipeSerializers);
    	
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);

        MinecraftForge.EVENT_BUS.register(new CoalstoneEventHandler());
    }
}