package fonnymunkey.simplehats.client.cloth;

import dev.emi.trinkets.api.TrinketsApi;
import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.common.item.ClothItem;
import fonnymunkey.simplehats.common.item.ClothItemDyeable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.OverlayTexture;

public class ClothLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    private final BipedEntityModel<T> clothModel;

    public ClothLayer(FeatureRendererContext<T, M> renderer) {
        super(renderer);
        EntityModelLoader loader = MinecraftClient.getInstance().getEntityModelLoader();
        this.clothModel = new BipedEntityModel<>(loader.getModelPart(EntityModelLayers.PLAYER_INNER_ARMOR));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       T entity, float limbSwing, float limbSwingAmount, float partialTicks,
                       float age, float netHeadYaw, float headPitch) {

        // 1인칭에서 렌더 안함
        if (entity == MinecraftClient.getInstance().cameraEntity &&
                MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON &&
                SimpleHats.config.client.forceFirstPersonNoRender) return;

        // Trinket의 ClothItem 렌더
        TrinketsApi.getTrinketComponent(entity).ifPresent(component ->
                component.forEach((slotReference, stack) -> {
                    if (!stack.isEmpty() && stack.getItem() instanceof ClothItem) {
                        renderCloth(stack, matrices, vertexConsumers, light, entity,
                                limbSwing, limbSwingAmount, age, netHeadYaw, headPitch);
                    }
                }));
    }

    private void renderCloth(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                 int light, T entity,
                                 float limbSwing, float limbSwingAmount, float age,
                                 float netHeadYaw, float headPitch) {

        if (entity.isInvisible()) return;

        // 아이템 이름
        String itemName = stack.getItem().getTranslationKey()
                .replace("item." + SimpleHats.modId + ".", "")
                .toLowerCase();
        if (itemName.contains("blank")) return;

        matrices.push();

        // 💡 올바른 순서로 애니메이션 적용
        EntityModel<T> contextModel = getContextModel();
        if (contextModel instanceof BipedEntityModel<?> bipedContext) {
            @SuppressWarnings("unchecked")
            BipedEntityModel<T> biped = (BipedEntityModel<T>) bipedContext;

            // ✅ 1. 먼저 animateModel 호출 (기본 애니메이션 상태 설정)
            clothModel.animateModel(entity, limbSwing, limbSwingAmount, age);

            // ✅ 2. setAngles 호출 (포즈 적용 - 웅크리기, 수영 등)
            clothModel.setAngles(entity, limbSwing, limbSwingAmount, age, netHeadYaw, headPitch);

            // ✅ 3. 마지막으로 copyStateTo로 정확한 상태 복사
            biped.copyStateTo(clothModel);
        }

        // 텍스처
        Identifier texture = new Identifier(SimpleHats.modId, "textures/item/cloth/" + itemName + ".png");

        // 염색 컬러 적용
        int color = 0xFFFFFF;
        if (stack.getItem() instanceof ClothItemDyeable dyeable)
            color = dyeable.getColor(stack);

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        // 🔥 실제 갑옷처럼 전체 파츠 렌더
        var buffer = vertexConsumers.getBuffer(clothModel.getLayer(texture));

        clothModel.head.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1f);
        clothModel.hat.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1f);
        clothModel.body.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1f);
        clothModel.rightArm.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1f);
        clothModel.leftArm.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1f);
        clothModel.rightLeg.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1f);
        clothModel.leftLeg.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1f);

        matrices.pop();
    }
}