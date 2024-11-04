package mandrejczuk.commands;

import mandrejczuk.audio.GuildPlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Collections;
import java.util.List;

public class DisconnectCommand  implements ICommand{
    @Override
    public String getName() {
        return "disconnect";
    }

    @Override
    public String getDescription() {
        return "Disconnect from server";
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.emptyList();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        var guildPlayer = GuildPlayerManager.getInstance().getOrCreate(event.getGuild());

        if (guildPlayer.getCurrentChannel() != null) {
           guildPlayer.disconnect();
            event.reply("Rozlonczylem sie z kanalu bo mnie " + event.getMember().getAsMention() + " wyprosil").queue();
        }
        else event.reply("Nie jestem na zadnym kanale" + event.getMember().getAsMention()).queue();

    }
}
