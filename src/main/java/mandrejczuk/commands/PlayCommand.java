package mandrejczuk.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mandrejczuk.audio.GuildPlayer;
import mandrejczuk.audio.GuildPlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayCommand implements ICommand{
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Plays requested media";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new LinkedList<>();
        data.add(new OptionData(OptionType.STRING,"title","Title for which search"));
        data.add(new OptionData(OptionType.STRING,"url","Link to Media"));
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

            var options = event.getOptions();


        if(options.size() > 1)
        {
            event.reply("Możesz wybrać tylko jedną z opcji gostek").queue();
        } else if (options.isEmpty()) {

            if(GuildPlayerManager.getInstance().existsById(event.getGuild().getIdLong()))
            {
                var guildPLayer = GuildPlayerManager.getInstance().getOrCreate(event.getGuild());
                guildPLayer.unpause();
            };
        }
        else {
            var guildPlayer = GuildPlayerManager.getInstance().getOrCreate(event.getGuild());
//            if(guildPlayer.getCurrentChannel() == null)
//            {
//                event.reply(event.getMember().getAsMention() +" odtwarzacza nie ma na serwerze - kolejki nie dodasz").queue();
//                GuildPlayerManager.getInstance().remove(event.getGuild());
//                return;
//            }
            var optionTitle = options.get(0).getName();
            var optionValue = options.get(0).getAsString();

            Consumer<AudioItem> itemConsumer = (audioItem)-> {
                if (audioItem instanceof AudioTrack track) {
                    event.reply("Na " + guildPlayer.getQueueSize() + " pozycji do kolejki dodano: " + track.getInfo().uri).queue();
                } else if (audioItem instanceof AudioPlaylist playlist) {
                    event.reply("Dodano " + playlist.getTracks().size() + " do kolejki").queue();
                }
            };
            Function<Throwable,Void> itemException = (throwable) -> {
                event.reply("blond dodawania do kolejki: " + throwable.getMessage()).queue();
                return null;
                };
            switch (optionTitle)
            {
                case "url" -> {
                    if(guildPlayer.getCurrentChannel() == null)
                    {
                        guildPlayer.joinChannel(event.getMember());
                        guildPlayer.play( optionValue)
                                .thenAccept(itemConsumer)
                                .exceptionally(itemException);
                    }
                    else {
                        guildPlayer.play(optionValue)
                                .thenAccept(itemConsumer)
                                .exceptionally(itemException);
                    }
                }
                case "title" ->{
                    if(guildPlayer.getCurrentChannel() == null)
                    {
                        guildPlayer.joinChannel(event.getMember());
                        guildPlayer.play(guildPlayer.getPlatform().search + optionValue)
                                .thenAccept(itemConsumer)
                                .exceptionally(itemException);

                    }
                    else {
                        guildPlayer.play(guildPlayer.getPlatform().search + optionValue)
                                .thenAccept(itemConsumer)
                                .exceptionally(itemException);
                    }
                }
            }
        }

    }
}
