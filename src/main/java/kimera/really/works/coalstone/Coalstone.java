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
        
        // TODO: JEI Support (27/12/2020: Tried to implement, Maven was being an asshole though. Need to try again soon.)
        // TODO: Condenser recipes reset when changing item stack sizes
        // TODO: Fix weird bug where /src/main/resources/kimera/... .CLASS files aren't updating to their /bin/kimera/... counterparts.
    }
}