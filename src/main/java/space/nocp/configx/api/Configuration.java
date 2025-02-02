package space.nocp.configx.api;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import space.nocp.configx.ConfigX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class Configuration<C> {
    public final String id;
    private final C defaultConfig;
    private final Class<C> typeOfConfig;
    private final Gson gson;

    private C config;

    protected Configuration(
            @NotNull String id,
            @NotNull C defaultConfig,
            @NotNull Class<C> typeOfConfig,
            Gson gson
    ) {
        this.id = id;
        this.defaultConfig = defaultConfig;
        this.typeOfConfig = typeOfConfig;
        this.gson = gson;
        load();
    }

    public void load() {
        File file = new File(ConfigX.CONFIG_PATH.toString(), id +".json");

        if(file.exists()) {
            try(FileInputStream fis = new FileInputStream(file)) {
                config = gson.fromJson(new String(fis.readAllBytes()), typeOfConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            config = defaultConfig;
            save();
        }

        if(ConfigX.ENABLE_CONFIGX_LOG) ConfigX.LOGGER.info("[ConfigX] "+ getFileName() +" is loaded.");
    }

    /** @noinspection ResultOfMethodCallIgnored*/
    public void save() {
        File file = new File(ConfigX.CONFIG_PATH.toString(), id +".json");

        try(FileOutputStream fos = new FileOutputStream(file)) {
            file.createNewFile();

            fos.write(gson.toJson(config).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ConfigX.ENABLE_CONFIGX_LOG) ConfigX.LOGGER.info("[ConfigX] "+ getFileName() +" is saved.");
    }

    public C get() {
        return config;
    }

    public void set(C config) {
        this.config = config;
    }

    public String getFileName() {
        return id +".json";
    }

    public Path getPath() {
        return ConfigX.CONFIG_PATH.resolve(getFileName()).toAbsolutePath();
    }
}
