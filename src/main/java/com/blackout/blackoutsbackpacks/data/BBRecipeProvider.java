package com.blackout.blackoutsbackpacks.data;

import com.blackout.blackoutsbackpacks.BlackoutsBackpacks;
import com.blackout.blackoutsbackpacks.registry.BBItems;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

import java.util.function.Consumer;

public class BBRecipeProvider extends RecipeProvider {
    public BBRecipeProvider(DataGenerator gen) {
        super(gen);
    }

    private static void surroundItem(Consumer<IFinishedRecipe> consumer, IItemProvider pOutput, IItemProvider pSurroundMaterial, IItemProvider pItemMaterial) {
        ShapedRecipeBuilder.shaped(pOutput).define('#', pSurroundMaterial).define('X', pItemMaterial).pattern("###").pattern("#X#").pattern("###").unlockedBy("has_" + pSurroundMaterial.asItem(), has(pSurroundMaterial)).save(consumer);
    }

    private static void backpackSmithing(Consumer<IFinishedRecipe> consumer, Item output, Item input, Item ingredient) {
        SmithingRecipeBuilder.smithing(Ingredient.of(input), Ingredient.of(ingredient), output).unlocks("has_" + ingredient, has(ingredient)).save(consumer, "blackoutsbackpacks:" + output + "_smithing");
    }

    public String getName() {
        return BlackoutsBackpacks.MODNAME + ": Recipes";
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        surroundItem(consumer, BBItems.LEATHER_BACKPACK.get(), Items.LEATHER, Items.CHEST);
        surroundItem(consumer, BBItems.ENDER_BACKPACK.get(), Items.LEATHER, Items.ENDER_CHEST);

        backpackSmithing(consumer, BBItems.IRON_BACKPACK.get(), BBItems.LEATHER_BACKPACK.get(), Items.IRON_BLOCK);
        backpackSmithing(consumer, BBItems.GOLD_BACKPACK.get(), BBItems.IRON_BACKPACK.get(), Items.GOLD_BLOCK);
        backpackSmithing(consumer, BBItems.DIAMOND_BACKPACK.get(), BBItems.GOLD_BACKPACK.get(), Items.DIAMOND_BLOCK);
        backpackSmithing(consumer, BBItems.EMERALD_BACKPACK.get(), BBItems.DIAMOND_BACKPACK.get(), Items.EMERALD_BLOCK);
        backpackSmithing(consumer, BBItems.NETHERITE_BACKPACK.get(), BBItems.EMERALD_BACKPACK.get(), Items.NETHERITE_INGOT);
    }
}
