package space.nocp.configx.test.api;

import org.junit.jupiter.api.BeforeAll;
import space.nocp.configx.ConfigX;
import space.nocp.configx.api.*;
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
    private static final String testConfigId = "test-config";

    @BeforeAll
    public static void beforeAll() {
        ConfigManager.get().register(testConfigId, defaultTestConfig, TestClass.class);
    }

    @Test
    public void testReadConfig() throws Exception {
        final Configuration<TestClass> config = ConfigManager.get().config(testConfigId);
        if(config == null) {
            throw new Exception("Config is not found.");
        }

        final TestClass obj = config.get();

        Assertions.assertEquals("Hello World", obj.test1);
        Assertions.assertTrue(obj.test2);
        Assertions.assertEquals(1234, obj.test3);
        Assertions.assertEquals((Object) null, obj.test4);

        Assertions.assertEquals(testConfigId, config.id);
        Assertions.assertEquals(testConfigId +".json", config.getFileName());
        Assertions.assertEquals(ConfigX.CONFIG_PATH.resolve(config.getFileName()), config.getPath());
    }

    @Test
    public void testModifyConfig() throws Exception {
        final Configuration<TestClass> config = ConfigManager.get().config(testConfigId);
        if(config == null) {
            throw new Exception("Config is not found.");
        }

        final TestClass obj = config.get();

        obj.test2 = false;
        config.set(obj);
        config.save();

        Assertions.assertFalse(((TestClass) ConfigManager.get().config(testConfigId).get()).test2);
    }

    @AfterAll
    public static void afterAll() throws IOException {
        FileUtils.deleteDirectory(new File(ConfigX.CONFIG_PATH.toString()));
    }
}
