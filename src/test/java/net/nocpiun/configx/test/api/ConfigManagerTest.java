package net.nocpiun.configx.test.api;

import net.nocpiun.configx.api.ConfigManager;
import net.nocpiun.configx.api.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    public void testModifyConfig() {
        Configuration config = manager.getOrCreateConfig(testConfigName, defaultTestConfig);

        config.setValue("test-2", false);
        config.save();

        Assertions.assertEquals(false, manager.getOrCreateConfig(testConfigName, defaultTestConfig).getValue("test-2"));
    }
}
