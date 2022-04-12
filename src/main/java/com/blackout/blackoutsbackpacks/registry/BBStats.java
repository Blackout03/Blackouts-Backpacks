package com.blackout.blackoutsbackpacks.registry;

import com.blackout.blackoutsbackpacks.BlackoutsBackpacks;
import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = BlackoutsBackpacks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BBStats {
	public static final DeferredRegister<StatType<?>> STAT_TYPES = DeferredRegister.create(ForgeRegistries.STAT_TYPES, BlackoutsBackpacks.MODID);

	public static final ResourceLocation OPEN_BACKPACK = registerCustom("open_backpack");
	public static final ResourceLocation OPEN_ENDER_BACKPACK = registerCustom("open_ender_backpack");

	private static ResourceLocation registerCustom(String key) {
		ResourceLocation resourcelocation = new ResourceLocation(BlackoutsBackpacks.MODID, key);
		Registry.register(Registry.CUSTOM_STAT, key, resourcelocation);
		Stats.CUSTOM.get(resourcelocation, IStatFormatter.DEFAULT);
		return resourcelocation;
	}
}
