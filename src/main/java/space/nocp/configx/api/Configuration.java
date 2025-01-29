package space.nocp.configx.api;

import com.google.gson.Gson;
import space.nocp.configx.ConfigX;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class Configuration {
    private final String name;
    private File file;
    private HashMap<String, Object> config;

    protected Configuration(String name, File file, HashMap<String, Object> loadedConfig) {
        this.name = name;
        this.file = file;
        config = loadedConfig;
    }

    public void save() {
        this.save(new Gson());
    }

    public void save(Gson providedGson) {
        ConfigManager.get().saveFile(file, config, providedGson);
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return name +".json";
    }

    public Path getPath() {
        return ConfigX.CONFIG_PATH.resolve(getFileName()).toAbsolutePath();
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return (T) config.get(key);
    }

    public <T> void setValue(String key, T value) {
        config.put(key, value);
    }

    public void setAll(HashMap<String, Object> newConfig) {
        newConfig.forEach(this::setValue);
    }

    public void forEach(BiConsumer<? super String, ? super Object> action) {
        config.forEach(action);
    }
}
