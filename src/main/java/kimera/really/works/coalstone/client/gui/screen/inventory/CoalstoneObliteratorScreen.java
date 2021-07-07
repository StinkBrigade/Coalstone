package kimera.really.works.coalstone.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import kimera.really.works.coalstone.common.inventory.container.CoalstoneCondenserContainer;
import kimera.really.works.coalstone.common.inventory.container.CoalstoneObliteratorContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CoalstoneObliteratorScreen  extends ContainerScreen<CoalstoneObliteratorContainer>
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("coalstone", "textures/gui/container/coalstone_obliterator.png");

    private static final int BURN_BAR_XPOS = 8;
    private static final int BURN_BAR_YPOS = 36;
    private static final int BURN_BAR_WIDTH = 14;
    private static final int BURN_BAR_HEIGHT = 14;
    private static final int BURN_BAR_TEXTURE_XPOS = 176;
    private static final int BURN_BAR_TEXTURE_YPOS = 0;

    private static final int GRIND_BAR_XPOS = 51;
    private static final int GRIND_BAR_YPOS = 40;
    private static final int GRIND_BAR_WIDTH = 24;
    private static final int GRIND_BAR_HEIGHT = 6;
    private static final int GRIND_BAR_TEXTURE_XPOS = 176;
    private static final int GRIND_BAR_TEXTURE_YPOS = 14;

    private static final int COOK_BAR_XPOS = 79;
    private static final int COOK_BAR_YPOS = 35;
    private static final int COOK_BAR_WIDTH = 24;
    private static final int COOK_BAR_HEIGHT = 15;
    private static final int COOK_BAR_TEXTURE_XPOS = 176;
    private static final int COOK_BAR_TEXTURE_YPOS = 20;

    public CoalstoneObliteratorScreen(CoalstoneObliteratorContainer screenContainer, PlayerInventory inv, ITextComponent title)
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

        double grindFraction = this.container.getGrindAmountFraction();
        int grindWidth = (int)(GRIND_BAR_WIDTH * grindFraction);
        this.blit(matrixStack, edgeSpacingX + GRIND_BAR_XPOS, edgeSpacingY + GRIND_BAR_YPOS, GRIND_BAR_TEXTURE_XPOS, GRIND_BAR_TEXTURE_YPOS, grindWidth, GRIND_BAR_HEIGHT);

        double cookFraction = this.container.getCookTimeFraction();
        int cookWidth = (int)(COOK_BAR_WIDTH * cookFraction);
        this.blit(matrixStack, edgeSpacingX + COOK_BAR_XPOS, edgeSpacingY + COOK_BAR_YPOS, COOK_BAR_TEXTURE_XPOS, COOK_BAR_TEXTURE_YPOS, cookWidth, COOK_BAR_HEIGHT);
    }
}
