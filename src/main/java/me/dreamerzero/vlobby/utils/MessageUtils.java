package me.dreamerzero.vlobby.utils;

import com.velocitypowered.api.command.CommandSource;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class MessageUtils {
    private MessageUtils(){}
    public static void sendMessage(CommandSource source, String message){
        if(message.isBlank()) return;
        source.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    public static void sendMessage(CommandSource source, String message, TagResolver resolver){
        if(message.isBlank()) return;
        source.sendMessage(MiniMessage.miniMessage().deserialize(message, resolver));
    }
}
