package space.nocp.configx.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import space.nocp.configx.ConfigX;

import java.util.Optional;

import static net.minecraft.server.command.CommandManager.literal;

public class ConfigXCommand implements Command<ServerCommandSource> {
    public final static String cmd = "configx";

    public ConfigXCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        final RootCommandNode<ServerCommandSource> root = dispatcher.getRoot();

        final LiteralCommandNode<ServerCommandSource> command = literal(cmd)
                .executes(this)
                .build();

        root.addChild(command);
    }

    @Override
    public int run(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();

        Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer(ConfigX.MOD_ID);
        if(mod.isEmpty()) return 0;
        final String version = mod.get().getMetadata().getVersion().getFriendlyString();

        source.sendMessage(Text.of("§lConfig X§r v"+ version));
        source.sendMessage(Text.of("§7Copyright (c) 2025 Tetoe-mc"));
        if(source.hasPermissionLevel(4)) {
            source.sendMessage(Text.of("Config folder path: "+ ConfigX.CONFIG_PATH));
        }

        return 1;
    }
}
