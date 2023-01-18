package me.adrianed.vlobby.configuration;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@SuppressWarnings("all")
@ConfigSerializable
public class Messages implements Section {
    private String errorMessage = "<red>You could not be transported to the server <server>";
    private String notAvailableServerMessage = "<red>No lobby servers available now, try again later";
    private String successfullyMessage = "<red>You have been successfully sent to the lobby <server>";
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
