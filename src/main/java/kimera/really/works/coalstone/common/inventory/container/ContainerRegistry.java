package kimera.really.works.coalstone.common.inventory.container;

import kimera.really.works.coalstone.Coalstone;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry
{
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Coalstone.MODID);

	public static final RegistryObject<ContainerType<CoalstoneFurnaceContainer>> COALSTONE_FURNACE = CONTAINERS.register("coalstone_furnace", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		return new CoalstoneFurnaceContainer(windowId, inv);
	}));

	public static final RegistryObject<ContainerType<CoalstoneCondenserContainer>> COALSTONE_CONDENSER = CONTAINERS.register("coalstone_condenser", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		return CoalstoneCondenserContainer.createContainerClientSide(windowId, inv, data);
	}));

	public static final RegistryObject<ContainerType<CoalstoneObliteratorContainer>> COALSTONE_OBLITERATOR = CONTAINERS.register("coalstone_obliterator", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		return CoalstoneObliteratorContainer.createContainerClientSide(windowId, inv, data);
	}));
}
