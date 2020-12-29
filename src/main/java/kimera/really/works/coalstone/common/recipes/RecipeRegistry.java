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
	public static final IRecipeType<CoalstoneCondenserRecipe> coalstoneCondenserRecipe = new CoalstoneCondenserRecipeType();
	
	public static void registerRecipeSerializers(Register<IRecipeSerializer<?>> event)
	{
		Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(coalstoneCondenserRecipe.toString()), coalstoneCondenserRecipe);
	
		event.getRegistry().register(CoalstoneCondenserRecipe.SERIALIZER);
	}
	
	public static final Map<ResourceLocation, IRecipe<?>> getRecipes(IRecipeType<?> recipeType, RecipeManager recipeManager)
	{
		final Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipesMap = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, recipeManager, "recipes");

		return recipesMap.get(recipeType);
	}
}