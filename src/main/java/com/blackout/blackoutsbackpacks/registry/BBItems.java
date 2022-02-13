package com.blackout.blackoutsbackpacks.registry;

import com.blackout.blackoutsbackpacks.BlackoutsBackpacks;
import com.blackout.blackoutsbackpacks.items.BackpackItem;
import com.blackout.blackoutsbackpacks.items.EnderChestBackpackItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BBItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BlackoutsBackpacks.MODID);

    public static final RegistryObject<BackpackItem> LEATHER_BACKPACK = ITEMS.register("leather_backpack", () -> new BackpackItem(new Item.Properties().tab(BBItemGroups.BACKPACKS)));
    public static final RegistryObject<EnderChestBackpackItem> ENDER_BACKPACK = ITEMS.register("ender_backpack", () -> new EnderChestBackpackItem(new Item.Properties().tab(BBItemGroups.BACKPACKS)));
}