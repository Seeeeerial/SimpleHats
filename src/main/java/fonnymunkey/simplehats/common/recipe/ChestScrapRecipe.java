package fonnymunkey.simplehats.common.recipe;

import fonnymunkey.simplehats.common.init.ModRegistry;
import fonnymunkey.simplehats.common.item.ChestItem;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ChestScrapRecipe extends SpecialCraftingRecipe {
    public ChestScrapRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public String getGroup() {
        return "simplehats:hatscraps";
    }

    @Override
    public boolean matches(RecipeInputInventory craftingInventory, World world) {
        int[] list = processInventory(craftingInventory);
        return list[0] != -1 && list[1] != -1;
    }

    @Override
    public ItemStack craft(RecipeInputInventory craftingInventory, DynamicRegistryManager registryManager) {
        int[] list = processInventory(craftingInventory);
        if(list[0] != -1 && list[1] != -1) {
            return switch(((ChestItem)craftingInventory.getStack(list[0]).getItem()).getChestEntry().getChestRarity()) {
                case COMMON -> new ItemStack(ModRegistry.HATSCRAPS_COMMON);
                case UNCOMMON -> new ItemStack(ModRegistry.HATSCRAPS_UNCOMMON);
                case RARE, EPIC -> new ItemStack(ModRegistry.HATSCRAPS_RARE);
            };
        }
        return ItemStack.EMPTY;
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory craftingInventory) {
        DefaultedList<ItemStack> remainList = DefaultedList.ofSize(craftingInventory.size(), ItemStack.EMPTY);
        for(int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack slot = craftingInventory.getStack(i);
            if(!slot.isEmpty() && slot.getItem() instanceof ShearsItem) {
                ItemStack slot1 = slot.copy();
                if(slot1.isDamageable()) {
                    slot1.setDamage(slot.getDamage() + 1);
                    if(slot1.getDamage() >= slot1.getMaxDamage()) slot1 = ItemStack.EMPTY;
                }
                remainList.set(i, slot1);
                break;
            }
        }
        return remainList;
    }

    private static int[] processInventory(RecipeInputInventory craftingInventory) {
        int chestIndex = -1;
        int shearsIndex = -1;

        for (int i = 0; i < craftingInventory.size(); i++) {
            ItemStack stack = craftingInventory.getStack(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ChestItem) {
                    String itemName = stack.getItem().getTranslationKey().toLowerCase();
                    if (!itemName.contains("blank")) {
                        chestIndex = i;
                    }
                }
                else if (stack.getItem() instanceof ShearsItem) shearsIndex = i;
            }
        }

        return new int[]{chestIndex, shearsIndex};
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.CHESTSCRAP_SERIALIZER;
    }
}