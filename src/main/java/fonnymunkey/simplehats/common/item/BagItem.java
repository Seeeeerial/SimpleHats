package fonnymunkey.simplehats.common.item;

import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.common.init.ModRegistry;
import fonnymunkey.simplehats.util.HatEntry;
import fonnymunkey.simplehats.util.HatEntry.HatSeason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.WeightedListIntProvider;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BagItem extends Item {

    private HatSeason hatSeason = HatSeason.NONE;
    private boolean seasonal = false;
    private Rarity rarity = Rarity.COMMON; // Don't let enchantment glint artificially change loot results

    private List<Item> availableLootList = new ArrayList<>();
    private WeightedListIntProvider availableLootWeighted = null;

    public BagItem(Rarity rarity) {
        super(new Item.Settings().rarity(rarity));
        this.rarity = rarity;
    }

    public BagItem(HatSeason hatSeason) {
        super(new Item.Settings().rarity(Rarity.EPIC));
        this.hatSeason = hatSeason;
        this.seasonal = true;
        this.rarity = Rarity.EPIC;
    }

    public static SoundEvent getUnwrapFinishSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        player.playSound(getUnwrapFinishSound(), 1.0F,
                1.0F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.4F);
        itemStack.decrement(1);

        if (!level.isClient()) {
            // Seasonal bag chance
            if (!this.seasonal && HatSeason.getSeason() != HatSeason.NONE) {
                if (level.getRandom().nextFloat() * 100.0F < SimpleHats.config.common.seasonalBagChance) {
                    player.dropItem(getSeasonalBag());
                }
            }
            player.dropItem(this.getBagResult(level));
        }
        return TypedActionResult.success(itemStack, level.isClient());
    }

    private static Item getSeasonalBag() {
        switch (HatSeason.getSeason()) {
            case EASTER -> { return ModRegistry.HATBAG_EASTER; }
            case SUMMER -> { return ModRegistry.HATBAG_SUMMER; }
            case HALLOWEEN -> { return ModRegistry.HATBAG_HALLOWEEN; }
            case FESTIVE -> { return ModRegistry.HATBAG_FESTIVE; }
        }
        SimpleHats.logger.log(org.apache.logging.log4j.Level.ERROR, "Failed to get seasonal bag type.");
        return Items.AIR;
    }

    private ItemConvertible getBagResult(World level) {
        if (availableLootList.size() == 0) {
            // HatItems
            for (HatItem hat : ModRegistry.hatList) {
                if ((hat.getHatEntry().getHatRarity() == this.rarity || this.seasonal) &&
                        hat.getHatEntry().getHatWeight() != 0 &&
                        hat.getHatEntry().getHatSeason() == this.hatSeason) {
                    availableLootList.add(hat);
                }
            }
            // TailItems
            for (TailItem tail : ModRegistry.tailList) {
                if (tail.getTailEntry().getTailRarity() == this.rarity &&
                        tail.getTailEntry().getTailWeight() != 0) {
                    availableLootList.add(tail);
                }
            }
            // ChestItems
            for (ChestItem chest : ModRegistry.chestList) {
                if (chest.getChestEntry().getChestRarity() == this.rarity &&
                        chest.getChestEntry().getChestWeight() != 0) {
                    availableLootList.add(chest);
                }
            }

            if (availableLootList.size() == 0) {
                SimpleHats.logger.log(org.apache.logging.log4j.Level.ERROR,
                        "Failed to populate " + this.getName() + " loot list.");
                return Items.AIR;
            }
        }

        if (availableLootWeighted == null) {
            try {
                DataPool.Builder<IntProvider> tempListBuilder = DataPool.<IntProvider>builder();
                for (int i = 0; i < availableLootList.size(); i++) {
                    Item item = availableLootList.get(i);
                    int weight = (item instanceof HatItem hat) ? hat.getHatEntry().getHatWeight()
                            : (item instanceof  TailItem tail) ? tail.getTailEntry().getTailWeight()
                            : ((ChestItem) item).getChestEntry().getChestWeight();
                    tempListBuilder.add(ConstantIntProvider.create(i), weight);
                }
                availableLootWeighted = new WeightedListIntProvider(tempListBuilder.build());
            } catch (Exception ex) {
                SimpleHats.logger.log(org.apache.logging.log4j.Level.ERROR,
                        "Failed to generate " + this.getName() + " weighted loot table: " + ex);
                return Items.AIR;
            }
        }

        return availableLootList.get(availableLootWeighted.get(level.random));
    }
}