package com.blackout.blackoutsbackpacks.items;

import com.blackout.blackoutsbackpacks.container.BackpackContainer;
import com.blackout.blackoutsbackpacks.registry.BBStats;
import com.blackout.blackoutsbackpacks.util.BBUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class EmeraldBackpackItem extends Item {
    private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.blackoutsbackpacks.backpack");

    public EmeraldBackpackItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        //make sure we're server side
        if(!worldIn.isClientSide) {
            CompoundNBT tag = new CompoundNBT();
            //if it doesn't have a tag - make one to stop crashes
            if(!playerIn.getItemInHand(handIn).hasTag()) {
                tag.putInt("width", 9);
                tag.putInt("height", 5);

                playerIn.getMainHandItem().setTag(tag);
            }

            if (tag.getInt("height") != 5) {
                tag.putInt("height", 5);
            }

            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerIn;
            NetworkHooks.openGui(serverPlayerEntity, new BackpackItemContainerProvider(handIn, playerIn.getMainHandItem()), (buf) -> buf.writeInt(handIn == Hand.MAIN_HAND ? 0 : 1));
            playerIn.awardStat(BBStats.OPEN_BACKPACK);
        }

        return ActionResult.pass(playerIn.getMainHandItem());
    }

    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        int width = 9;
        int height = 5;

        CompoundNBT tag = new CompoundNBT();
        if(stack.hasTag()) {
            assert stack.getTag() != null;
            if (stack.getTag().contains("width")) {
                width = stack.getTag().getInt("width");
                height = stack.getTag().getInt("height");
            }

            if (tag.getInt("height") != 5) {
                tag.putInt("height", 5);
            }
        }

        StringTextComponent widthComponent = new StringTextComponent("Width: ");
        widthComponent.append(new StringTextComponent(width + "").withStyle(TextFormatting.LIGHT_PURPLE));

        StringTextComponent heightComponent = new StringTextComponent("Height: ");
        heightComponent.append(new StringTextComponent(height + "").withStyle(TextFormatting.LIGHT_PURPLE));

        tooltip.add(widthComponent);
        tooltip.add(heightComponent);

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    /*
    *   Class to handle the opening of Backpack Container
    */
    public class BackpackItemContainerProvider implements INamedContainerProvider {
        private final int inventoryWidth;
        private final int inventoryHeight;
        private final ItemStackHandler inventory;
        private final Hand hand;

        // constructor takes the hand and backpack stack
        public BackpackItemContainerProvider(Hand hand, ItemStack backpack) {
            int inventoryWidth = 9;
            int inventoryHeight = 5;

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

            if (tag.getInt("height") != 5) {
                tag.putInt("height", 5);
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
}
