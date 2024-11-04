package mandrejczuk.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mandrejczuk.audio.GuildPlayerManager;
import mandrejczuk.audio.Radio;
import mandrejczuk.configuration.Configuration;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RadioCommand implements ICommand{

   private final List<Radio> radioList = new LinkedList<>();
    public RadioCommand() {

        Configuration.getInstance().getRadiosProperties().forEach(
                (k,v)->radioList.add(new Radio(k.toString(),v.toString()))
        );
    }

    @Override
    public String getName() {
        return "radio";
    }

    @Override
    public String getDescription() {
        return "radio";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new LinkedList<>();
      var optionData = new OptionData(OptionType.STRING,"radio","wybierz radio do grania");
      radioList.forEach(r->optionData.addChoice(r.getName(),r.getAddress()));
      data.add(optionData);
        data.add(new OptionData(OptionType.STRING,"play","start radio")
        );
        data.add(new OptionData(OptionType.STRING,"stop","stop radio")
        );
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        var optionList = event.getOptions();
        var guildPlayer = GuildPlayerManager.getInstance().getOrCreate(event.getGuild());
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
        for (var option : optionList)
        {
            switch (option.getName())
            {
                case "radio"->{
                   var opt = radioList.stream().filter(r->r.getAddress().equals(option.getAsString())).findAny();
                   opt.ifPresent(r->guildPlayer.setRadio(r));
                    System.out.println(guildPlayer.getRadio());
                }
                case "play"->{
                    guildPlayer.joinChannel(event.getMember());
                    guildPlayer.play(guildPlayer.getRadio().getAddress())
                            .thenAccept(itemConsumer)
                            .exceptionally(itemException);
                    if(guildPlayer.getQueue().size() >0)
                    {
                        guildPlayer.moveTrackFromTo(guildPlayer.getQueueSize(), 0);
                        guildPlayer.skipPlaying();
                    }

                }
                case "stop"->{
                    guildPlayer.skipPlaying();
                }
            }
        }



    }
}
