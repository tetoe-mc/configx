package space.nocp.configx.test.api;

import space.nocp.configx.ConfigX;
import space.nocp.configx.api.ConfigManager;
import space.nocp.configx.api.Configuration;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigManagerTest {
    private static final HashMap<String, Object> defaultTestConfig = new HashMap<>();
    private static final String testConfigName = "test-config";

    private final ConfigManager manager = ConfigManager.get();

    @BeforeAll
    public static void beforeAll() {
        defaultTestConfig.put("test-1", "Hello World");
        defaultTestConfig.put("test-2", true);
        defaultTestConfig.put("test-3", 1234);
        defaultTestConfig.put("test-4", null);
    }

    @Test
    public void testReadConfig() {
        Configuration config = manager.getOrCreateConfig(testConfigName, defaultTestConfig);

        Assertions.assertEquals("Hello World", config.getValue("test-1"));
        Assertions.assertEquals(true, config.getValue("test-2"));
        Assertions.assertEquals(1234, (int) config.getValue("test-3"));
        Assertions.assertEquals((Object) null, config.getValue("test-4"));

        Assertions.assertEquals(testConfigName, config.getName());
        Assertions.assertEquals(testConfigName +".json", config.getFileName());
        Assertions.assertEquals(ConfigX.CONFIG_PATH.resolve(config.getFileName()), config.getPath());
    }

    @Test
    public void testModifyConfig() {
        Configuration config = manager.getOrCreateConfig(testConfigName, defaultTestConfig);

        config.setValue("test-2", false);
        config.save();

        Assertions.assertEquals(false, manager.getOrCreateConfig(testConfigName, defaultTestConfig).getValue("test-2"));
    }

    @AfterAll
    public static void afterAll() throws IOException {
        FileUtils.deleteDirectory(new File(ConfigX.CONFIG_PATH.toString()));
    }
}
