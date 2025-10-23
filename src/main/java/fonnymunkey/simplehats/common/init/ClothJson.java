package fonnymunkey.simplehats.common.init;

import com.google.common.io.Files;
import com.google.gson.*;
import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.util.ChestEntry;
import fonnymunkey.simplehats.util.ClothEntry;
import fonnymunkey.simplehats.util.TailEntry;
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

public class ClothJson {
    private static List<ClothEntry> clothList = new ArrayList<>();
    private static final List<ClothEntry> defaultCloths = Arrays.asList(
            new ClothEntry("apron_grey", Rarity.RARE, 5, 0),
            new ClothEntry("apron_red", Rarity.RARE, 5, 0),
            new ClothEntry("apron_blue", Rarity.RARE, 5, 0),
            new ClothEntry("apron_mint", Rarity.RARE, 5, 0),
            new ClothEntry("apron_green", Rarity.RARE, 5, 0),
            new ClothEntry("apron_brown", Rarity.RARE, 5, 0),
            new ClothEntry("apron_yellow", Rarity.RARE, 5, 0),
            new ClothEntry("coat_color", Rarity.RARE, 5, 0),
            new ClothEntry("coat_dye", Rarity.RARE, 5, 0, new ClothEntry.ClothDyeSettings(true, 16383998)),
            new ClothEntry("diamond_chest", Rarity.RARE, 5, 0, new ClothEntry.ClothDyeSettings(true, 16383998)),
            new ClothEntry("gold_chest", Rarity.RARE, 5, 0),
            new ClothEntry("leather_chest", Rarity.RARE, 5, 0, new ClothEntry.ClothDyeSettings(true, 16383998)),
            new ClothEntry("netherite_chest", Rarity.RARE, 5, 0),
            new ClothEntry("iron_chest", Rarity.RARE, 5, 0),
            new ClothEntry("muffler_dye", Rarity.RARE, 5, 0, new ClothEntry.ClothDyeSettings(true, 16383998)),
            new ClothEntry("muffler2", Rarity.RARE, 5, 0, new ClothEntry.ClothDyeSettings(true, 16383998)),
            new ClothEntry("muffler_color", Rarity.RARE, 5, 0),
            new ClothEntry("cow", Rarity.RARE, 5, 0),
            new ClothEntry("blank_top", Rarity.RARE, 0, 0)
    );
    public static List<ClothEntry> getClothList() {
        return clothList;
    }

    public static void registerClothJson() {
        try {
            File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), SimpleHats.modId + "cloth.json");

            if(!file.exists()) {
                SimpleHats.logger.log(Level.INFO, "SimpleHats simpleHatscloth.json not found, generating default file.");

                file.createNewFile();
                file.setWritable(true);

                JsonObject dataJson = new JsonObject();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                for(ClothEntry entry : defaultCloths) {
                    JsonElement element = gson.toJsonTree(entry);
                    dataJson.add(entry.getClothName(), element);
                }

                String dataString = gson.toJson(dataJson);
                PrintWriter writer = new PrintWriter(file);
                writer.write(dataString);
                writer.flush();
                writer.close();

                clothList = defaultCloths;
                SimpleHats.logger.log(Level.INFO, "Loaded " + clothList.size() + " cloth entries from default file.");
            }
            else {
                file.setWritable(true);
                String fileString = Files.asCharSource(file, Charset.defaultCharset()).read();
                JsonObject json = JsonParser.parseString(fileString).getAsJsonObject();

                Gson gson = new Gson();

                outer:
                for(Map.Entry<String, JsonElement> entry : json.entrySet()){
                    JsonElement dataElement = entry.getValue();
                    ClothEntry clothEntry = gson.fromJson(dataElement, ClothEntry.class);

                    if(clothEntry.getClothName().isEmpty()) {
                        SimpleHats.logger.log(Level.WARN, "Attempted to load empty cloth name, skipping.");
                        continue;
                    }
                    if(!validateName(clothEntry.getClothName())) {
                        SimpleHats.logger.log(Level.WARN, "Attempted to load invalid cloth name \"" + clothEntry.getClothName() + "\", skipping.");
                        continue;
                    }
                    for(ClothEntry temp : clothList) {
                        if(temp.getClothName().equalsIgnoreCase(clothEntry.getClothName()) || clothEntry.getClothName().equalsIgnoreCase("special")) {
                            SimpleHats.logger.log(Level.WARN, "Attempted to load duplicate cloth name \"" + clothEntry.getClothName() + "\", skipping.");
                            continue outer;
                        }
                    }
                    //validate entries from json and set defaults
                    clothEntry.validateDeserializedEntry();
                    clothList.add(clothEntry);
                }
                SimpleHats.logger.log(Level.INFO, "Loaded " + clothList.size() + " cloth entries from simplehats.json");
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
