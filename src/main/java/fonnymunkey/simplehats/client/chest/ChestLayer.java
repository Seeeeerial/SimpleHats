package fonnymunkey.simplehats.client.chest;

import dev.emi.trinkets.api.TrinketsApi;
import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.common.item.ChestItem;
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

public class ChestLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    public ChestLayer(FeatureRendererContext<T, M> renderer) {
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
                    if (!itemStack.isEmpty() && itemStack.getItem() instanceof ChestItem) {
                        renderChest(itemStack, poseStack, buffer, packedLight, livingEntity,
                                limbSwing, limbSwingAmount, partialTicks, age, netHeadYaw, headPitch);
                    }
                }));
    }

    private void renderChest(ItemStack itemStack, MatrixStack poseStack, VertexConsumerProvider buffer,
                             int packedLight, T livingEntity, float limbSwing, float limbSwingAmount,
                             float partialTicks, float age, float netHeadYaw, float headPitch) {

        // "blank"라는 단어가 이름에 포함되어 있으면 렌더링 생략
        String itemName = itemStack.getItem().getTranslationKey().toLowerCase();
        if (itemName.contains("blank")) {
            return;
        }

        if (!livingEntity.isInvisible()) {
            poseStack.push();

            // 꼬리 위치로 이동 (허리~엉덩이 근처)
            translateToChest(poseStack, livingEntity, limbSwing, limbSwingAmount, age, itemStack);

            MinecraftClient.getInstance().getEntityRenderDispatcher().getHeldItemRenderer()
                    .renderItem(livingEntity, itemStack,
                            net.minecraft.client.render.model.json.ModelTransformationMode.GROUND,
                            false, poseStack, buffer, packedLight);

            poseStack.pop();
        }
    }

    private static void translateToChest(MatrixStack poseStack, LivingEntity entity,
                                         float limbSwing, float limbSwingAmount, float age,
                                         ItemStack itemStack) {

        // === 날개 뿌리 위치 (등 중앙) ===
        float baseY = 0.4F;
        float baseZ = 0.9F;
        float rotationPitch = 0.0F;

        // 웅크릴 때 위치 보정
        if (entity.isInSneakingPose()) {
            baseY = 0.5F;
            baseZ = 1.1F;
            rotationPitch = 15.0F;
        }

        // "fallen_angel_wings"인 경우 등에서 좀 더 뒤로 밀기
        boolean isFallenWing = itemStack.getItem().getTranslationKey().toLowerCase().contains("fallen_angel_wings");
        if (isFallenWing) {
            baseZ += 0.2F; // 날개가 몸에 너무 붙지 않게
        }

        // === 뿌리로 이동 ===
        poseStack.translate(0.0F, baseY, baseZ);
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotationPitch));

        // === 뿌리 회전은 최소화 ===
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

        // === 끝부분 펄럭임 ===
        float walkSwing = (float) Math.sin(limbSwing * 0.4F) * 3.0F * limbSwingAmount; // 걷기 흔들림 약하게
        float idleFlap = (float) Math.sin(age * 0.1F) * 2.0F; // 미세한 시간 기반 펄럭임

        // 끝부분만 회전 적용
        poseStack.translate(0.0F, 0.0F, 0.3F); // 날개 길이만큼 끝부분 이동
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(idleFlap));
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(walkSwing));

        // === 스케일 ===
        if(isFallenWing) {
            poseStack.scale(5.0F, -5.0F, -5.0F);
        }
        else {
            poseStack.scale(4.0F, -4.0F, -4.0F);
        }
    }
}