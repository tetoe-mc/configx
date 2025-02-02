package space.nocp.configx.api;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.nocp.configx.ConfigX;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigManager {
    private static ConfigManager instance;
    private static final AtomicBoolean isNormalStop = new AtomicBoolean(false);

    private final List<Configuration> configList = new ArrayList<>();

    private ConfigManager() {
        // Auto save when server stops or crashes
        ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
            configList.forEach(Configuration::save);
            ConfigX.LOGGER.info("[ConfigX] All configurations are saved.");
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(!isNormalStop.get()) {
                configList.forEach(Configuration::save);
                ConfigX.LOGGER.info("[ConfigX] All configurations are saved.");
            }
        }));

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
