package fonnymunkey.simplehats.mixin.core;

import dev.emi.trinkets.api.TrinketsApi;
import fonnymunkey.simplehats.common.item.ClothItem;
import fonnymunkey.simplehats.common.item.HatItem;
import fonnymunkey.simplehats.common.item.TailItem;
import fonnymunkey.simplehats.common.item.etcItems.BlankShoes;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class MixinHumanoidArmorLayer {

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    public void simplehats_renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot, int light, BipedEntityModel<LivingEntity> model, CallbackInfo ci) {
        if(entity instanceof PlayerEntity && armorSlot.equals(EquipmentSlot.HEAD)) {
            TrinketsApi.getTrinketComponent(entity).ifPresent(component -> {
                var hasHatTrinket = !component.getEquipped(stack -> stack.getItem() instanceof HatItem).isEmpty();
                if(hasHatTrinket) ci.cancel();
            });
        }

        else if (entity instanceof PlayerEntity && armorSlot.equals(EquipmentSlot.CHEST)) {
            TrinketsApi.getTrinketComponent(entity).ifPresent(component -> {
                var hasBeltTrinket = !component.getEquipped(stack -> stack.getItem() instanceof ClothItem).isEmpty();
                if (hasBeltTrinket) ci.cancel(); // 바지 렌더링 취소
            });
        }

        else if (entity instanceof PlayerEntity && armorSlot.equals(EquipmentSlot.LEGS)) {
            TrinketsApi.getTrinketComponent(entity).ifPresent(component -> {
                var hasBeltTrinket = !component.getEquipped(stack -> stack.getItem() instanceof TailItem).isEmpty();
                if (hasBeltTrinket) ci.cancel(); // 바지 렌더링 취소
            });
        }

        else if (entity instanceof PlayerEntity && armorSlot.equals(EquipmentSlot.FEET)) {
            TrinketsApi.getTrinketComponent(entity).ifPresent(component -> {
                var hasShoesTrinket = !component.getEquipped(stack -> stack.getItem() instanceof BlankShoes).isEmpty();
                if (hasShoesTrinket) ci.cancel(); // 바지 렌더링 취소
            });
        }
    }
}
