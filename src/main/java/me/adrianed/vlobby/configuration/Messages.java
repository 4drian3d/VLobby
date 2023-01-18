package me.adrianed.vlobby.configuration;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@SuppressWarnings("all")
@ConfigSerializable
public class Messages implements Section {
    @Comment("""
        If an error occurs when sending the player to the lobby,
        the following message will be shown""")
    private String errorMessage = "<red>You could not be transported to the server <server>";
    @Comment("Message to be sent in case no lobbies are available to send to player")
    private String notAvailableServerMessage = "<red>No lobby servers available now, try again later";
    @Comment("""
        In case the player's teleportation is successful,
        this message will be sent.
        Like all messages, if left empty, it will not be sent
            """)
    private String successfullyMessage = "<red>You have been successfully sent to the lobby <server>";
    @Comment("""
        Message to send in case the player is in the same lobby
        as the one to which the player is trying to teleport""")
    private String alreadyInThisLobby = "<red>You are already in the Lobby";
    public String getErrorMessage() {
        return errorMessage;
    }

    public String getNotAvailableServerMessage() {
        return notAvailableServerMessage;
    }

    public String getSuccessfullyMessage() {
        return successfullyMessage;
    }

    public String getAlreadyInThisLobby() {
        return alreadyInThisLobby;
    }
}
