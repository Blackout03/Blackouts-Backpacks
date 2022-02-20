package com.blackout.blackoutsbackpacks.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BBBaseContainer extends Container {
    public IItemHandler chestInventory;
    public int chestWidth;
    public int chestHeight;

    public BBBaseContainer(ContainerType<?> type, int windowID, PlayerInventory playerInventory, int chestWidth, int chestHeight, IItemHandler inventory) {
        super(type, windowID);
        this.chestWidth = chestWidth;
        this.chestHeight = chestHeight;
        this.chestInventory = inventory;

        //get the centered position of the player inventory
        int playerInvX = (int)(8 + ((chestWidth > 9 ? ((chestWidth - 9) * 18) / 2f : 0)));

        //add hotbar
        for(int x = 0; x < 9; x++) {
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
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                int index = x + y * 9;
                addSlot(new Slot(playerInventory, 9 + index, playerInvX + (x * 18), 17 + (y * 18) + (chestHeight * 18) + 13));
            }
        }

        //add chest slots
        for(int y = 0; y < chestHeight; y++) {
            for(int x = 0; x < chestWidth; x++) {
                int index = x + y * chestWidth;
                addSlot(new SlotItemHandler(inventory, index, 8 + (x * 18), 17 + (y * 18)));
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
}