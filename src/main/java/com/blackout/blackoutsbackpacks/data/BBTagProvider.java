package com.blackout.blackoutsbackpacks.data;

import com.blackout.blackoutsbackpacks.BlackoutsBackpacks;
import com.blackout.blackoutsbackpacks.registry.BBItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;

public class BBTagProvider extends BlockTagsProvider {
    public BBTagProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, BlackoutsBackpacks.MODID, existingFileHelper);
    }

    public static class BBTagProviderForBlocks extends BlockTagsProvider {
        public BBTagProviderForBlocks(DataGenerator gen, ExistingFileHelper existingFileHelper) {
            super(gen, BlackoutsBackpacks.MODID, existingFileHelper);
        }

        protected Path getPath(ResourceLocation resourceLocation) {
            return this.generator.getOutputFolder().resolve("data/" + resourceLocation.getNamespace() + "/tags/blocks/" + resourceLocation.getPath() + ".json");
        }

        public String getName() {
            return null;
        }

        protected void addTags() {
            // Nothing will ever be here as we add no blocks.
        }
    }

    public static class BBItemTagProvider extends ItemTagsProvider {
        public BBItemTagProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
            super(gen, new BBTagProviderForBlocks(gen, existingFileHelper), BlackoutsBackpacks.MODID, existingFileHelper);
        }

        protected Path getPath(ResourceLocation resourceLocation) {
            return this.generator.getOutputFolder().resolve("data/" + resourceLocation.getNamespace() + "/tags/items/" + resourceLocation.getPath() + ".json");
        }

        public String getName() {
            return BlackoutsBackpacks.MODNAME + ": Item Tags";
        }

        @Override
        protected void addTags() {
            this.tag(ItemTags.PIGLIN_LOVED).add(BBItems.GOLD_BACKPACK.get());
        }
    }
}
