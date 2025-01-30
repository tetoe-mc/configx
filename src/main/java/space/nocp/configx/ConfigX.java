package space.nocp.configx;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import space.nocp.configx.api.ConfigManager;
import space.nocp.configx.command.ConfigXCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class ConfigX implements ModInitializer {
	public static final String MOD_ID = "configx";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().toAbsolutePath();

	public ConfigManager manager;

	@Override
	public void onInitialize() {
		manager = ConfigManager.get();

		CommandRegistrationCallback.EVENT.register((dispatcher, access, environment) -> new ConfigXCommand(dispatcher));
	}
}
