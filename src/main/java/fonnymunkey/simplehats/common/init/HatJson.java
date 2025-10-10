package fonnymunkey.simplehats.common.init;

import com.google.common.io.Files;
import com.google.gson.*;
import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.util.HatEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HatJson {
    private static List<HatEntry> hatList = new ArrayList<>();

    private static final List<HatEntry> defaultHats = Arrays.asList(
            new HatEntry("babyturtle", Rarity.RARE, 5, 0, new HatEntry.HatParticleSettings(true, "minecraft:splash", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
            new HatEntry("batwinghat", Rarity.RARE, 5, 0),
            new HatEntry("bigribbon", Rarity.RARE, 5, 1, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("bluefireeye", Rarity.RARE, 5, 0, new HatEntry.HatParticleSettings(true, "minecraft:soul_fire_flame", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FEET)),
            new HatEntry("carrotonstick", Rarity.RARE, 5, 0),
            new HatEntry("cartoonegg", Rarity.RARE, 5, 0),
            new HatEntry("christmastree", Rarity.RARE, 5, 0, new HatEntry.HatParticleSettings(true, "minecraft:snowflake", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
            new HatEntry("crabonhead", Rarity.RARE, 5, 0),
            new HatEntry("finnhood", Rarity.RARE, 5, 0),
            new HatEntry("fireworks", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998), new HatEntry.HatParticleSettings(true, "minecraft:firework", 0.02F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
            new HatEntry("floatinghearts", Rarity.RARE, 5, 0, new HatEntry.HatParticleSettings(true, "minecraft:heart", 0.02F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
            new HatEntry("flowercrown", Rarity.RARE, 5, 0, new HatEntry.HatParticleSettings(true, "minecraft:spore_blossom_air", 0.02F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
            new HatEntry("foxhat", Rarity.RARE, 5, 1, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("headshot", Rarity.RARE, 5, 0),
            new HatEntry("horsemask", Rarity.RARE, 5, 0),
            new HatEntry("lilbow", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("raincloud", Rarity.RARE, 5, 0),
            new HatEntry("snowmanbaby", Rarity.RARE, 5, 0, new HatEntry.HatParticleSettings(true, "minecraft:snowflake", 0.04F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
            new HatEntry("stress", Rarity.RARE, 5, 0, new HatEntry.HatParticleSettings(true, "minecraft:warped_spore", 0.02F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
            new HatEntry("sunglasses", Rarity.RARE, 5, 0),
            new HatEntry("winghat", Rarity.RARE, 5, 0, new HatEntry.HatParticleSettings(true, "minecraft:cloud", 0.03F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
            new HatEntry("zigzagwitchhat", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998), new HatEntry.HatParticleSettings(true, "minecraft:witch", 0.04F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
            new HatEntry("smallroundmimi", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("bluebellhat", Rarity.RARE, 5, 0),
            new HatEntry("fallenleaf", Rarity.RARE, 5, 0),
            new HatEntry("kirby1", Rarity.RARE, 5, 0),
            new HatEntry("pumpkinberet", Rarity.RARE, 5, 0),
            new HatEntry("bearberet1", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("rabbitberet1", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("strawhat", Rarity.RARE, 5, 0),
            new HatEntry("daisyflowerstrawhat", Rarity.RARE, 5, 0),
            new HatEntry("sunflowerstrawhat", Rarity.RARE, 5, 0),
            new HatEntry("tulipflowerstrawhat", Rarity.RARE, 5, 0),
            new HatEntry("cornflowerstrawhat", Rarity.RARE, 5, 0),
            new HatEntry("cat1_black", Rarity.RARE, 5, 0),
            new HatEntry("cat1_britishshorthair", Rarity.RARE, 5, 0),
            new HatEntry("cat1_calico", Rarity.RARE, 5, 0),
            new HatEntry("cat1_jellie", Rarity.RARE, 5, 0),
            new HatEntry("cat1_ocelot", Rarity.RARE, 5, 0),
            new HatEntry("cat1_ragdoll", Rarity.RARE, 5, 0),
            new HatEntry("cat1_red", Rarity.RARE, 5, 0),
            new HatEntry("cat1_siamese", Rarity.RARE, 5, 0),
            new HatEntry("cat1_tabby", Rarity.RARE, 5, 0),
            new HatEntry("cat1_white", Rarity.RARE, 5, 0),
            new HatEntry("cat1_allblack", Rarity.RARE, 5, 0),
            new HatEntry("breadcap", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("beret", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("beret2", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("mushberet1", Rarity.RARE, 5, 0),
            new HatEntry("mushberet2", Rarity.RARE, 5, 0),
            new HatEntry("blankhat", Rarity.RARE, 5, 0),
            new HatEntry("bearears", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("catears", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("rabbit_ear", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("tiger_ears", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("wolf_ear", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
            new HatEntry("angel_ring", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16777160)),
            new HatEntry("pinkhat", Rarity.RARE, 5, 0, new HatEntry.HatDyeSettings(true, 16383998))

//            new HatEntry("bandana", Rarity.UNCOMMON, 5, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("bandanargb", Rarity.RARE, 5, 0),
//            new HatEntry("baseballeaster", Rarity.UNCOMMON, 5, 0, HatEntry.HatSeason.EASTER),
//            new HatEntry("baseballhat", Rarity.UNCOMMON, 0, 1, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("baseballhatfestive", Rarity.UNCOMMON, 5, 0, HatEntry.HatSeason.FESTIVE),
//            new HatEntry("baseballhatjuly", Rarity.UNCOMMON, 5, 0, HatEntry.HatSeason.SUMMER),
//            new HatEntry("baseballhatrgb", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("beanie", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("beanieeaster", Rarity.UNCOMMON, 5, 0, HatEntry.HatSeason.EASTER),
//            new HatEntry("beaniefestive", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.FESTIVE),
//            new HatEntry("beaniejuly", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.SUMMER),
//            new HatEntry("beaniergb", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("beaniespooky", Rarity.UNCOMMON, 5, 0, HatEntry.HatSeason.HALLOWEEN),
//            new HatEntry("beehat", Rarity.COMMON, 5, 2, new HatEntry.HatParticleSettings(true, "minecraft:falling_honey", 0.02F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("bicorne", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("bigbrain", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("bigcrown", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("bigeyes", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("bigstevehead", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("bowler", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("breadhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("brownbrick", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("bunnyhat", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.EASTER),
//            new HatEntry("burgerhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("caddycap", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("camera", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:flash", 0.002F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("camerabeard", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:flash", 0.002F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("candleonhead", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:small_flame", 0.03F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("candycane", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.FESTIVE),
//            new HatEntry("cheeseslice", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("chefshat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("chickenhead", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("chickenonhead", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("christmascakehat", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.FESTIVE),
//            new HatEntry("clockface", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("cowboy", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("cowboyrgb", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("crown", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("cuphead", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("cyclopseye", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("dairyqueen", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("dangereqsue", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("dangeresquejuly", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.SUMMER),
//            new HatEntry("demoneyes", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:crimson_spore", 0.05F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("demonhorns", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:lava", 0.03F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FEET)),
//            new HatEntry("digger", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("dimmahat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("discoball", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("disguise", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("doctorhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("dorkglassesandteeth", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("doubletake", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("dragonhead", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998), new HatEntry.HatParticleSettings(true, "minecraft:small_flame", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FEET)),
//            new HatEntry("dragonskull", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:ash", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("dragonskullender", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:dragon_breath", 0.05F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("drinkinhat", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("dumhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("dwarfminerbeard", Rarity.UNCOMMON, 0, 2),
//            new HatEntry("easterhead", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("egghead", Rarity.UNCOMMON, 0, 1, new HatEntry.HatDyeSettings(true, 16383998), HatEntry.HatSeason.EASTER),
//            new HatEntry("eggonhead", Rarity.UNCOMMON, 0, 1, HatEntry.HatSeason.EASTER),
//            new HatEntry("elfhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("explorerhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("eyepatch", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("fakeblight", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:entity_effect", 0.05F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("fakefire", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:flame", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("farmerbrim", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 7895160), HatEntry.HatSeason.EASTER),
//            new HatEntry("festiveantlers", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.FESTIVE),
//            new HatEntry("festiveribbon", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998), HatEntry.HatSeason.FESTIVE),
//            new HatEntry("fishonhead", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:splash", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("flagjuly", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.SUMMER),
//            new HatEntry("flies", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("floatingstar", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:electric_spark", 0.03F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("floweronhead", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:spore_blossom_air", 0.02F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("fro", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("frozenhead", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:snowflake", 0.05F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("fullironhelm", Rarity.UNCOMMON, 0, 3),
//            new HatEntry("ghostmask", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:soul", 0.05F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD), HatEntry.HatSeason.HALLOWEEN),
//            new HatEntry("goggles", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("grandmadisguise", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("greenbirb", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("grinchhat", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.FESTIVE),
//            new HatEntry("halo", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("headbolts", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:electric_spark", 0.03F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD), HatEntry.HatSeason.HALLOWEEN),
//            new HatEntry("headphonesblue", Rarity.UNCOMMON, 0, 3, new HatEntry.HatParticleSettings(true, "minecraft:note", 0.01F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("hockeymask", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.HALLOWEEN),
//            new HatEntry("holyhead", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:glow", 0.02F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("hosthat", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:crimson_spore", 0.02F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("icedragonskull", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:snowflake", 0.04F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("jackohat", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.HALLOWEEN),
//            new HatEntry("jesterhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("julydouble", Rarity.UNCOMMON, 0, 2, HatEntry.HatSeason.SUMMER),
//            new HatEntry("kirbymouthful", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("largehorns", Rarity.UNCOMMON, 0, 1),
//            new HatEntry("madscientist", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("magikarp", Rarity.UNCOMMON, 0, 1),
//            new HatEntry("megamanhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("mistletoe", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.FESTIVE),
//            new HatEntry("mohawk", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("monkeyking", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("monocle", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("moreeyes", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("murdered", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.HALLOWEEN),
//            new HatEntry("nekoears", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("palmtree", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.SUMMER),
//            new HatEntry("paperbag", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("partyhat", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("paypay", Rarity.UNCOMMON, 0, 1),
//            new HatEntry("penguinbaby", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("penguinhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("pighead", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("pinhead", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.HALLOWEEN),
//            new HatEntry("plaguedoctor", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998), HatEntry.HatSeason.HALLOWEEN),
//            new HatEntry("pog", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("pohatoe", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("policebucket", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("policesiren", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("poofballhat", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("poofballrgb", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("popehat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("potionhead", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:entity_effect", 0.05F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("presentsstack", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.FESTIVE),
//            new HatEntry("propelhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("questbook", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rabbitears", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.EASTER),
//            new HatEntry("rabbitonhead", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rainboworbiters", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("ranahat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("redeyes", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rednose", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.FESTIVE),
//            new HatEntry("redstache", Rarity.UNCOMMON, 0, 1),
//            new HatEntry("rgbbigribbon", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rgbbowler", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rgbdragonskull", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rgbdrinkinhat", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("rgbeasterhead", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rgbfullhelm", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rgbpartyhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rgbsmallbowler", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rgbsunglasses", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rgbtoptophathat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rgbushanka", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rock", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rubbernipple", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("sandcastle", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.SUMMER),
//            new HatEntry("santaclaus", Rarity.UNCOMMON, 0, 1, HatEntry.HatSeason.FESTIVE),
//            new HatEntry("sausage", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("seaweedhat", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.SUMMER),
//            new HatEntry("shakehat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("sheep", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("shroomcap", Rarity.UNCOMMON, 0, 1, new HatEntry.HatParticleSettings(true, "minecraft:mycelium", 0.08F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("simsgem", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("smokingpipe", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:smoke", 0.02F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("sombrero", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("sonichood", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:electric_spark", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FEET)),
//            new HatEntry("spadesoldier", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("spiderweb", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.HALLOWEEN),
//            new HatEntry("springer", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("sprout", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("spyzombie", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("stackofeggs", Rarity.UNCOMMON, 0, 0, HatEntry.HatSeason.EASTER),
//            new HatEntry("summerhat", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 11330558)),
//            new HatEntry("sunglassesbig", Rarity.UNCOMMON, 0, 1),
//            new HatEntry("supersandhat", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:electric_spark", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("swimmer", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("tinkerhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("topcathat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("tophat", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("toptophathat", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16383998)),
//            new HatEntry("triangleshades", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("tricorne", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("tvhead", Rarity.UNCOMMON, 0, 2),
//            new HatEntry("unicornhorn", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:glow", 0.02F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("ushanka", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("vikinghatbeard", Rarity.UNCOMMON, 0, 2),
//            new HatEntry("villagernose", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("acornhat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("aegishat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("alienphil", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("amalgalichhat", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:portal", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("angrymask", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("antlers", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("apple", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("artsy", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("babydolphin", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:dolphin", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//
//            new HatEntry("artsy_doll", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:glow", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("azumanga_hat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("beret_ribbon", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("bucket", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:falling_water", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("burning_m_bison", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("chalk_stick", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("chi_ears", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("circular_glasses", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 12763584)),
//            new HatEntry("cucumbereyemask", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("dejiko", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("fez", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("fishing_hat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("lightning_eyes", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16777215)),
//            new HatEntry("longfoxears", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 12763584)),
//            new HatEntry("milady_doll", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("nyan_doll", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:entity_effect", 0.2F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("orange_hat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("peppino", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16777215)),
//            new HatEntry("pom_moog", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("puchiko", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("rabi_en_rose", Rarity.UNCOMMON, 0, 1),
//            new HatEntry("scouter", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 12763584)),
//            new HatEntry("sleepeyemask", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:heart", 0.01F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("sport_sunglasses", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("strawberry_hat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("teddy_bear", Rarity.UNCOMMON, 0, 6),
//            new HatEntry("the_noise", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("toy_story_alien", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("twilight_doll", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:enchant", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("worms_mine", Rarity.UNCOMMON, 0, 0),
//
//            new HatEntry("alien_antennae", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 5308240)),
//            new HatEntry("angel_and_devil", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("astronaut", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("axolotl_on_head", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:dolphin", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("baby_crewmate", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16719360)),
//            new HatEntry("bee_on_head", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:falling_honey", 0.02F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("beetle_on_head", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("binky", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 36095)),
//            new HatEntry("cardboard_box", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("cat_hat", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16752640)),
//            new HatEntry("cat_on_head", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 16743680)),
//            new HatEntry("caterpillar_on_head", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("chocolate_sauced", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("crystal_horns", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:glow", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("dipper", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("druid_antlers", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 5299200)),
//            new HatEntry("druid_antlers_rare", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 5299200), new HatEntry.HatParticleSettings(true, "minecraft:spore_blossom_air", 0.05F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("eevee_ears", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("eyeholder_beeholder", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:falling_honey", 0.04F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("eyeholder_dark", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:soul_fire_flame", 0.05F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("eyeholder_evil", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:portal", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("eyeholder_warm", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:lava", 0.03F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("eyeholder_xanath", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:enchant", 0.2F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("gnome", Rarity.UNCOMMON, 0, 0, new HatEntry.HatDyeSettings(true, 65280)),
//            new HatEntry("gnome_clover_wig", Rarity.UNCOMMON, 0, 1),
//            new HatEntry("greaser", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("hat_of_discipline", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("ladybug_on_head", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("lil_bows", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("lil_termagant", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("medusa", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("mimic_head", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("mimic_head_dark", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:soul", 0.05F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("mimic_head_gold", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:electric_spark", 0.05F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("mindflayer", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:glow", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("mindflayer_alhoon", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:enchant", 0.2F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("octodad", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:splash", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("pika_ears", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:electric_spark", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_FULL)),
//            new HatEntry("right_hand_hat", Rarity.UNCOMMON, 0, 1),
//            new HatEntry("round_purple_wig", Rarity.UNCOMMON, 0, 1),
//            new HatEntry("round_red_wig", Rarity.UNCOMMON, 0, 1),
//            new HatEntry("slime_cube_dnd", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:soul", 0.05F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("slime_head", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:spore_blossom_air", 0.04F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("stinkycheeseman", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:mycelium", 0.12F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("stuck_lollipop", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("tanuki_leaf", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("the_noisier", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("thumbnail", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("tick_on_head", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("toast", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("toilet", Rarity.UNCOMMON, 0, 0, new HatEntry.HatParticleSettings(true, "minecraft:splash", 0.1F, HatEntry.HatParticleSettings.HatParticleMovement.TRAILING_HEAD)),
//            new HatEntry("tomato_splats", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("traffic_cone", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("udder_hat", Rarity.UNCOMMON, 0, 0),
//            new HatEntry("worm_hat", Rarity.UNCOMMON, 0, 0)
            );
    public static List<HatEntry> getHatList() {
        return hatList;
    }

    public static void registerHatJson() {
        try {
            File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), SimpleHats.modId + ".json");

            if(!file.exists()) {
                SimpleHats.logger.log(Level.INFO, "SimpleHats simplehats.json not found, generating default file.");

                file.createNewFile();
                file.setWritable(true);

                JsonObject dataJson = new JsonObject();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                for(HatEntry entry : defaultHats) {
                    JsonElement element = gson.toJsonTree(entry);
                    dataJson.add(entry.getHatName(), element);
                }

                String dataString = gson.toJson(dataJson);
                PrintWriter writer = new PrintWriter(file);
                writer.write(dataString);
                writer.flush();
                writer.close();

                hatList = defaultHats;
                SimpleHats.logger.log(Level.INFO, "Loaded " + hatList.size() + " hat entries from default file.");
            }
            else {
                file.setWritable(true);
                String fileString = Files.asCharSource(file, Charset.defaultCharset()).read();
                JsonObject json = JsonParser.parseString(fileString).getAsJsonObject();

                Gson gson = new Gson();

                outer:
                for(Map.Entry<String, JsonElement> entry : json.entrySet()){
                    JsonElement dataElement = entry.getValue();
                    HatEntry hatEntry = gson.fromJson(dataElement, HatEntry.class);

                    if(hatEntry.getHatName().isEmpty()) {
                        SimpleHats.logger.log(Level.WARN, "Attempted to load empty hat name, skipping.");
                        continue;
                    }
                    if(!validateName(hatEntry.getHatName())) {
                        SimpleHats.logger.log(Level.WARN, "Attempted to load invalid hat name \"" + hatEntry.getHatName() + "\", skipping.");
                        continue;
                    }
                    for(HatEntry temp : hatList) {
                        if(temp.getHatName().equalsIgnoreCase(hatEntry.getHatName()) || hatEntry.getHatName().equalsIgnoreCase("special")) {
                            SimpleHats.logger.log(Level.WARN, "Attempted to load duplicate hat name \"" + hatEntry.getHatName() + "\", skipping.");
                            continue outer;
                        }
                    }
                    //validate entries from json and set defaults
                    hatEntry.validateDeserializedEntry();
                    hatList.add(hatEntry);
                }
                SimpleHats.logger.log(Level.INFO, "Loaded " + hatList.size() + " hat entries from simplehats.json");
            }
        }
        catch(Exception ex) {
            SimpleHats.logger.log(Level.ERROR, "Loading simplehats.json failed: " + ex);
        }
    }

    private static boolean validateName(String name) {
        for(char c : name.toCharArray()) {
            if(!Identifier.isCharValid(c)) return false;
        }
        return true;
    }
}
