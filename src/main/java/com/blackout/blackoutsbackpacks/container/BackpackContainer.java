package com.blackout.blackoutsbackpacks.container;

import com.blackout.blackoutsbackpacks.items.*;
import com.blackout.blackoutsbackpacks.registry.BBContainerTypes;
import com.blackout.blackoutsbackpacks.registry.BBItems;
import com.blackout.blackoutsbackpacks.util.BBUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Objects;

public class BackpackContainer extends BBBaseContainer {
    public static BackpackContainer createContainerFromItemstack(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        //store the hand and the stack, so we can write NBT later
        Hand hand = Hand.values()[extraData.readInt()];
        ItemStack backpack = playerInventory.player.getItemInHand(hand);

        //default width
        int inventoryWidth = 9;
        int inventoryHeight;

        if (playerInventory.getSelected().getItem() instanceof DyeableBackpackItem) {
            inventoryHeight = 1;
        } else if (playerInventory.getSelected().getItem() instanceof IronBackpackItem) {
            inventoryHeight = 2;
        } else if (playerInventory.getSelected().getItem() instanceof GoldBackpackItem) {
            inventoryHeight = 3;
        } else if (playerInventory.getSelected().getItem() instanceof DiamondBackpackItem) {
            inventoryHeight = 4;
        } else if (playerInventory.getSelected().getItem() instanceof EmeraldBackpackItem) {
            inventoryHeight = 5;
        } else if (playerInventory.getSelected().getItem() instanceof NetheriteBackpackItem) {
            inventoryHeight = 6;
        } else if (playerInventory.getSelected().getItem() instanceof AmethystBackpackItem) {
            inventoryHeight = 7;
        } else if (playerInventory.getSelected().getItem() instanceof RubyBackpackItem) {
            inventoryHeight = 8;
        } else {
            inventoryWidth = 0;
            inventoryHeight = 0;
        }

        //if no tag, make one now to stop crashes
        if(backpack.getTag() == null) {
            backpack.setTag(new CompoundNBT());
        }

        CompoundNBT tag = backpack.getTag();

        //if it doesn't have a width, add one now - otherwise retrieve it
        if(!tag.contains("width")) {
            tag.putInt("width", inventoryWidth);
            tag.putInt("height", inventoryHeight);
        } else {
            inventoryWidth = tag.getInt("width");
            inventoryHeight = tag.getInt("height");
        }

        //get an item handler from the size
        ItemStackHandler inventoryHandler = new ItemStackHandler(inventoryWidth * inventoryHeight);

        if(tag.contains("Inventory")) {
            //read in the data from the backpack
            inventoryHandler.deserializeNBT(tag.getCompound("Inventory"));
            //fix the size if need be
            inventoryHandler = BBUtils.validateHandlerSize(inventoryHandler, inventoryWidth, inventoryHeight);
        }

        return new BackpackContainer(windowID, playerInventory, inventoryWidth, inventoryHeight, inventoryHandler, hand);
    }

    public Hand hand;

    public BackpackContainer(int windowID, PlayerInventory playerInventory, int inventoryWidth, int inventoryHeight, ItemStackHandler inventoryHandler, Hand hand) {
        super(BBContainerTypes.BACKPACK_CONTAINER_TYPE.get(), windowID, playerInventory, inventoryWidth, inventoryHeight, inventoryHandler);
        this.hand = hand;
    }

    @Override
    public void removed(PlayerEntity playerIn) {
        //save the data to the backpack
        if(this.chestInventory instanceof ItemStackHandler) {
            ItemStackHandler handler = (ItemStackHandler) this.chestInventory;
            if(!playerIn.getItemInHand(hand).isEmpty()) {
                Objects.requireNonNull(playerIn.getItemInHand(hand).getTag()).put("Inventory", handler.serializeNBT());
            }
        }
    }
}