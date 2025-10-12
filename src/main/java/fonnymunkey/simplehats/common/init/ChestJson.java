package fonnymunkey.simplehats.common.init;

import com.google.common.io.Files;
import com.google.gson.*;
import fonnymunkey.simplehats.SimpleHats;
import fonnymunkey.simplehats.util.ChestEntry;
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

public class ChestJson {
    private static List<ChestEntry> chestList = new ArrayList<>();
    private static final List<ChestEntry> defaultChests = Arrays.asList(
            new ChestEntry("fallen_angel_wings", Rarity.RARE, 5, 0),
            new ChestEntry("wind_wings", Rarity.RARE, 5, 0),
            new ChestEntry("fire_wings", Rarity.RARE, 5, 0),
            new ChestEntry("wind_wings_dyeable", Rarity.RARE, 5, 0, new ChestEntry.ChestDyeSettings(true, 16383998)),
            new ChestEntry("fire_wings_dyeable", Rarity.RARE, 5, 0, new ChestEntry.ChestDyeSettings(true, 16383998)),
            new ChestEntry("small_angel_wing", Rarity.RARE, 5, 0, new ChestEntry.ChestDyeSettings(true, 16383998)),
            new ChestEntry("small_devil_wing", Rarity.RARE, 5, 0, new ChestEntry.ChestDyeSettings(true, 3355443)),
            new ChestEntry("blank_top", Rarity.RARE, 0, 0)
            //new ChestEntry("blank_top", Rarity.RARE, 5, 0, new ChestEntry.ChestDyeSettings(true, 16383998)),

    );
    public static List<ChestEntry> getChestList() {
        return chestList;
    }

    public static void registerChestJson() {
        try {
            File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), SimpleHats.modId + "chest.json");

            if(!file.exists()) {
                SimpleHats.logger.log(Level.INFO, "SimpleHats simpleHatschest.json not found, generating default file.");

                file.createNewFile();
                file.setWritable(true);

                JsonObject dataJson = new JsonObject();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                for(ChestEntry entry : defaultChests) {
                    JsonElement element = gson.toJsonTree(entry);
                    dataJson.add(entry.getChestName(), element);
                }

                String dataString = gson.toJson(dataJson);
                PrintWriter writer = new PrintWriter(file);
                writer.write(dataString);
                writer.flush();
                writer.close();

                chestList = defaultChests;
                SimpleHats.logger.log(Level.INFO, "Loaded " + chestList.size() + " chest entries from default file.");
            }
            else {
                file.setWritable(true);
                String fileString = Files.asCharSource(file, Charset.defaultCharset()).read();
                JsonObject json = JsonParser.parseString(fileString).getAsJsonObject();

                Gson gson = new Gson();

                outer:
                for(Map.Entry<String, JsonElement> entry : json.entrySet()){
                    JsonElement dataElement = entry.getValue();
                    ChestEntry chestEntry = gson.fromJson(dataElement, ChestEntry.class);

                    if(chestEntry.getChestName().isEmpty()) {
                        SimpleHats.logger.log(Level.WARN, "Attempted to load empty chest name, skipping.");
                        continue;
                    }
                    if(!validateName(chestEntry.getChestName())) {
                        SimpleHats.logger.log(Level.WARN, "Attempted to load invalid chest name \"" + chestEntry.getChestName() + "\", skipping.");
                        continue;
                    }
                    for(ChestEntry temp : chestList) {
                        if(temp.getChestName().equalsIgnoreCase(chestEntry.getChestName()) || chestEntry.getChestName().equalsIgnoreCase("special")) {
                            SimpleHats.logger.log(Level.WARN, "Attempted to load duplicate chest name \"" + chestEntry.getChestName() + "\", skipping.");
                            continue outer;
                        }
                    }
                    //validate entries from json and set defaults
                    chestEntry.validateDeserializedEntry();
                    chestList.add(chestEntry);
                }
                SimpleHats.logger.log(Level.INFO, "Loaded " + chestList.size() + " chest entries from simplehats.json");
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
