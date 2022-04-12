package com.blackout.blackoutsbackpacks.registry;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BBItemGroups {
	public static final ItemGroup BACKPACKS = new ItemGroup("blackoutsbackpacks.backpacks") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(BBItems.LEATHER_BACKPACK.get());
		}
	};
}
