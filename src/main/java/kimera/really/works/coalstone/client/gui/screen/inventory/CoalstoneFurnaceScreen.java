package kimera.really.works.coalstone.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;

import kimera.really.works.coalstone.common.inventory.container.CoalstoneFurnaceContainer;
import net.minecraft.client.gui.recipebook.FurnaceRecipeGui;
import net.minecraft.client.gui.screen.inventory.AbstractFurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CoalstoneFurnaceScreen extends AbstractFurnaceScreen<CoalstoneFurnaceContainer>
{
   private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("coalstone", "textures/gui/container/coalstone_furnace.png");

   public CoalstoneFurnaceScreen(CoalstoneFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
   {
      super(screenContainer, new FurnaceRecipeGui(), inv, titleIn, GUI_TEXTURE);
   }
   
   @Override
   protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y)
   {
	   this.font.drawString(matrixStack, this.title.getString(), (float)this.titleX, (float)this.titleY, 12105912);
	   this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 12105912);
   }
}