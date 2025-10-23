package fonnymunkey.simplehats;

import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import fonnymunkey.simplehats.client.chest.ChestLayer;
import fonnymunkey.simplehats.client.cloth.ClothLayer;
import fonnymunkey.simplehats.client.hat.HatLayer;
import fonnymunkey.simplehats.client.hatdisplay.HatDisplayModel;
import fonnymunkey.simplehats.client.hatdisplay.HatDisplayRenderer;
import fonnymunkey.simplehats.client.tail.TailLayer;
import fonnymunkey.simplehats.common.init.ModRegistry;
import fonnymunkey.simplehats.common.item.ChestItemDyeable;
import fonnymunkey.simplehats.common.item.ClothItemDyeable;
import fonnymunkey.simplehats.common.item.HatItemDyeable;
import fonnymunkey.simplehats.common.item.TailItemDyeable;
import fonnymunkey.simplehats.common.item.etcItems.BlankShoesRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.item.Item;

import static fonnymunkey.simplehats.SimpleHats.BLANK_SHOES;

@Environment(EnvType.CLIENT)
public class SimpleHatsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for(Item hat : ModRegistry.hatList) {
            if(hat instanceof HatItemDyeable hatDye) {
                ColorProviderRegistry.ITEM.register((stack, color) -> ((HatItemDyeable)stack.getItem()).getColor(stack), hatDye);
            }
            if(hat instanceof TrinketRenderer renderer) {
                TrinketRendererRegistry.registerRenderer(hat, renderer);
            }
        }
        for(Item tail : ModRegistry.tailList) {
            if(tail instanceof TailItemDyeable tailDye) {
                ColorProviderRegistry.ITEM.register((stack, color) -> ((TailItemDyeable)stack.getItem()).getColor(stack), tailDye);
            }
            if(tail instanceof TrinketRenderer renderer) {
                TrinketRendererRegistry.registerRenderer(tail, renderer);
            }
        }
        for(Item chest : ModRegistry.chestList) {
            if(chest instanceof ChestItemDyeable chestDye) {
                ColorProviderRegistry.ITEM.register((stack, color) -> ((ChestItemDyeable)stack.getItem()).getColor(stack), chestDye);
            }
            if(chest instanceof TrinketRenderer renderer) {
                TrinketRendererRegistry.registerRenderer(chest, renderer);
            }
        }
        for(Item cloth : ModRegistry.clothList) {
            if(cloth instanceof ClothItemDyeable clothDye) {
                ColorProviderRegistry.ITEM.register((stack, color) -> ((ClothItemDyeable)stack.getItem()).getColor(stack), clothDye);
            }
            if(cloth instanceof TrinketRenderer renderer) {
                TrinketRendererRegistry.registerRenderer(cloth, renderer);
            }
        }
        TrinketRendererRegistry.registerRenderer((Item)ModRegistry.HATSPECIAL, (TrinketRenderer)ModRegistry.HATSPECIAL);
        TrinketRendererRegistry.registerRenderer(BLANK_SHOES, new BlankShoesRenderer());
        /*
        if(SimpleHats.config.common.allowUpdates) {
            UUIDHandler.checkResourceUpdates();
        }
        */

        EntityRendererRegistry.register(ModRegistry.HATDISPLAYENTITY, HatDisplayRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(HatDisplayRenderer.HATDISPLAY_LOCATION, HatDisplayModel::getTexturedModelData);
        
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if(entityRenderer instanceof PlayerEntityRenderer playerEntityRenderer) {
                registrationHelper.register(new HatLayer<>(playerEntityRenderer));
                registrationHelper.register(new TailLayer<>(playerEntityRenderer));
                registrationHelper.register(new ChestLayer<>(playerEntityRenderer));
                registrationHelper.register(new ClothLayer<>(playerEntityRenderer));
            }
        });
    }
}
