package fonnymunkey.simplehats.util;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Rarity;

public class ChestEntry {
    public static final ChestEntry.ChestDyeSettings DYE_NONE = new ChestEntry.ChestDyeSettings(false, 0);

    @SerializedName("name")
    private String chestName;
    @SerializedName("rarity")
    private Rarity chestRarity;
    @SerializedName("weight")
    private int chestWeight;
    @SerializedName("variants")
    private int variantRange;
    @SerializedName("Dye Settings")
    private ChestEntry.ChestDyeSettings chestDyeSettings;

    public ChestEntry(String name) {
        this(name, Rarity.COMMON, 0, 0, DYE_NONE);
    }
    public ChestEntry(String name, Rarity rarity, int weight) {
        this(name, rarity, weight, 0, DYE_NONE);
    }
    public ChestEntry(String name, Rarity rarity, int weight, int variantRange) {
        this(name, rarity, weight, variantRange, DYE_NONE);
    }
    public ChestEntry(String name, Rarity rarity, int weight, int variantRange, ChestEntry.ChestDyeSettings dye) {
        this.chestName = name;
        this.chestRarity = rarity;
        this.chestWeight = Math.max(0, weight);
        this.variantRange = Math.max(0, variantRange);
        this.chestDyeSettings = dye;
    }

    public String getChestName() {
        return this.chestName;
    }

    public Rarity getChestRarity() {
        return this.chestRarity;
    }

    public int getChestWeight() {
        return this.chestWeight;
    }

    public int getChestVariantRange() { return this.variantRange; }

    public ChestEntry.ChestDyeSettings getChestDyeSettings() { return this.chestDyeSettings; }

    public void validateDeserializedEntry() {
        //name already checked
        if(this.chestRarity == null) this.chestRarity = Rarity.COMMON;
        this.chestWeight = Math.max(0, this.chestWeight);
        this.variantRange = Math.max(0, this.variantRange);
        if(this.chestDyeSettings == null) this.chestDyeSettings = DYE_NONE;
    }
    ///////////
    //Dye
    ///////////
    public static class ChestDyeSettings {
        @SerializedName("enabled")
        private boolean useDye;
        @SerializedName("decimal color")
        private int defaultColor;

        public ChestDyeSettings(boolean useDye, int defaultColor) {
            this.useDye = useDye;
            this.defaultColor = defaultColor;
        }

        public boolean getUseDye() {
            return useDye;
        }

        public int getColorCode() {
            return defaultColor;
        }
    }
}