package mandrejczuk;

import mandrejczuk.configuration.Configuration;
import mandrejczuk.listeners.ListenerAdapterImpl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.IOException;
import java.nio.file.Path;


public class Main {
    public static void main(String  [] args) {



        var config = Configuration.getInstance();
        JDA jda = JDABuilder.
                createLight(config.getToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES)
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new ListenerAdapterImpl())
                .build();

    }
}