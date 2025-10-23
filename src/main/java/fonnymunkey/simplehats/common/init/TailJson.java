package fonnymunkey.simplehats.common.init;

import com.google.common.io.Files;
import com.google.gson.*;
import fonnymunkey.simplehats.SimpleHats;
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

public class TailJson {
    private static List<TailEntry> tailList = new ArrayList<>();
    private static final List<TailEntry> defaultTails = Arrays.asList(
        new TailEntry("bear_tail", Rarity.RARE, 5, 0, new TailEntry.TailDyeSettings(true, 16383998)),
        new TailEntry("cat_tail", Rarity.RARE, 5, 0, new TailEntry.TailDyeSettings(true, 16383998)),
        new TailEntry("fox_tail", Rarity.RARE, 5, 0, new TailEntry.TailDyeSettings(true, 16383998)),
        new TailEntry("rabbit_tail", Rarity.RARE, 5, 0, new TailEntry.TailDyeSettings(true, 16383998)),
        new TailEntry("raccoon_tail", Rarity.RARE, 5, 0, new TailEntry.TailDyeSettings(true, 16383998)),
        new TailEntry("tiger_tail", Rarity.RARE, 5, 0, new TailEntry.TailDyeSettings(true, 14974780)),
        new TailEntry("wolf_tail", Rarity.RARE, 5, 0, new TailEntry.TailDyeSettings(true, 16383998)),
        new TailEntry("tail_demonblack", Rarity.RARE, 5, 0, new TailEntry.TailDyeSettings(true, 16383998)),
        new TailEntry("blank_pants", Rarity.RARE, 0, 0)
    );
    public static List<TailEntry> getTailList() {
        return tailList;
    }

    public static void registerTailJson() {
        try {
            File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), SimpleHats.modId + "tail.json");

            if(!file.exists()) {
                SimpleHats.logger.log(Level.INFO, "SimpleHats simpleHatstail.json not found, generating default file.");

                file.createNewFile();
                file.setWritable(true);

                JsonObject dataJson = new JsonObject();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                for(TailEntry entry : defaultTails) {
                    JsonElement element = gson.toJsonTree(entry);
                    dataJson.add(entry.getTailName(), element);
                }

                String dataString = gson.toJson(dataJson);
                PrintWriter writer = new PrintWriter(file);
                writer.write(dataString);
                writer.flush();
                writer.close();

                tailList = defaultTails;
                SimpleHats.logger.log(Level.INFO, "Loaded " + tailList.size() + " tail entries from default file.");
            }
            else {
                file.setWritable(true);
                String fileString = Files.asCharSource(file, Charset.defaultCharset()).read();
                JsonObject json = JsonParser.parseString(fileString).getAsJsonObject();

                Gson gson = new Gson();

                outer:
                for(Map.Entry<String, JsonElement> entry : json.entrySet()){
                    JsonElement dataElement = entry.getValue();
                    TailEntry tailEntry = gson.fromJson(dataElement, TailEntry.class);

                    if(tailEntry.getTailName().isEmpty()) {
                        SimpleHats.logger.log(Level.WARN, "Attempted to load empty tail name, skipping.");
                        continue;
                    }
                    if(!validateName(tailEntry.getTailName())) {
                        SimpleHats.logger.log(Level.WARN, "Attempted to load invalid tail name \"" + tailEntry.getTailName() + "\", skipping.");
                        continue;
                    }
                    for(TailEntry temp : tailList) {
                        if(temp.getTailName().equalsIgnoreCase(tailEntry.getTailName()) || tailEntry.getTailName().equalsIgnoreCase("special")) {
                            SimpleHats.logger.log(Level.WARN, "Attempted to load duplicate tail name \"" + tailEntry.getTailName() + "\", skipping.");
                            continue outer;
                        }
                    }
                    //validate entries from json and set defaults
                    tailEntry.validateDeserializedEntry();
                    tailList.add(tailEntry);
                }
                SimpleHats.logger.log(Level.INFO, "Loaded " + tailList.size() + " tail entries from simplehats.json");
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
