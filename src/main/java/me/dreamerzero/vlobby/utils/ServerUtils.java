package me.dreamerzero.vlobby.utils;

import java.util.Optional;
import java.util.Random;

import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.dreamerzero.vlobby.VLobby;
import me.dreamerzero.vlobby.enums.SendMode;

public final class ServerUtils {
    private ServerUtils(){}
    private static final Random rm = new Random();

    public static RegisteredServer getConfigServer(VLobby plugin){
        RegisteredServer server = null;
        switch(plugin.getConfig().getServerOptions().getSendMode()){
            case RANDOM: server = getRandomServer(plugin); break;
            case FIRST_AVAILABLE: server = getFirstServer(plugin); break;
            case FIRST_EMPTY: server = getFirstEmpty(plugin); break;
            case EMPTIEST: server = getEmptiestServer(plugin); break;
        }
        if(server != null) return server;
        return plugin.getConfig().getServerOptions().getSendMode() == SendMode.RANDOM
            ? getFirstServer(plugin)
            : getRandomServer(plugin);
    }
    public static RegisteredServer getRandomServer(VLobby plugin){
        final String[] servers = plugin.getConfig().getServerOptions().getLobbyServers();
        for(int i = 0; i < servers.length; i++){
            String server = servers[rm.nextInt(servers.length)-1];
            Optional<RegisteredServer> sv = plugin.getProxy().getServer(server);
            if(sv.isPresent()) return sv.get();
        }
        return null;
    }

    public static RegisteredServer getFirstServer(VLobby plugin){
        for(String st : plugin.getConfig().getServerOptions().getLobbyServers()){
            Optional<RegisteredServer> sv = plugin.getProxy().getServer(st);
            if(sv.isPresent()) return sv.get();
        }
        return null;
    }

    public static RegisteredServer getEmptiestServer(VLobby plugin){
        RegisteredServer emptiest = null;
        for(String st : plugin.getConfig().getServerOptions().getLobbyServers()){
            Optional<RegisteredServer> sv = plugin.getProxy().getServer(st);
            if(sv.isPresent()) {
                RegisteredServer actualsv = sv.get();
                if(emptiest == null)
                    emptiest = actualsv;
                else {
                    if(actualsv.getPlayersConnected().size() < emptiest.getPlayersConnected().size()){
                        emptiest = actualsv;
                    }
                }
            }
        }
        return emptiest;
    }

    public static RegisteredServer getFirstEmpty(VLobby plugin){
        for(String st : plugin.getConfig().getServerOptions().getLobbyServers()){
            Optional<RegisteredServer> optSv = plugin.getProxy().getServer(st);
            if(optSv.isPresent()){
                RegisteredServer sv = optSv.get();
                if(sv.getPlayersConnected().isEmpty()) return sv;
            }
        }
        return null;
    }
}
