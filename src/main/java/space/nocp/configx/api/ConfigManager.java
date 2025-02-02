package space.nocp.configx.api;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.nocp.configx.ConfigX;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static ConfigManager instance;

    private final List<Configuration> configList = new ArrayList<>();

    private ConfigManager() {
        ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
            // Auto save when server stopping
            configList.forEach(Configuration::save);
            ConfigX.LOGGER.info("[ConfigX] All configurations are saved.");
        });

        ConfigX.LOGGER.info("[ConfigX] Ready. Config dir path: "+ ConfigX.CONFIG_PATH);
    }

    public <C> Configuration<C> register(@NotNull String id, @NotNull C defaultConfig, @NotNull Class<C> typeOfConfig) {
        return register(id, defaultConfig, typeOfConfig, new Gson());
    }

    public <C> Configuration<C> register(@NotNull String id, @NotNull C defaultConfig, @NotNull Class<C> typeOfConfig, Gson gson) {
        Configuration<C> config = new Configuration<>(id, defaultConfig, typeOfConfig, gson);
        configList.add(config);
        return config;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <C> Configuration<C> config(String id) {
        for(Configuration item : configList) {
            if(item.id.equals(id)) {
                return (Configuration<C>) item;
            }
        }
        return null;
    }

    public static ConfigManager get() {
        if(instance == null) instance = new ConfigManager();
        return instance;
    }
}
