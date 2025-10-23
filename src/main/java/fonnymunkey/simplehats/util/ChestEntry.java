package fonnymunkey.simplehats.util;

import com.google.gson.annotations.SerializedName;
import fonnymunkey.simplehats.SimpleHats;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.apache.logging.log4j.Level;

public class ChestEntry {
    public static final ChestEntry.ChestDyeSettings DYE_NONE = new ChestEntry.ChestDyeSettings(false, 0);
    public static final ChestEntry.ChestParticleSettings PARTICLE_NONE = new ChestEntry.ChestParticleSettings(false, "minecraft:heart", 0, ChestEntry.ChestParticleSettings.ChestParticleMovement.TRAILING_FULL);

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
    @SerializedName("Particle Settings")
    private ChestEntry.ChestParticleSettings chestParticleSettings;

    public ChestEntry(String name) {
        this(name, Rarity.COMMON, 0, 0, DYE_NONE, PARTICLE_NONE);
    }
    public ChestEntry(String name, Rarity rarity, int weight) {
        this(name, rarity, weight, 0, DYE_NONE, PARTICLE_NONE);
    }
    public ChestEntry(String name, Rarity rarity, int weight, int variantRange) {
        this(name, rarity, weight, variantRange, DYE_NONE, PARTICLE_NONE);
    }
    public ChestEntry(String name, Rarity rarity, int weight, int variantRange, ChestEntry.ChestDyeSettings dye) {
        this(name, rarity, weight, variantRange, dye, PARTICLE_NONE);
    }
    public ChestEntry(String name, Rarity rarity, int weight, int variantRange, ChestEntry.ChestParticleSettings particle) {
        this(name, rarity, weight, variantRange, DYE_NONE, particle);
    }
    public ChestEntry(String name, Rarity rarity, int weight, int variantRange, ChestEntry.ChestDyeSettings dye, ChestEntry.ChestParticleSettings particle) {
        this.chestName = name;
        this.chestRarity = rarity;
        this.chestWeight = Math.max(0, weight);
        this.variantRange = Math.max(0, variantRange);
        this.chestDyeSettings = dye;
        this.chestParticleSettings = particle;
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

    public ChestEntry.ChestParticleSettings getChestParticleSettings() { return this.chestParticleSettings; }

    public void validateDeserializedEntry() {
        //name already checked
        if(this.chestRarity == null) this.chestRarity = Rarity.COMMON;
        this.chestWeight = Math.max(0, this.chestWeight);
        this.variantRange = Math.max(0, this.variantRange);
        if(this.chestDyeSettings == null) this.chestDyeSettings = DYE_NONE;
        if(this.chestParticleSettings == null) this.chestParticleSettings = PARTICLE_NONE;
        else this.chestParticleSettings.validateParticleSettings();
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
    public static class ChestParticleSettings {
        @SerializedName("enabled")
        private boolean useParticle;
        @SerializedName("name")
        private String particleTypeString;
        @SerializedName("frequency")
        private float particleFrequency;
        @SerializedName("movement")
        private ChestEntry.ChestParticleSettings.ChestParticleMovement particleMovement;

        private transient DefaultParticleType particleTypeParsed = ParticleTypes.HEART;

        public ChestParticleSettings(boolean useParticle, String particleTypeString, float particleFrequency, ChestEntry.ChestParticleSettings.ChestParticleMovement particleMovement) {
            this.useParticle = useParticle;
            this.particleTypeString = particleTypeString;
            this.particleFrequency = particleFrequency;
            this.particleMovement = particleMovement;
            this.parseParticleString();
        }

        public boolean getUseParticles() { return this.useParticle; }

        public DefaultParticleType getParticleType() { return this.particleTypeParsed; }

        public float getParticleFrequency() { return this.particleFrequency; }

        public ChestEntry.ChestParticleSettings.ChestParticleMovement getParticleMovement() { return this.particleMovement; }

        public void validateParticleSettings() {
            //use defaults false
            if(this.particleTypeString.isEmpty()) this.particleTypeString = "minecraft:heart";
            if(this.particleMovement == null) this.particleMovement = ChestEntry.ChestParticleSettings.ChestParticleMovement.TRAILING_FULL;
            this.parseParticleString();
        }

        private void parseParticleString() {
            this.particleTypeParsed = (DefaultParticleType) Registries.PARTICLE_TYPE.get(new Identifier(this.particleTypeString));
            if(this.particleTypeParsed == null) {
                SimpleHats.logger.log(Level.ERROR, "Particle type \"" + this.particleTypeString + "\" failed to parse, setting default.");
                this.particleTypeParsed = ParticleTypes.HEART;
            }
        }

        public enum ChestParticleMovement {
            TRAILING_CHEST,
            TRAILING_FEET,
            TRAILING_FULL;
        }
    }
}