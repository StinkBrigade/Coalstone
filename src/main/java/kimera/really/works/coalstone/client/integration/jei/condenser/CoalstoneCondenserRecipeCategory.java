package kimera.really.works.coalstone.client.integration.jei.condenser;

import java.awt.Color;
import java.text.NumberFormat;

import com.mojang.blaze3d.matrix.MatrixStack;

import kimera.really.works.coalstone.Coalstone;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CoalstoneCondenserRecipeCategory implements IRecipeCategory<CoalstoneCondenserRecipe>
{
    public static final ResourceLocation UID = new ResourceLocation(Coalstone.MODID, "coalstone_condenser");

    private final IDrawable background;
    private final String localizedName;
    private final IDrawable icon;

    private static final int[] CONDENSER_SLOTS_XPOS = { 103, 25, 7, 25, 43, 25 };
    private static final int[] CONDENSER_SLOTS_YPOS = { 25, 7, 25, 25, 25, 43 };
    
    private static final int COOK_TIME_XPOS = 7;
    private static final int COOK_TIME_YPOS = 67;

    public CoalstoneCondenserRecipeCategory(IGuiHelper guiHelper)
    {
        background = guiHelper.createDrawable(new ResourceLocation(Coalstone.MODID, "textures/gui/condenser_jei.png"), 0, 0, 132, 84);
        localizedName = I18n.format("jei.coalstone.coalstone_condenser");
        icon = guiHelper.createDrawableIngredient(new ItemStack(ItemRegistry.COALSTONE_CONDENSER.get()));
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
        /*RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        overlay.draw(matrixStack, 0, 0);
        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();*/
    	Minecraft mc = Minecraft.getInstance();
    	mc.fontRenderer.func_243248_b(matrixStack, getSmeltTimeText(recipe.getCookTime()), COOK_TIME_XPOS, COOK_TIME_YPOS, Color.gray.getRGB());
    }
    
    private ITextComponent getSmeltTimeText(int cookTime)
    {
    	NumberFormat numberInstance = NumberFormat.getNumberInstance();
    	numberInstance.setMaximumFractionDigits(2);
    	String smeltTimeSeconds = numberInstance.format(cookTime / 200f);
    	return new TranslationTextComponent("jei.coalstone.cookTime", smeltTimeSeconds);
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
    	int currentSlot = 0;
    	
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(currentSlot, false, CONDENSER_SLOTS_XPOS[currentSlot], CONDENSER_SLOTS_YPOS[currentSlot]);
        guiItemStacks.set(currentSlot, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));

        for(int inputSlotIndex = 0; inputSlotIndex < CoalstoneCondenserTileEntity.INPUT_SLOT_COUNT; inputSlotIndex++)
        {
            guiItemStacks.init(++currentSlot, true, CONDENSER_SLOTS_XPOS[currentSlot], CONDENSER_SLOTS_YPOS[currentSlot]);
            guiItemStacks.set(currentSlot, iIngredients.getInputs(VanillaTypes.ITEM).get(inputSlotIndex));
        }
    }
}
