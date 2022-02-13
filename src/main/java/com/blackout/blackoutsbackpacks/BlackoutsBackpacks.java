package com.blackout.blackoutsbackpacks;

import com.blackout.blackoutsbackpacks.registry.BBItems;
import com.blackout.blackoutsbackpacks.registry.BBStats;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BlackoutsBackpacks.MODID)
public class BlackoutsBackpacks {
    public static final String MODID = "blackoutsbackpacks";
    public static final String MODNAME = "Blackouts Backpacks";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger();

    public BlackoutsBackpacks() {
        LOGGER.debug(MODNAME + " Version is: " + VERSION);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BBItems.ITEMS.register(eventBus);
        BBStats.STAT_TYPES.register(eventBus);
    }
}
