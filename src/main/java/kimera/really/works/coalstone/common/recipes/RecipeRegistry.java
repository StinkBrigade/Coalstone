package kimera.really.works.coalstone.common.recipes;

import java.util.Map;

import org.apache.logging.log4j.Level;

import kimera.really.works.coalstone.Coalstone;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class RecipeRegistry
{
	public static final IRecipeType<CoalstoneCondenserRecipe> COALSTONE_CONDENSER_RECIPE_TYPE = new CoalstoneCondenserRecipeType();
	public static final IRecipeType<CoalstoneObliteratorRecipe> COALSTONE_OBLITERATOR_RECIPE_TYPE = new CoalstoneObliteratorRecipeType();
	
	public static void registerRecipeSerializers(Register<IRecipeSerializer<?>> event)
	{
		Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(COALSTONE_CONDENSER_RECIPE_TYPE.toString()), COALSTONE_CONDENSER_RECIPE_TYPE);
		Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(COALSTONE_OBLITERATOR_RECIPE_TYPE.toString()), COALSTONE_OBLITERATOR_RECIPE_TYPE);

		event.getRegistry().register(CoalstoneCondenserRecipe.SERIALIZER);
		event.getRegistry().register(CoalstoneObliteratorRecipe.SERIALIZER);
	}
	
	public static final Map<ResourceLocation, IRecipe<?>> getRecipes(IRecipeType<?> recipeType, RecipeManager recipeManager)
	{
		final Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipesMap = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, recipeManager, "field_199522_d");

		return recipesMap.get(recipeType);
	}
}