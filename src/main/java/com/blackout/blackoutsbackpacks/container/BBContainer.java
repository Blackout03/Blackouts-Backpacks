package com.blackout.blackoutsbackpacks.container;

import com.blackout.blackoutsbackpacks.items.*;
import com.blackout.blackoutsbackpacks.registry.BBContainerTypes;
import com.blackout.blackoutsbackpacks.registry.BBTags;
import com.blackout.blackoutsbackpacks.util.BBUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class BBContainer extends Container {
	public IItemHandler chestInventory;
	public int chestWidth;
	public int chestHeight;

	public BBContainer(ContainerType<?> type, int windowID, PlayerInventory playerInventory, int chestWidth, int chestHeight, IItemHandler inventory) {
		super(type, windowID);
		this.chestWidth = chestWidth;
		this.chestHeight = chestHeight;
		this.chestInventory = inventory;

		//get the centered position of the player inventory
		int playerInvX = (int) (8 + ((chestWidth > 9 ? ((chestWidth - 9) * 18) / 2f : 0)));

		//add hotbar
		for (int x = 0; x < 9; x++) {
			//if the inventory is a backpack and the current slot contains said backpack - make it unusable to prevent backpacks going missing
			if (x == playerInventory.selected) {
				addSlot(new Slot(playerInventory, x, playerInvX + (x * 18), 17 + (3 * 18) + (chestHeight * 18) + 17) {
					@Override
					public boolean mayPickup(PlayerEntity playerIn) {
						return false;
					}
				});
			} else {
				//otherwise, just proceed as usual
				addSlot(new Slot(playerInventory, x, playerInvX + (x * 18), 17 + (3 * 18) + (chestHeight * 18) + 17));
			}
		}

		//add main inv slots
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				int index = x + y * 9;
				addSlot(new Slot(playerInventory, 9 + index, playerInvX + (x * 18), 17 + (y * 18) + (chestHeight * 18) + 13));
			}
		}

		//add chest slots
		for (int y = 0; y < chestHeight; y++) {
			for (int x = 0; x < chestWidth; x++) {
				int index = x + y * chestWidth;
				addSlot(new SlotItemHandler(inventory, index, 8 + (x * 18), 17 + (y * 18)) {
					@Override
					public boolean mayPlace(ItemStack itemStack) {
						return !(itemStack.getItem().is(BBTags.Items.BACKPACKS));
					}
				});
			}
		}
	}

	/*
		Copied from ChestBlock
	 */
	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;

		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (itemstack1.getItem().is(BBTags.Items.BACKPACKS)) return ItemStack.EMPTY;

			if (index >= 36) {
				if (!this.moveItemStackTo(itemstack1, 0, 36, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 36, this.slots.size(), false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return itemstack;
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return true;
	}

	public static class BackpackContainer extends BBContainer {
		public Hand hand;

		public BackpackContainer(int windowID, PlayerInventory playerInventory, int inventoryWidth, int inventoryHeight, ItemStackHandler inventoryHandler, Hand hand) {
			super(BBContainerTypes.BACKPACK_CONTAINER_TYPE.get(), windowID, playerInventory, inventoryWidth, inventoryHeight, inventoryHandler);
			this.hand = hand;
		}

		public static BackpackContainer createContainerFromItemstack(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
			//store the hand and the stack, so we can write NBT later
			Hand hand = Hand.values()[extraData.readInt()];
			ItemStack backpack = playerInventory.player.getItemInHand(hand);

			//default width
			int inventoryWidth = 9;
			int inventoryHeight = 0;

			if (playerInventory.getSelected().getItem() instanceof DyeableBackpackItem) inventoryHeight = 1;
			else if (playerInventory.getSelected().getItem() instanceof IronBackpackItem) inventoryHeight = 2;
			else if (playerInventory.getSelected().getItem() instanceof GoldBackpackItem) inventoryHeight = 3;
			else if (playerInventory.getSelected().getItem() instanceof DiamondBackpackItem) inventoryHeight = 4;
			else if (playerInventory.getSelected().getItem() instanceof EmeraldBackpackItem) inventoryHeight = 5;
			else if (playerInventory.getSelected().getItem() instanceof NetheriteBackpackItem) inventoryHeight = 6;
			else if (playerInventory.getSelected().getItem() instanceof AmethystBackpackItem) inventoryHeight = 7;
			else if (playerInventory.getSelected().getItem() instanceof RubyBackpackItem) inventoryHeight = 8;
			else inventoryWidth = 0;

			//if no tag, make one now to stop crashes
			if (backpack.getTag() == null) backpack.setTag(new CompoundNBT());

			CompoundNBT tag = backpack.getTag();

			//if it doesn't have a width, add one now - otherwise retrieve it
			if (!tag.contains("width")) {
				tag.putInt("width", inventoryWidth);
				tag.putInt("height", inventoryHeight);
			} else {
				inventoryWidth = tag.getInt("width");
				inventoryHeight = tag.getInt("height");
			}

			//get an item handler from the size
			ItemStackHandler inventoryHandler = new ItemStackHandler(inventoryWidth * inventoryHeight);

			if (tag.contains("Inventory")) {
				//read in the data from the backpack
				inventoryHandler.deserializeNBT(tag.getCompound("Inventory"));
				//fix the size if need be
				inventoryHandler = BBUtils.validateHandlerSize(inventoryHandler, inventoryWidth, inventoryHeight);
			}

			return new BackpackContainer(windowID, playerInventory, inventoryWidth, inventoryHeight, inventoryHandler, hand);
		}

		@Override
		public void removed(PlayerEntity playerIn) {
			//save the data to the backpack
			if (this.chestInventory instanceof ItemStackHandler) {
				ItemStackHandler handler = (ItemStackHandler) this.chestInventory;
				if (!playerIn.getItemInHand(hand).isEmpty()) Objects.requireNonNull(playerIn.getItemInHand(hand).getTag()).put("Inventory", handler.serializeNBT());
			}
		}
	}
}