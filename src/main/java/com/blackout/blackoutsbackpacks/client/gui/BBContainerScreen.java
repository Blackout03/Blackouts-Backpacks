package com.blackout.blackoutsbackpacks.client.gui;

import com.blackout.blackoutsbackpacks.BlackoutsBackpacks;
import com.blackout.blackoutsbackpacks.container.BBContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BBContainerScreen extends ContainerScreen<BBContainer> {
	public static ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation(BlackoutsBackpacks.MODID, "textures/gui/chest.png");
	public static ResourceLocation CHEST_GUI_SLOTS_TEXTURE = new ResourceLocation(BlackoutsBackpacks.MODID, "textures/gui/chest_slots.png");

	public int chestWidth;
	public int chestHeight;

	public BBContainerScreen(BBContainer chestContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(chestContainer, inv, titleIn);

		this.chestWidth = chestContainer.chestWidth;
		this.chestHeight = chestContainer.chestHeight;

		//calculate all the sizes using the right padding
		imageWidth = 7 + (chestWidth * 18) + 7;
		imageHeight = 16 + (chestHeight * 18) + 13 + (3 * 18) + 4 + 18 + 7;

		//move the inventory tag to the center
		inventoryLabelY = 16 + (chestHeight * 18) + 3;
		inventoryLabelX = (int) (7 + ((chestWidth > 9 ? ((chestWidth - 9) * 18) / 2f : 0)));
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);

		assert minecraft != null;
		minecraft.textureManager.bind(CHEST_GUI_TEXTURE);

		int playerInvX = (int) (7 + ((chestWidth > 9 ? ((chestWidth - 9) * 18) / 2f : 0)));

		//draw the center of the GUI using the texture
		blit(matrixStack, leftPos + 7, topPos + 7, imageWidth - 14, imageHeight - 14, 8f, 8f, 1, 1, 15, 15);

		//top left, top right
		blit(matrixStack, leftPos, topPos, 7, 7, 0f, 0f, 7, 7, 15, 15);
		blit(matrixStack, leftPos + imageWidth - 7, topPos, 7, 7, 8f, 0f, 7, 7, 15, 15);

		//bottom left, bottom right
		blit(matrixStack, leftPos, topPos + imageHeight - 7, 7, 7, 0f, 8f, 7, 7, 15, 15);
		blit(matrixStack, leftPos + imageWidth - 7, topPos + imageHeight - 7, 7, 7, 8f, 8f, 7, 7, 15, 15);

		//connects
		blit(matrixStack, leftPos + 7, topPos, imageWidth - 14, 7, 7f, 0f, 1, 7, 15, 15);
		blit(matrixStack, leftPos + 7, topPos + imageHeight - 7, imageWidth - 14, 7, 7f, 8f, 1, 7, 15, 15);

		//horizontal connects
		blit(matrixStack, leftPos, topPos + 7, 7, imageHeight - 14, 0f, 8f, 7, 1, 15, 15);
		blit(matrixStack, leftPos + imageWidth - 7, topPos + 7, 7, imageHeight - 14, 8f, 8f, 7, 1, 15, 15);

		//draw the slots in
		drawSlots(matrixStack, 7, 16, chestWidth, chestHeight);
		drawSlots(matrixStack, playerInvX, 16 + (chestHeight * 18 + 13), 9, 3);
		drawSlots(matrixStack, playerInvX, 16 + (chestHeight * 18 + 13 + (3 * 18) + 4), 9, 1);

		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	public void drawSlots(MatrixStack matrixStack, int x, int y, int w, int h) {
		assert minecraft != null;
		minecraft.textureManager.bind(CHEST_GUI_SLOTS_TEXTURE);
		//blit the texture for slots using the amount given
		blit(matrixStack, leftPos + x, topPos + y, w * 18, h * 18, 0f, 0f, w * 18, h * 18, 162, 162);
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
	}
}