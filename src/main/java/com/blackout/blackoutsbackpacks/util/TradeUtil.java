package com.blackout.blackoutsbackpacks.util;

import com.blackout.blackoutsbackpacks.items.DyeableBackpackItem;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.*;
import net.minecraftforge.event.village.VillagerTradesEvent;

import java.util.List;
import java.util.Random;

public class TradeUtil {
    public static final int NOVICE = 1;
    public static final int APPRENTICE = 2;
    public static final int JOURNEYMAN = 3;
    public static final int EXPERT = 4;
    public static final int MASTER = 5;

    public static void addVillagerTrades(VillagerTradesEvent event, int level, VillagerTrades.ITrade... trades) {
        for (VillagerTrades.ITrade trade : trades) event.getTrades().get(level).add(trade);
    }

    public static void addVillagerTrades(VillagerTradesEvent event, VillagerProfession profession, int level, VillagerTrades.ITrade... trades) {
        if (event.getType() == profession) addVillagerTrades(event, level, trades);
    }

    public static class BBDyedBackpackForEmeraldsTrade implements VillagerTrades.ITrade {
        private final Item item;
        private final int value;
        private final int maxUses;
        private final int villagerXp;

        public BBDyedBackpackForEmeraldsTrade(Item item, int value, int maxUses, int villagerXp) {
            this.item = item;
            this.value = value;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
        }

        public MerchantOffer getOffer(Entity entity, Random random) {
            ItemStack itemstack = new ItemStack(Items.EMERALD, this.value);
            ItemStack itemstack1 = new ItemStack(this.item);
            if (this.item instanceof DyeableBackpackItem) {
                List<DyeItem> list = Lists.newArrayList();
                list.add(getRandomDye(random));
                if (random.nextFloat() > 0.7F) {
                    list.add(getRandomDye(random));
                }

                if (random.nextFloat() > 0.8F) {
                    list.add(getRandomDye(random));
                }

                itemstack1 = IDyeableArmorItem.dyeArmor(itemstack1, list);
            }

            return new MerchantOffer(itemstack, itemstack1, this.maxUses, this.villagerXp, 0.2F);
        }

        private static DyeItem getRandomDye(Random random) {
            return DyeItem.byColor(DyeColor.byId(random.nextInt(16)));
        }
    }
}
