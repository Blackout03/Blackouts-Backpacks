package com.blackout.blackoutsbackpacks.registry;

import com.blackout.blackoutsbackpacks.BlackoutsBackpacks;
import com.blackout.blackoutsbackpacks.items.*;
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

    public static final RegistryObject<DyeableBackpackItem> LEATHER_BACKPACK = ITEMS.register("leather_backpack", () -> new DyeableBackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS)));
    public static final RegistryObject<IronBackpackItem> IRON_BACKPACK = ITEMS.register("iron_backpack", () -> new IronBackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS)));
    public static final RegistryObject<GoldBackpackItem> GOLD_BACKPACK = ITEMS.register("gold_backpack", () -> new GoldBackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS)));
    public static final RegistryObject<DiamondBackpackItem> DIAMOND_BACKPACK = ITEMS.register("diamond_backpack", () -> new DiamondBackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS)));
    public static final RegistryObject<EmeraldBackpackItem> EMERALD_BACKPACK = ITEMS.register("emerald_backpack", () -> new EmeraldBackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS)));
    public static final RegistryObject<NetheriteBackpackItem> NETHERITE_BACKPACK = ITEMS.register("netherite_backpack", () -> new NetheriteBackpackItem(new Item.Properties().stacksTo(1).fireResistant().tab(BBItemGroups.BACKPACKS)));

    public static final RegistryObject<AmethystBackpackItem> AMETHYST_BACKPACK = ITEMS.register("amethyst_backpack", () -> new AmethystBackpackItem(new Item.Properties().stacksTo(1).fireResistant().tab(isModLoaded("chaosawakens") ? BBItemGroups.BACKPACKS : null)));
    public static final RegistryObject<RubyBackpackItem> RUBY_BACKPACK = ITEMS.register("ruby_backpack", () -> new RubyBackpackItem(new Item.Properties().stacksTo(1).fireResistant().tab(isModLoaded("chaosawakens") ? BBItemGroups.BACKPACKS : null)));
    
    public static final RegistryObject<EnderChestBackpackItem> ENDER_BACKPACK = ITEMS.register("ender_backpack", () -> new EnderChestBackpackItem(new Item.Properties().stacksTo(1).tab(BBItemGroups.BACKPACKS)));
}