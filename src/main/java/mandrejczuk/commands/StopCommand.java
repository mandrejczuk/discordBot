package mandrejczuk.commands;

import mandrejczuk.audio.GuildPlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class StopCommand implements ICommand{
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "stop sounds";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        var guild = event.getGuild();

        if(GuildPlayerManager.getInstance().existsById(guild.getIdLong()))
        {
            var guildPlayer = GuildPlayerManager.getInstance().getOrCreate(guild);
            guildPlayer.pause();
        }
        else
        {
            event.reply("Nic nie gra wiec nie ma co stopowac").queue();
        }

    }
}
