package com.blackout.blackoutsbackpacks.items;

import com.blackout.blackoutsbackpacks.registry.BBStats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EnderChestBackpackItem extends Item {
	private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.blackoutsbackpacks.ender_backpack");

	public EnderChestBackpackItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		final ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide && player instanceof ServerPlayerEntity) {
			open((ServerPlayerEntity) player, hand == Hand.MAIN_HAND ? player.getMainHandItem().getCount() : -1);
		}
		return ActionResult.success(stack);
	}

	public void open(ServerPlayerEntity player, int selectedSlot) {
		EnderChestInventory enderchestinventory = player.getEnderChestInventory();
		NetworkHooks.openGui(player, new SimpleNamedContainerProvider((p_226928_1_, p_226928_2_, p_226928_3_) -> ChestContainer.threeRows(p_226928_1_, p_226928_2_, enderchestinventory), CONTAINER_TITLE), buffer -> buffer.writeVarInt(selectedSlot));
		player.awardStat(BBStats.OPEN_ENDER_BACKPACK);
	}
}
