package space.nocp.configx.legacy_api;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.jetbrains.annotations.NotNull;
import space.nocp.configx.ConfigX;
import com.google.gson.Gson;

import java.io.*;
import java.util.HashSet;

@Deprecated
public class ConfigManager {
    private static ConfigManager instance;

    private final HashSet<Configuration<Object>> configurations = new HashSet<>();

    private ConfigManager() {
        ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
            configurations.forEach(Configuration::save);
            ConfigX.LOGGER.info("All configurations are saved.");
        });

        ConfigX.LOGGER.info("ConfigX is ready. Config dir path: "+ ConfigX.CONFIG_PATH);
    }

    public <T> Configuration<T> getOrCreateConfig(String name, @NotNull T defaultConfig, @NotNull Class<T> type) {
        return this.getOrCreateConfig(name, defaultConfig, type, new Gson());
    }

    @SuppressWarnings("unchecked")
    public <T> Configuration<T> getOrCreateConfig(String name, @NotNull T defaultConfig, @NotNull Class<T> type, Gson providedGson) {
        File file = new File(ConfigX.CONFIG_PATH.toString(), name +".json");
        T loaded;
        if(file.exists()) {
            loaded = loadFile(file, type, providedGson);
        } else {
            loaded = defaultConfig;
        }
        saveFile(file, loaded, providedGson);

        Configuration<T> config = new Configuration<>(name, file, loaded);
        configurations.add((Configuration<Object>) config);

        return config;
    }

    private <T> T loadFile(File file, Class<T> type, Gson gson) {
        try {
            FileInputStream fis = new FileInputStream(file);

            T result = gson.fromJson(new String(fis.readAllBytes()), type);
            fis.close();

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** @noinspection ResultOfMethodCallIgnored*/
    protected <T> void saveFile(File file, T config, Gson gson) {
        try(FileOutputStream fos = new FileOutputStream(file)) {
            file.createNewFile();

            fos.write(gson.toJson(config).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigManager get() {
        if(instance == null) instance = new ConfigManager();
        return instance;
    }
}
