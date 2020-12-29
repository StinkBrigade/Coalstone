package kimera.really.works.coalstone.client.integration.jei.categories;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import kimera.really.works.coalstone.Coalstone;
import kimera.really.works.coalstone.client.gui.screen.inventory.CoalstoneCondenserScreen;
import kimera.really.works.coalstone.common.inventory.container.CoalstoneCondenserContainer;
import kimera.really.works.coalstone.common.items.ItemRegistry;
import kimera.really.works.coalstone.common.recipes.CoalstoneCondenserRecipe;
import kimera.really.works.coalstone.common.tileentities.CoalstoneCondenserTileEntity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Size2i;

public class CoalstoneCondenserRecipeCategory implements IRecipeCategory<CoalstoneCondenserRecipe>
{
    public static final ResourceLocation UID = new ResourceLocation(Coalstone.MODID, "coalstone_condenser");

    private final IDrawable background;
    private final String localizedName;
    private final IDrawable overlay;
    private final IDrawable icon;

    private static final int[][] CONDENSER_SLOTS_XPOS =
    {
            { 104 },
            { 26, 8, 26, 44, 26 }
    };
    private static final int[][] CONDENSER_SLOTS_YPOS =
    {
            { 35 },
            { 17, 35, 35, 35, 53 }
    };

    public CoalstoneCondenserRecipeCategory(IGuiHelper guiHelper)
    {
        background = guiHelper.createBlankDrawable(132, 84);
        localizedName = I18n.format("jei.coalstone.coalstone_condenser");
        overlay = guiHelper.createDrawable(new ResourceLocation(Coalstone.MODID, "textures/gui/condenser.png"), 0, 0, 132, 84);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ItemRegistry.coalstoneCondenser.get()));
    }

    @Override
    public ResourceLocation getUid()
    {
        return UID;
    }

    @Override
    public Class<? extends CoalstoneCondenserRecipe> getRecipeClass()
    {
        return CoalstoneCondenserRecipe.class;
    }

    @Override
    public String getTitle()
    {
        return localizedName;
    }

    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }


    @Override
    public void draw(CoalstoneCondenserRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY)
    {
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        overlay.draw(matrixStack, 0, 0);
        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();
    }

    @Override
    public void setIngredients(CoalstoneCondenserRecipe recipe, IIngredients iIngredients)
    {
        iIngredients.setInputIngredients(recipe.getIngredients());
        iIngredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CoalstoneCondenserRecipe recipe, IIngredients iIngredients)
    {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(CoalstoneCondenserTileEntity.FIRST_OUTPUT_SLOT_INDEX, false, CONDENSER_SLOTS_XPOS[0][0], CONDENSER_SLOTS_YPOS[0][0]);

        for(int inputSlotIndex = 0; inputSlotIndex < CoalstoneCondenserTileEntity.INPUT_SLOT_COUNT; inputSlotIndex++)
        {
            guiItemStacks.init(inputSlotIndex, true, CONDENSER_SLOTS_XPOS[1][inputSlotIndex], CONDENSER_SLOTS_YPOS[1][inputSlotIndex]);
        }
    }
}
