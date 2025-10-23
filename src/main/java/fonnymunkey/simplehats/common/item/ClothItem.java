package fonnymunkey.simplehats.common.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketEnums;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.client.TrinketRenderer;
import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.util.ClothEntry;
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

public class ClothItem extends TrinketItem implements TrinketRenderer {
    private ClothEntry clothEntry;

    public ClothItem(ClothEntry entry) {
        super(new Item.Settings()
                .maxCount(1)
                //.group(SimpleHats.HAT_TAB)
                .rarity(entry.getClothRarity())
                .fireproof());
        this.clothEntry = entry;
    }

    public ClothEntry getClothEntry() {
        return this.clothEntry;
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity.getWorld().isClient()) return;
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World level, List<Text> tooltip, TooltipContext flag) {
        if(((ClothItem)itemStack.getItem()).getClothEntry().getClothVariantRange()>0) tooltip.add(Text.translatable("tooltip.simplehats.variant"));
        if(((ClothItem)itemStack.getItem()).getClothEntry().getClothName().equalsIgnoreCase("special")) {
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
