package com.blackout.blackoutsbackpacks;

import com.blackout.blackoutsbackpacks.data.BBAdvancementProvider;
import com.blackout.blackoutsbackpacks.data.BBRecipeProvider;
import com.blackout.blackoutsbackpacks.data.BBTagProvider;
import com.blackout.blackoutsbackpacks.registry.BBContainerTypes;
import com.blackout.blackoutsbackpacks.registry.BBItems;
import com.blackout.blackoutsbackpacks.registry.BBStats;
import io.github.chaosawakens.data.CAAdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BlackoutsBackpacks.MODID)
public class BlackoutsBackpacks {
    public static final String MODID = "blackoutsbackpacks";
    public static final String MODNAME = "Blackout's Backpacks";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger();


    public BlackoutsBackpacks() {
        LOGGER.debug(MODNAME + " Version is: " + VERSION);
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::gatherData);

        BBItems.ITEMS.register(eventBus);
        BBStats.STAT_TYPES.register(eventBus);
        BBContainerTypes.CONTAINER_TYPES.register(eventBus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void gatherData(final GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        final ExistingFileHelper existing = event.getExistingFileHelper();

        if (event.includeServer()) {
            dataGenerator.addProvider(new BBAdvancementProvider(dataGenerator));
            dataGenerator.addProvider(new BBRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new BBTagProvider.BBItemTagProvider(dataGenerator, existing));
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        BBContainerTypes.registerScreens(event);
    }
}
