package com.blackout.blackoutsbackpacks.data;

import com.blackout.blackoutsbackpacks.BlackoutsBackpacks;
import com.blackout.blackoutsbackpacks.registry.BBItems;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.chaosawakens.ChaosAwakens;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public class BBAdvancementProvider extends AdvancementProvider {
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	private final DataGenerator generator;

	public BBAdvancementProvider(DataGenerator generatorIn) {
		super(generatorIn);
		this.generator = generatorIn;
	}

	private static Path getPath(Path pathIn, Advancement advancementIn) {
		return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
	}

	private static String id(String s) {
		return BlackoutsBackpacks.MODID + ":" + s;
	}

	@Override
	public void run(DirectoryCache cache) {
		Path path = this.generator.getOutputFolder();
		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<Advancement> consumer = (advancement) -> {
			if (!set.add(advancement.getId())) {
				throw new IllegalStateException("Duplicate advancement " + advancement.getId());
			} else {
				Path path1 = getPath(path, advancement);
				try {
					IDataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), path1);
				} catch (IOException e) {
					BlackoutsBackpacks.LOGGER.error("Couldn't save advancement {}", path1, e);
				}
			}
		};
		this.register(consumer);
	}

	public void register(Consumer<Advancement> t) {
		Advancement obtainLeatherBackpack = registerAdvancement("obtain_leather_backpack", FrameType.TASK, BBItems.LEATHER_BACKPACK.get()).parent(Advancement.Builder.advancement().build(new ResourceLocation("adventure/root"))).addCriterion("leather_backpack",
				InventoryChangeTrigger.Instance.hasItems(BBItems.LEATHER_BACKPACK.get())).save(t, id("obtain_leather_backpack"));

		Advancement obtainEnderBackpack = registerAdvancement("obtain_ender_backpack", FrameType.TASK, BBItems.ENDER_BACKPACK.get()).parent(Advancement.Builder.advancement().build(new ResourceLocation("story/form_obsidian"))).addCriterion("ender_backpack",
				InventoryChangeTrigger.Instance.hasItems(BBItems.ENDER_BACKPACK.get())).save(t, id("obtain_ender_backpack"));

		Advancement obtainNetheriteBackpack = registerAdvancement("obtain_netherite_backpack", FrameType.GOAL, BBItems.NETHERITE_BACKPACK.get()).parent(Advancement.Builder.advancement().build(new ResourceLocation("nether/obtain_ancient_debris"))).addCriterion("netherite_backpack",
				InventoryChangeTrigger.Instance.hasItems(BBItems.NETHERITE_BACKPACK.get())).save(t, id("obtain_netherite_backpack"));

		Advancement obtainRubyBackpack = registerAdvancement("obtain_ruby_backpack", FrameType.CHALLENGE, BBItems.RUBY_BACKPACK.get()).parent(Advancement.Builder.advancement().build(new ResourceLocation(ChaosAwakens.MODID, "root"))).addCriterion("ruby_backpack",
				InventoryChangeTrigger.Instance.hasItems(BBItems.RUBY_BACKPACK.get())).save(t, id("obtain_ruby_backpack"));
	}

	private Advancement.Builder registerAdvancement(String name, FrameType type, IItemProvider... items) {
		Validate.isTrue(items.length > 0);
		return Advancement.Builder.advancement().display(items[0], new TranslationTextComponent("advancements.blackoutsbackpacks." + name + ".title"),
				new TranslationTextComponent("advancements.blackoutsbackpacks." + name + ".description"), null, type, true, true, false);
	}
}
