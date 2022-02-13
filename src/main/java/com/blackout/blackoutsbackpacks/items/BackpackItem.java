package com.blackout.blackoutsbackpacks.items;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class BackpackItem extends Item implements IDyeableArmorItem {
    private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.blackoutsbackpacks.backpack");

    public BackpackItem(Properties properties) {
        super(properties);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT compound) {
        return new InventoryCapability();
    }

    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT nbt = super.getShareTag(stack);
        if (nbt != null)
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
                    .ifPresent(capability -> nbt.put("Inventory", ((ItemStackHandler) capability).serializeNBT()));
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null)
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
                    .ifPresent(capability -> ((ItemStackHandler) capability).deserializeNBT((CompoundNBT) nbt.get("Inventory")));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand hand) {
        ActionResult<ItemStack> actionResult = super.use(world, entity, hand);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (entity instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) entity, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return CONTAINER_TITLE;
                }

                @Override
                public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
                    PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
                    packetBuffer.writeBlockPos(new BlockPos(x, y, z));
                    packetBuffer.writeByte(hand == Hand.MAIN_HAND ? 0 : 1);
                    return ChestContainer.oneRow(player.containerMenu.containerId, inventory);
                }
            }, buf -> {
                buf.writeBlockPos(new BlockPos(x, y, z));
                buf.writeByte(hand == Hand.MAIN_HAND ? 0 : 1);
            });
        }
        return actionResult;
    }


    private static class InventoryCapability implements ICapabilitySerializable<CompoundNBT> {
        private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(this::createItemHandler);

        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? this.inventory.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            return getItemHandler().serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            getItemHandler().deserializeNBT(nbt);
        }

        private ItemStackHandler createItemHandler() {
            return new ItemStackHandler(9) {
                @Override
                public int getSlotLimit(int slot) {
                    return 64;
                }
            };
        }

        private ItemStackHandler getItemHandler() {
            return inventory.orElseThrow(RuntimeException::new);
        }
    }

}
