package net.nocpiun.configx.api;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.nocpiun.configx.ConfigX;
import org.jetbrains.annotations.Nullable;
import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class ConfigManager {
    private static ConfigManager instance;

    private final HashSet<Configuration> configurations = new HashSet<>();

    private ConfigManager() {
        ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
            configurations.forEach(Configuration::save);
            ConfigX.LOGGER.info("All configurations are saved.");
        });
    }

    public Configuration getOrCreateConfig(String name, @Nullable HashMap<String, Object> defaultConfig) {
        File file = new File(ConfigX.CONFIG_PATH.toString(), name +".json");
        HashMap<String, Object> loaded;
        if(file.exists()) {
            loaded = loadFile(file);

            if(defaultConfig != null && loaded != null) {
                defaultConfig.forEach((key, value) -> {
                    if(!loaded.containsKey(key)) {
                        loaded.put(key, value);
                    }
                });
            }
        } else {
            loaded = (defaultConfig == null) ? new HashMap<>() : defaultConfig;
        }
        saveFile(file, loaded);

        Configuration config = new Configuration(name, file, loaded);
        configurations.add(config);

        return config;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> loadFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);

            Gson gson = new Gson();
            HashMap<String, Object> result = (HashMap<String, Object>) gson.fromJson(new String(fis.readAllBytes()), HashMap.class);
            fis.close();

            ConfigX.LOGGER.info(file.getName() +" is successfully loaded.");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** @noinspection ResultOfMethodCallIgnored*/
    protected void saveFile(File file, HashMap<String, Object> config) {
        try(FileOutputStream fos = new FileOutputStream(file)) {
            file.createNewFile();

            Gson gson = new Gson();
            fos.write(gson.toJson(config).getBytes());
            fos.close();

            ConfigX.LOGGER.info(file.getName() +" is successfully saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigManager get() {
        if(instance == null) instance = new ConfigManager();
        return instance;
    }
}
