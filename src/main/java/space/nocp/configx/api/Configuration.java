package space.nocp.configx.api;

import com.google.gson.Gson;
import space.nocp.configx.ConfigX;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class Configuration<T> {
    private final String name;
    private File file;
    private T config;

    protected Configuration(String name, File file, T loadedConfig) {
        this.name = name;
        this.file = file;
        config = loadedConfig;
    }

    public void save(T obj) {
        this.save(obj, new Gson());
    }

    public void save(T obj, Gson providedGson) {
        config = obj;
        ConfigManager.get().saveFile(file, config, providedGson);
    }

    public void save() {
        ConfigManager.get().saveFile(file, config, new Gson());
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

    public T getObject() {
        return config;
    }
}
