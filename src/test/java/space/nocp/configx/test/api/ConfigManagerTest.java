package space.nocp.configx.test.api;

import space.nocp.configx.ConfigX;
import space.nocp.configx.api.ConfigManager;
import space.nocp.configx.api.Configuration;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class TestClass {
    String test1;
    boolean test2;
    int test3;
    Object test4;

    protected TestClass(String test1, boolean test2, int test3, Object test4) {
        this.test1 = test1;
        this.test2 = test2;
        this.test3 = test3;
        this.test4 = test4;
    }
}

public class ConfigManagerTest {
    private static final TestClass defaultTestConfig = new TestClass("Hello World", true, 1234, null);
    private static final String testConfigName = "test-config";

    private final ConfigManager manager = ConfigManager.get();

    @Test
    public void testReadConfig() {
        Configuration<TestClass> config = manager.getOrCreateConfig(testConfigName, defaultTestConfig, TestClass.class);
        TestClass obj = config.getObject();

        Assertions.assertEquals("Hello World", obj.test1);
        Assertions.assertTrue(obj.test2);
        Assertions.assertEquals(1234, obj.test3);
        Assertions.assertEquals((Object) null, obj.test4);

        Assertions.assertEquals(testConfigName, config.getName());
        Assertions.assertEquals(testConfigName +".json", config.getFileName());
        Assertions.assertEquals(ConfigX.CONFIG_PATH.resolve(config.getFileName()), config.getPath());
    }

    @Test
    public void testModifyConfig() {
        Configuration<TestClass> config = manager.getOrCreateConfig(testConfigName, defaultTestConfig, TestClass.class);
        TestClass obj = config.getObject();

        obj.test2 = false;
        config.save(obj);

        Assertions.assertFalse(manager.getOrCreateConfig(testConfigName, defaultTestConfig, TestClass.class).getObject().test2);
    }

    @AfterAll
    public static void afterAll() throws IOException {
        FileUtils.deleteDirectory(new File(ConfigX.CONFIG_PATH.toString()));
    }
}
