package fonnymunkey.simplehats.common.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketEnums;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.client.TrinketRenderer;
import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.util.ChestEntry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import java.util.List;

public class ChestItem  extends TrinketItem implements TrinketRenderer {
    private ChestEntry chestEntry;

    public ChestItem(ChestEntry entry) {
        super(new Item.Settings()
                .maxCount(1)
                //.group(SimpleHats.HAT_TAB)
                .rarity(entry.getChestRarity())
                .fireproof());
        this.chestEntry = entry;
    }

    public ChestEntry getChestEntry() {
        return this.chestEntry;
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity.getWorld().isClient()) return;
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World level, List<Text> tooltip, TooltipContext flag) {
        if(((ChestItem)itemStack.getItem()).getChestEntry().getChestVariantRange()>0) tooltip.add(Text.translatable("tooltip.simplehats.variant"));
        if(((ChestItem)itemStack.getItem()).getChestEntry().getChestName().equalsIgnoreCase("special")) {
            if(itemStack.getNbt()!=null && itemStack.getNbt().getInt("CustomModelData") > 0) {
                tooltip.add(Text.translatable("tooltip.simplehats.special_true"));
            }
            else {
                tooltip.add(Text.translatable("tooltip.simplehats.special_false"));
            }
        }
    }

    @Override
    public TrinketEnums.DropRule getDropRule(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity instanceof PlayerEntity && SimpleHats.config.common.keepHatOnDeath) return TrinketEnums.DropRule.KEEP;
        else return TrinketEnums.DropRule.DEFAULT;
    }

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        //Empty, for compatibility with Accessories
    }
}
