package kimera.really.works.coalstone.client.integration.jei;

import kimera.really.works.coalstone.Coalstone;
import kimera.really.works.coalstone.client.integration.jei.categories.CoalstoneCondenserRecipeCategory;
import kimera.really.works.coalstone.common.blocks.BlockRegistry;
import kimera.really.works.coalstone.common.items.ItemRegistry;
import kimera.really.works.coalstone.common.recipes.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

@JeiPlugin
public class CoalstonePluginJEI implements IModPlugin
{
    private static final ResourceLocation UID = new ResourceLocation(Coalstone.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        registry.addRecipeCategories(
                new CoalstoneCondenserRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry)
    {
        World world = Minecraft.getInstance().world;
        registry.addRecipes(RecipeRegistry.getRecipes(RecipeRegistry.coalstoneCondenserRecipe, world.getRecipeManager()).values(), CoalstoneCondenserRecipeCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry)
    {
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.coalstoneCondenser.get()), CoalstoneCondenserRecipeCategory.UID);
    }
}
