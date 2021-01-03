package kimera.really.works.coalstone.client.integration.jei;

import java.util.Objects;

import javax.annotation.Nonnull;

import kimera.really.works.coalstone.Coalstone;
import kimera.really.works.coalstone.client.integration.jei.condenser.CoalstoneCondenserRecipeCategory;
import kimera.really.works.coalstone.common.blocks.BlockRegistry;
import kimera.really.works.coalstone.common.recipes.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@JeiPlugin
public class CoalstonePluginJEI implements IModPlugin
{
    private static final ResourceLocation UID = new ResourceLocation(Coalstone.MODID, "jei_plugin");

    public static IJeiHelpers jeiHelper;
    
    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
    	jeiHelper = registry.getJeiHelpers();
        registry.addRecipeCategories(
                new CoalstoneCondenserRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry)
    {
        World world = Objects.requireNonNull(Minecraft.getInstance().world);
        registry.addRecipes(RecipeRegistry.getRecipes(RecipeRegistry.COALSTONE_CONDENSER_RECIPE_TYPE, world.getRecipeManager()).values(), CoalstoneCondenserRecipeCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry)
    {
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.COALSTONE_CONDENSER.get()), CoalstoneCondenserRecipeCategory.UID);
    }
}
