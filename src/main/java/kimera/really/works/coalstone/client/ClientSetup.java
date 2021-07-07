package kimera.really.works.coalstone.client;

import kimera.really.works.coalstone.Coalstone;
import kimera.really.works.coalstone.client.gui.screen.inventory.CoalstoneCondenserScreen;
import kimera.really.works.coalstone.client.gui.screen.inventory.CoalstoneFurnaceScreen;
import kimera.really.works.coalstone.client.gui.screen.inventory.CoalstoneObliteratorScreen;
import kimera.really.works.coalstone.common.inventory.container.ContainerRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Coalstone.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
	public static void init(final FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(ContainerRegistry.COALSTONE_FURNACE.get(), CoalstoneFurnaceScreen::new);
		ScreenManager.registerFactory(ContainerRegistry.COALSTONE_CONDENSER.get(), CoalstoneCondenserScreen::new);
		ScreenManager.registerFactory(ContainerRegistry.COALSTONE_OBLITERATOR.get(), CoalstoneObliteratorScreen::new);
	}
}
