package fonnymunkey.simplehats.client.tail;

import dev.emi.trinkets.api.TrinketsApi;
import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.common.item.TailItem;
import fonnymunkey.simplehats.util.TailEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class TailLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    public TailLayer(FeatureRendererContext<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack poseStack, VertexConsumerProvider buffer, int packedLight,
                       T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks,
                       float age, float netHeadYaw, float headPitch) {

        // 1인칭 시점에서 꼬리가 카메라에 겹치지 않게 방지
        if (livingEntity == MinecraftClient.getInstance().cameraEntity &&
                MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON &&
                SimpleHats.config.client.forceFirstPersonNoRender) return;

        TrinketsApi.getTrinketComponent(livingEntity)
                .ifPresent(component -> component.forEach((slotReference, itemStack) -> {
                    if (!itemStack.isEmpty() && itemStack.getItem() instanceof TailItem) {
                        renderTail(itemStack, poseStack, buffer, packedLight, livingEntity,
                                limbSwing, limbSwingAmount, partialTicks, age, netHeadYaw, headPitch);
                    }
                }));
    }

    private void renderTail(ItemStack itemStack, MatrixStack poseStack, VertexConsumerProvider buffer,
                            int packedLight, T livingEntity, float limbSwing, float limbSwingAmount,
                            float partialTicks, float age, float netHeadYaw, float headPitch) {
        if (!livingEntity.isInvisible()) {
            poseStack.push();

            // 꼬리의 위치로 이동 (허리~엉덩이 근처)
            translateToTail(poseStack, livingEntity, limbSwing, limbSwingAmount, age);


            MinecraftClient.getInstance().getEntityRenderDispatcher().getHeldItemRenderer()
                    .renderItem(livingEntity, itemStack,
                            net.minecraft.client.render.model.json.ModelTransformationMode.GROUND,
                            false, poseStack, buffer, packedLight);

            poseStack.pop();
        }
    }

    private static void translateToTail(MatrixStack poseStack, LivingEntity entity,
                                        float limbSwing, float limbSwingAmount, float age) {

        // === 기본 위치 (꼬리 뿌리) ===
        float baseY = 0.9F;
        float baseZ = 0.15F;

        // === 기본 회전값 ===
        float rotationPitch = 0.0F; // 숙이기
        float rotationYaw = 0.0F;   // 좌우 흔들림
        float tailBend = 0.0F;      // 꼬리 끝만 흔들리게 적용할 추가 회전

        // 웅크릴 때 꼬리 위치와 각도 조정
        if (entity.isInSneakingPose()) {
            baseY = 0.85F;
            baseZ = 0.6F;
            rotationPitch = 45.0F;
        }

        // 걷는 중 흔들림
        rotationYaw += (float) Math.sin(limbSwing * 0.6F) * 15.0F * limbSwingAmount;

        // 미세한 진동 — 꼬리 끝부분만 흔들림
        tailBend += (float) Math.sin(age * 0.15F) * 3.0F;

        // === 1️⃣ 뿌리 위치로 이동 ===
        poseStack.translate(0.0F, baseY, baseZ);

        // === 2️⃣ 회전 중심이 뿌리에 고정된 상태로 회전 ===
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotationPitch));
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F + rotationYaw));

        // === 3️⃣ 꼬리의 길이 방향으로 이동 후 끝부분만 추가 회전 (진동) ===
        poseStack.translate(0.0F, 0.0F, 0.1F); // 꼬리 중심보다 약간 뒤쪽으로
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(tailBend));

        // === 스케일 ===
        poseStack.scale(0.75F, -0.75F, -0.75F);
    }
}