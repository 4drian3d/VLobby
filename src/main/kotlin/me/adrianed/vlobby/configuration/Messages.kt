package me.adrianed.vlobby.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
class Messages: Section {
    @Comment(
        "If an error occurs when sending the player to the lobby,\n"+
        "the following message will be shown"
    )
    var errorMessage = "<red>You could not be transported to the server <server>"

    @Comment("Message to be sent in case no lobbies are available to send to player")
    var notAvailableServerMessage = "<red>No lobby servers available now, try again later"

    @Comment(
        "In case the player's teleportation is successful,\n"+
        "this message will be sent.\n"+
        "Like all messages, if left empty, it will not be sent"
    )
    var successfullyMessage = "<red>You have been successfully sent to the lobby <server>"

    @Comment(
        "Message to send in case the player is in the same lobby\n"+
        "as the one to which the player is trying to teleport"
    )
    var alreadyInThisLobby = "<red>You are already in the Lobby"
}