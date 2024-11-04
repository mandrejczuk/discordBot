package mandrejczuk.commands;

import mandrejczuk.audio.GuildPlayer;
import mandrejczuk.audio.GuildPlayerManager;
import mandrejczuk.audio.Platform;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.LinkedList;
import java.util.List;

public class SetPlatformCommand implements ICommand{
    @Override
    public String getName() {
        return "set-platform";
    }

    @Override
    public String getDescription() {
        return "sets a default platform for title searching";
    }

    @Override
    public List<OptionData> getOptions() {
        LinkedList<OptionData> data = new LinkedList<>();
        data.add(new OptionData(OptionType.STRING, "set-platform","set platform for searching title")
                .addChoice("Youtube","youtube")
                .addChoice("Soundcloud","soundcloud")
                .setRequired(true));
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        var guild = event.getGuild();
        GuildPlayer guildPlayer = GuildPlayerManager.getInstance().getOrCreate(guild);
        switch (event.getOptions().get(0).getName())
        {
            case "youtube" -> guildPlayer.setPlatform(Platform.YOUTUBE);
            case "soundcloud" -> guildPlayer.setPlatform(Platform.SOUNDCLOUD);

        }
    }
}
