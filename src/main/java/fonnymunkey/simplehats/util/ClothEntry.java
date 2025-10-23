package fonnymunkey.simplehats.util;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Rarity;

public class ClothEntry {
    public static final ClothEntry.ClothDyeSettings DYE_NONE = new ClothEntry.ClothDyeSettings(false, 0);

    @SerializedName("name")
    private String clothName;
    @SerializedName("rarity")
    private Rarity clothRarity;
    @SerializedName("weight")
    private int clothWeight;
    @SerializedName("variants")
    private int variantRange;
    @SerializedName("Dye Settings")
    private ClothEntry.ClothDyeSettings clothDyeSettings;

    public ClothEntry(String name) {
        this(name, Rarity.COMMON, 0, 0, DYE_NONE);
    }
    public ClothEntry(String name, Rarity rarity, int weight) {
        this(name, rarity, weight, 0, DYE_NONE);
    }
    public ClothEntry(String name, Rarity rarity, int weight, int variantRange) {
        this(name, rarity, weight, variantRange, DYE_NONE);
    }
    public ClothEntry(String name, Rarity rarity, int weight, int variantRange, ClothEntry.ClothDyeSettings dye) {
        this.clothName = name;
        this.clothRarity = rarity;
        this.clothWeight = Math.max(0, weight);
        this.variantRange = Math.max(0, variantRange);
        this.clothDyeSettings = dye;
    }

    public String getClothName() {
        return this.clothName;
    }

    public Rarity getClothRarity() {
        return this.clothRarity;
    }

    public int getClothWeight() {
        return this.clothWeight;
    }

    public int getClothVariantRange() { return this.variantRange; }

    public ClothEntry.ClothDyeSettings getClothDyeSettings() { return this.clothDyeSettings; }

    public void validateDeserializedEntry() {
        //name already checked
        if(this.clothRarity == null) this.clothRarity = Rarity.COMMON;
        this.clothWeight = Math.max(0, this.clothWeight);
        this.variantRange = Math.max(0, this.variantRange);
        if(this.clothDyeSettings == null) this.clothDyeSettings = DYE_NONE;
    }
    ///////////
    //Dye
    ///////////
    public static class ClothDyeSettings {
        @SerializedName("enabled")
        private boolean useDye;
        @SerializedName("decimal color")
        private int defaultColor;

        public ClothDyeSettings(boolean useDye, int defaultColor) {
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
