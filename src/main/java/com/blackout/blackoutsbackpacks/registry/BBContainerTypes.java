package com.blackout.blackoutsbackpacks.registry;

import com.blackout.blackoutsbackpacks.BlackoutsBackpacks;
import com.blackout.blackoutsbackpacks.client.gui.BBContainerScreen;
import com.blackout.blackoutsbackpacks.container.BBContainer.BackpackContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BBContainerTypes {
	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, BlackoutsBackpacks.MODID);

	public static final RegistryObject<ContainerType<BackpackContainer>> BACKPACK_CONTAINER_TYPE = register("backpack", BackpackContainer::createContainerFromItemstack);

	private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, IContainerFactory<T> factory) {
		return CONTAINER_TYPES.register(name, () -> IForgeContainerType.create(factory));
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerScreens(FMLClientSetupEvent event) {
		ScreenManager.register(BACKPACK_CONTAINER_TYPE.get(), BBContainerScreen::new);
	}
}
