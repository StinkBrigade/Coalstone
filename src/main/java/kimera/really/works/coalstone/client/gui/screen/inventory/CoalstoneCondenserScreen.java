package kimera.really.works.coalstone.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import kimera.really.works.coalstone.common.inventory.container.CoalstoneCondenserContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CoalstoneCondenserScreen extends ContainerScreen<CoalstoneCondenserContainer>
{
	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("coalstone", "textures/gui/container/coalstone_condenser.png");

	private static final int BURN_BAR_XPOS = 8;
	private static final int BURN_BAR_YPOS = 36;
	private static final int BURN_BAR_WIDTH = 14;
	private static final int BURN_BAR_HEIGHT = 14;
	private static final int BURN_BAR_TEXTURE_XPOS = 176;
	private static final int BURN_BAR_TEXTURE_YPOS = 0;
	
	private static final int COOK_BAR_XPOS = 100;
	private static final int COOK_BAR_YPOS = 19;
	private static final int COOK_BAR_WIDTH = 17;
	private static final int COOK_BAR_HEIGHT = 53;
	private static final int COOK_BAR_TEXTURE_XPOS = 176;
	private static final int COOK_BAR_TEXTURE_YPOS = 14;
	
	public CoalstoneCondenserScreen(CoalstoneCondenserContainer screenContainer, PlayerInventory inv, ITextComponent title)
	{
		super(screenContainer, inv, title);
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY)
	{
		this.font.drawString(matrixStack, this.title.getString(), (float)this.titleX, (float)this.titleY, 12105912);
		this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 12105912);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
		
		int edgeSpacingX = (this.width - this.xSize) / 2;
		int edgeSpacingY = (this.height - this.ySize) / 2;
		this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
		
		double burnFraction = this.container.getBurnTimeFraction();
		int burnHeight = (int)(BURN_BAR_HEIGHT * burnFraction);
		int burnOffsetY = (int)((1.0 - burnFraction) * BURN_BAR_HEIGHT);
		this.blit(matrixStack, edgeSpacingX + BURN_BAR_XPOS, edgeSpacingY + BURN_BAR_YPOS + burnOffsetY, BURN_BAR_TEXTURE_XPOS, BURN_BAR_TEXTURE_YPOS + burnOffsetY, BURN_BAR_WIDTH, burnHeight);
		
		double cookFraction = this.container.getCookTimeFraction();
		int cookHeight = (int)(COOK_BAR_HEIGHT * cookFraction);
		this.blit(matrixStack, edgeSpacingX + COOK_BAR_XPOS, edgeSpacingY + COOK_BAR_YPOS, COOK_BAR_TEXTURE_XPOS, COOK_BAR_TEXTURE_YPOS, COOK_BAR_WIDTH, cookHeight);
	}
}
