package com.blackout.blackoutsbackpacks.items;

import com.blackout.blackoutsbackpacks.container.BackpackContainer;
import com.blackout.blackoutsbackpacks.util.BBUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class BackpackItemContainerProvider implements INamedContainerProvider {
    private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.blackoutsbackpacks.backpack");
    private final int inventoryWidth;
    private final int inventoryHeight;
    private final ItemStackHandler inventory;
    private final Hand hand;

    // constructor takes the hand and backpack stack
    public BackpackItemContainerProvider(Hand hand, ItemStack backpack, int width, int height) {
        int inventoryWidth = width;
        int inventoryHeight = height;

        //if no tag make one now
        if(backpack.getTag() == null) {
            backpack.setTag(new CompoundNBT());
        }

        CompoundNBT tag = backpack.getTag();

        //if no width add it now
        if(!tag.contains("width")) {
            tag.putInt("width", inventoryWidth);
            tag.putInt("height", inventoryHeight);
        } else {
            inventoryWidth = tag.getInt("width");
            inventoryHeight = tag.getInt("height");
        }

        if (tag.getInt("height") != height) {
            tag.putInt("height", height);
        }

        //create our handler using the size
        ItemStackHandler inventoryHandler = new ItemStackHandler(inventoryWidth * inventoryHeight);

        if(tag.contains("Inventory")) {
            //read from nbt!
            inventoryHandler.deserializeNBT(tag.getCompound("Inventory"));
            //fix the size if need be
            inventoryHandler = BBUtils.validateHandlerSize(inventoryHandler, inventoryWidth, inventoryHeight);
        }

        // save all this data so our container can use it
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
        this.inventory = inventoryHandler;
        this.hand = hand;
    }

    @Override
    public ITextComponent getDisplayName() {
        return CONTAINER_TITLE;
    }

    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BackpackContainer(windowID, playerInventory, inventoryWidth, inventoryHeight, inventory, hand);
    }
}