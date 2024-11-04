package mandrejczuk;

import mandrejczuk.commands.*;
import mandrejczuk.configuration.Configuration;
import mandrejczuk.listeners.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


public class Main {
    public static void main(String  [] args) {


        var config = Configuration.getInstance();
        var commmandManager = new CommandManager();
        commmandManager.add(new JoinCommand());
        commmandManager.add(new DisconnectCommand());
        commmandManager.add(new PlayCommand());
        commmandManager.add(new QueueCommand());
        commmandManager.add(new StopCommand());
        commmandManager.add(new RadioCommand());
        JDA jda = JDABuilder.
                createLight(config.getToken(),
                        GatewayIntent.GUILD_VOICE_STATES,  // Ważne, żeby ten intent był włączony
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS
                )
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(commmandManager)
                .build();

        Runtime.getRuntime().addShutdownHook(new Thread(()->Configuration.getInstance().save()));

    }
}