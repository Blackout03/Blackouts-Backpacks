package com.blackout.blackoutsbackpacks.registry;

import com.blackout.blackoutsbackpacks.BlackoutsBackpacks;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class BBTags {
	public static class Items {
		public static ITag.INamedTag<Item> tag(String name) {
			return ItemTags.bind(BlackoutsBackpacks.MODID + ":" + name);
		}

		public static final ITag.INamedTag<Item> BACKPACKS = tag("backpacks");
	}
}
