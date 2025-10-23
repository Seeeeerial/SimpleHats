package fonnymunkey.simplehats.client.chest;

import dev.emi.trinkets.api.TrinketsApi;
import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.common.item.ChestItem;
import fonnymunkey.simplehats.common.item.HatItem;
import fonnymunkey.simplehats.util.ChestEntry;
import fonnymunkey.simplehats.util.HatEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.RotationAxis;

public class ChestLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    public ChestLayer(FeatureRendererContext<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack poseStack, VertexConsumerProvider buffer, int packedLight,
                       T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks,
                       float age, float netHeadYaw, float headPitch) {

        // 1인칭 시점에서 카메라에 겹치지 않게 방지
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

        // 이름에 "blank" 포함 시 렌더링 생략
        String itemName = itemStack.getItem().getTranslationKey().toLowerCase();
        if (itemName.contains("blank")) return;

        boolean isFallenWing = itemName.contains("fallen_angel_wings");

        if (!livingEntity.isInvisible()) {
            poseStack.push();

            // 위치 보정 및 회전
            translateToChest(poseStack, livingEntity, limbSwing, limbSwingAmount, age, itemStack);

            MinecraftClient.getInstance().getEntityRenderDispatcher().getHeldItemRenderer()
                    .renderItem(livingEntity, itemStack,
                            net.minecraft.client.render.model.json.ModelTransformationMode.GROUND,
                            false, poseStack, buffer, packedLight);

            poseStack.pop();
        }

        if(livingEntity instanceof PlayerEntity) {
            ChestEntry.ChestParticleSettings particleSettings = ((ChestItem)itemStack.getItem()).getChestEntry().getChestParticleSettings();
            ParticleEffect particle = particleSettings.getParticleType();
            if (particle == null) {
                return; // null이면 파티클 호출하지 않음
            }
            if(particleSettings.getUseParticles() && !MinecraftClient.getInstance().isPaused() && livingEntity.getRandom().nextFloat() < (livingEntity.isInvisible() ? particleSettings.getParticleFrequency()/2 : particleSettings.getParticleFrequency())) {
                double xSpread = isFallenWing ? 4.0 : 2.0; // 원하는 범위
                double zSpread = 1.0;

                double x = livingEntity.getX() + (livingEntity.getRandom().nextDouble() - 0.5) * xSpread;
                double z = livingEntity.getZ() + (livingEntity.getRandom().nextDouble() - (isFallenWing ? 1.3 : 0.7)) * zSpread;
                double d0 = livingEntity.getRandom().nextGaussian() * 0.02D,
                        d1 = livingEntity.getRandom().nextGaussian() * 0.02D,
                        d2 = livingEntity.getRandom().nextGaussian() * 0.02D,
                        y = switch(particleSettings.getParticleMovement()) {
                            case TRAILING_CHEST -> livingEntity.getY()+0.9;
                            case TRAILING_FEET -> livingEntity.getY()+0.25;
                            case TRAILING_FULL -> livingEntity.getRandomBodyY();
                        };

                // === 검은 깃털 전용 파티클 ===
                if (isFallenWing) {
                    // 잿빛 깃털
                    livingEntity.getWorld().addParticle(ParticleTypes.ASH, x, y + 0.1, z, d0, d1 * 0.3, d2);

                    // 연기 잔상
                    livingEntity.getWorld().addParticle(ParticleTypes.SMOKE, x, y + 0.2, z,
                            d0 * 0.3, d1 * 0.2, d2 * 0.3);

                    // 희미한 빛의 잔상 (엔드로드)
                    if (livingEntity.getRandom().nextFloat() < 0.3f) {
                        livingEntity.getWorld().addParticle(ParticleTypes.END_ROD, x, y + 0.25, z,
                                0.0, -0.02, 0.0);
                    }
                } else {
                    // 일반 날개용 기본 파티클
                    livingEntity.getWorld().addParticle(particleSettings.getParticleType(),
                            x, y, z, d0, d1, d2);
                }
            }
        }
    }

    private static void translateToChest(MatrixStack poseStack, LivingEntity entity,
                                         float limbSwing, float limbSwingAmount, float age,
                                         ItemStack itemStack) {

        // === 날개 뿌리 위치 (등 중앙) ===
        float baseY = 0.7F;
        float baseZ = 0.9F;
        float rotationPitch = 0.0F;

        // 웅크릴 때 위치 보정
        if (entity.isInSneakingPose()) {
            baseY = 0.8F;
            baseZ = 1.1F;
            rotationPitch = 15.0F;
        }

        // "fallen_angel_wings"인 경우 등에서 좀 더 뒤로 밀기
        boolean isFallenWing = itemStack.getItem().getTranslationKey().toLowerCase().contains("fallen_angel_wings");
        if (isFallenWing) {
            baseZ += 0.45F;
        }

        // === 이동 및 회전 ===
        poseStack.translate(0.0F, baseY, baseZ);
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotationPitch));
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

        // === 끝부분 펄럭임 ===
        float walkSwing = (float) Math.sin(limbSwing * 0.4F) * 3.0F * limbSwingAmount;
        float idleFlap = (float) Math.sin(age * 0.1F) * 2.0F;

        poseStack.translate(0.0F, 0.0F, 0.3F);
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(idleFlap));
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(walkSwing));

        // === 스케일 ===
        if (isFallenWing) {
            poseStack.scale(5.0F, -5.0F, -5.0F);
        } else {
            poseStack.scale(4.0F, -4.0F, -4.0F);
        }
    }
}