package me.dreamerzero.vlobby.commands;

import java.util.Arrays;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.dreamerzero.vlobby.VLobby;
import me.dreamerzero.vlobby.utils.MessageUtils;
import me.dreamerzero.vlobby.utils.ServerUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public final class LobbyCommand {
    private LobbyCommand(){}
    public static void loadCommands(VLobby plugin){
        ProxyServer proxy = plugin.getProxy();
        String[] commands = plugin.getConfig().getCommands();
        if(commands.length == 0){
            return;
        }
        LiteralCommandNode<CommandSource> lobbyCommand = LiteralArgumentBuilder.<CommandSource>literal(commands[0])
            .requires(src -> src.getPermissionValue("vlobby.command") != Tristate.FALSE)
            .executes(cmd -> {
                CommandSource source = cmd.getSource();
                if(!(source instanceof Player)){
                    plugin.getLogger().info(plugin.getConfig().getMessages().getConsoleMessage());
                    return 1;
                }
                Player player = (Player)source;
                RegisteredServer lobbyToSend = ServerUtils.getConfigServer(plugin);
                if(lobbyToSend == null){
                    plugin.getLogger().error("Cannot found lobby server to send");
                    MessageUtils.sendMessage(source, plugin.getConfig().getMessages().getNotAvailableServerMessage());
                    return 1;
                }
                player.createConnectionRequest(lobbyToSend).connect().thenAccept(result -> {
                    if(!result.isSuccessful()){
                        plugin.getLogger().error("Cannot connect to server: {}", lobbyToSend.getServerInfo().getName());
                        MessageUtils.sendMessage(
                            player,
                            plugin.getConfig().getMessages().getErrorMessage(),
                            Placeholder.unparsed("server", lobbyToSend.getServerInfo().getName())
                        );
                    } else {
                        MessageUtils.sendMessage(
                            player,
                            plugin.getConfig().getMessages().getSuccesfullyMessage(),
                            Placeholder.unparsed("server", lobbyToSend.getServerInfo().getName())
                        );
                    }
                });
                return 1;
            })
            .build();
        BrigadierCommand bCommand = new BrigadierCommand(lobbyCommand);
        CommandManager manager = proxy.getCommandManager();
        CommandMeta.Builder builder = manager.metaBuilder(bCommand).plugin(plugin);
        if(commands.length > 1){
            builder.aliases(Arrays.copyOfRange(commands, 1, commands.length-1));
        }
        manager.register(builder.build(), bCommand);
    }
}
