package mandrejczuk.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import mandrejczuk.audio.GuildPlayerManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.LinkedList;
import java.util.List;

public class JoinCommand implements ICommand {




    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Joins user channel";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new LinkedList<>();
        data.add(new OptionData(OptionType.CHANNEL,"channel", " Channel to join"));
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping channel = event.getOption("channel");
        Member member = event.getMember();
        var guildPlayer = GuildPlayerManager.getInstance().getOrCreate(event.getGuild());
        if(channel != null)
        {
            if(channel.getChannelType().isAudio())
            {
                guildPlayer.joinChannel(channel.getAsChannel().asVoiceChannel());
                event.reply("Dolonczylem na kanal " + channel.getAsMentionable().getAsMention()).queue();
            }
            else {
                event.reply(event.getMember().getAsMention() + " ty huncwocie musisz podac kanal glosowy").queue();
            }
        }
        else
        {
            guildPlayer.joinChannel(member);
            if(guildPlayer.getCurrentChannel() != null) {
                event.reply("Dolonczylem na kanal " + guildPlayer.getCurrentChannel().getAsMention()).queue();
            }
            else
            {
                event.reply(event.getMember().getAsMention() + " ty huncwocie doloncz na kanal zberezniku").queue();
                }

        }
    }
}
