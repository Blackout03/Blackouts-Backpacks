package com.blackout.blackoutsbackpacks.registry;

import com.blackout.blackoutsbackpacks.BlackoutsBackpacks;
import com.blackout.blackoutsbackpacks.items.BackpackItem;
import com.blackout.blackoutsbackpacks.items.DyeableBackpackItem;
import com.blackout.blackoutsbackpacks.items.EnderChestBackpackItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BBItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BlackoutsBackpacks.MODID);

    private static boolean isModLoaded(String modid) {
        return (ModList.get().isLoaded(modid));
    }

    public static final RegistryObject<DyeableBackpackItem> LEATHER_BACKPACK = ITEMS.register("leather_backpack", () -> new DyeableBackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS), 9, 1));
    public static final RegistryObject<BackpackItem> IRON_BACKPACK = ITEMS.register("iron_backpack", () -> new BackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS), 9, 2));
    public static final RegistryObject<BackpackItem> GOLD_BACKPACK = ITEMS.register("gold_backpack", () -> new BackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS), 9, 3));
    public static final RegistryObject<BackpackItem> DIAMOND_BACKPACK = ITEMS.register("diamond_backpack", () -> new BackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS), 9, 4));
    public static final RegistryObject<BackpackItem> EMERALD_BACKPACK = ITEMS.register("emerald_backpack", () -> new BackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS), 9, 5));
    public static final RegistryObject<BackpackItem> NETHERITE_BACKPACK = ITEMS.register("netherite_backpack", () -> new BackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS), 9, 6));

    public static final RegistryObject<BackpackItem> AMETHYST_BACKPACK = ITEMS.register("amethyst_backpack", () -> new BackpackItem(new Item.Properties().stacksTo(1).tab(isModLoaded("chaosawakens") ? BBItemGroups.BACKPACKS : null), 9, 7));
    public static final RegistryObject<BackpackItem> RUBY_BACKPACK = ITEMS.register("ruby_backpack", () -> new BackpackItem(new Item.Properties().stacksTo(1).tab(isModLoaded("chaosawakens") ? BBItemGroups.BACKPACKS : null), 9, 8));
    
    public static final RegistryObject<EnderChestBackpackItem> ENDER_BACKPACK = ITEMS.register("ender_backpack", () -> new EnderChestBackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS)));
}