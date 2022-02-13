package com.blackout.blackoutsbackpacks.client;

import com.blackout.blackoutsbackpacks.BlackoutsBackpacks;
import com.blackout.blackoutsbackpacks.registry.BBItems;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BlackoutsBackpacks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BBItemColors {
    @SubscribeEvent
    public static void registerColorHandlers(ColorHandlerEvent.Item event) {
        registerItemColorHandlers(event.getItemColors());
    }

    private static void registerItemColorHandlers(final ItemColors itemColors) {
        itemColors.register((stack, color) -> {
            return color > 0 ? -1 : ((IDyeableArmorItem)stack.getItem()).getColor(stack);
        }, BBItems.LEATHER_BACKPACK.get());
    }
}
