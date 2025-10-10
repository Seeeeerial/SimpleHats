package fonnymunkey.simplehats.util;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Rarity;

public class TailEntry {
    public static final TailEntry.TailDyeSettings DYE_NONE = new TailEntry.TailDyeSettings(false, 0);

    @SerializedName("name")
    private String tailName;
    @SerializedName("rarity")
    private Rarity tailRarity;
    @SerializedName("weight")
    private int tailWeight;
    @SerializedName("variants")
    private int variantRange;
    @SerializedName("Dye Settings")
    private TailEntry.TailDyeSettings tailDyeSettings;

    public TailEntry(String name) {
        this(name, Rarity.COMMON, 0, 0, DYE_NONE);
    }
    public TailEntry(String name, Rarity rarity, int weight) {
        this(name, rarity, weight, 0, DYE_NONE);
    }
    public TailEntry(String name, Rarity rarity, int weight, int variantRange) {
        this(name, rarity, weight, variantRange, DYE_NONE);
    }
    public TailEntry(String name, Rarity rarity, int weight, int variantRange, TailEntry.TailDyeSettings dye) {
        this.tailName = name;
        this.tailRarity = rarity;
        this.tailWeight = Math.max(0, weight);
        this.variantRange = Math.max(0, variantRange);
        this.tailDyeSettings = dye;
    }

    public String getTailName() {
        return this.tailName;
    }

    public Rarity getTailRarity() {
        return this.tailRarity;
    }

    public int getTailWeight() {
        return this.tailWeight;
    }

    public int getTailVariantRange() { return this.variantRange; }

    public TailEntry.TailDyeSettings getTailDyeSettings() { return this.tailDyeSettings; }

    public void validateDeserializedEntry() {
        //name already checked
        if(this.tailRarity == null) this.tailRarity = Rarity.COMMON;
        this.tailWeight = Math.max(0, this.tailWeight);
        this.variantRange = Math.max(0, this.variantRange);
        if(this.tailDyeSettings == null) this.tailDyeSettings = DYE_NONE;
    }
    ///////////
    //Dye
    ///////////
    public static class TailDyeSettings {
        @SerializedName("enabled")
        private boolean useDye;
        @SerializedName("decimal color")
        private int defaultColor;

        public TailDyeSettings(boolean useDye, int defaultColor) {
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