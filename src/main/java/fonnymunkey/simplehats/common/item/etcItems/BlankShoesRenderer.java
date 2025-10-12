package fonnymunkey.simplehats.common.item.etcItems;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class BlankShoesRenderer  implements TrinketRenderer {
    @Override
    public void render(
            ItemStack stack,
            SlotReference slotReference,
            EntityModel<? extends LivingEntity> contextModel,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            LivingEntity entity,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        // 아무것도 랜더링 하지 않음
    }
}
