package com.blackout.blackoutsbackpacks.items;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class BackpackItem extends Item implements IDyeableArmorItem {
    private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.blackoutsbackpacks.backpack");

    public BackpackItem(Properties properties) {
        super(properties);
    }

    public static Inventory getInventory(ItemStack stack) {
        return new Inventory(stack) {
            @Override
            public boolean canAddItem(@Nonnull ItemStack stack) {
                return canAddItem(stack);
            }
        };
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
        return new InvProvider(stack);
    }

    private static class InvProvider implements ICapabilityProvider {
        private final LazyOptional<IItemHandler> opt;

        private InvProvider(ItemStack stack) {
            opt = LazyOptional.of(() -> new InvWrapper(getInventory(stack)));
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, opt);
        }
    }


    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
        if (!world.isClientSide) {
            INamedContainerProvider container = new SimpleNamedContainerProvider((w, p, pl) -> ChestContainer.oneRow(player.containerMenu.containerId, player.inventory), CONTAINER_TITLE);
            NetworkHooks.openGui((ServerPlayerEntity) player, container, buf -> {
                buf.writeBoolean(hand == Hand.MAIN_HAND);
            });
        }
        return ActionResult.success(player.getItemInHand(hand));
    }

    @Override
    public ActionResultType useOn(ItemUseContext ctx) {
        World world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Direction side = ctx.getClickedFace();

        TileEntity tile = world.getBlockEntity(pos);
        if (tile != null) {
            if (!world.isClientSide) {
                IItemHandler tileInv;
                if (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).isPresent()) {
                    tileInv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).orElseThrow(NullPointerException::new);
                } else if (tile instanceof IInventory) {
                    tileInv = new InvWrapper((IInventory) tile);
                } else {
                    return ActionResultType.FAIL;
                }

                IInventory bagInv = getInventory(ctx.getItemInHand());
                for (int i = 0; i < bagInv.getContainerSize(); i++) {
                    ItemStack flower = bagInv.getItem(i);
                    ItemStack rem = ItemHandlerHelper.insertItemStacked(tileInv, flower, false);
                    bagInv.setItem(i, rem);
                }

            }

            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
