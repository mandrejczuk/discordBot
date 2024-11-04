package mandrejczuk.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mandrejczuk.audio.GuildPlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class QueueCommand implements ICommand{
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "queue info and operations";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new LinkedList<>();
        data.add(new OptionData(OptionType.BOOLEAN,"info","przekazuje informacje"));
        data.add(new OptionData(OptionType.INTEGER,"remove","usuwa z kolejki dany id")
                .setMinValue(0));
        data.add(new OptionData(OptionType.STRING,"move","Przesuwa danom piosenke wedlug wzorku source-destination"));

        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        var options = event.getOptions();
        if(GuildPlayerManager.getInstance().existsById(event.getGuild().getIdLong())) {
            if(options.size() > 1)
            {
                event.reply("Możesz wybrać tylko jedną z opcji gostek").queue();
            } else if (options.isEmpty()) {
                event.reply("Musisz wybrać któromś z opcji").queue();
            }
            else {
                var guildPlayer = GuildPlayerManager.getInstance().getOrCreate(event.getGuild());
                var optionTitle = options.get(0).getName();
                switch (optionTitle)
                {
                    case "info"->{
                    var list = guildPlayer.getQueue();

                    StringBuilder s = new StringBuilder();
                    s.append("Aktulanie gra %s \n".formatted(guildPlayer.getPlayingTrackInfo().uri));
                    s.append("W kolejce jest %d trackow\n".formatted(list.size()));
                    for(int i  = 0 ; i < list.size()  ; i++)
                    {
                        var track = list.get(i);
                        if(s.length() + ("[%d].  <%s>\n".formatted(i+1,track.getInfo().uri).length()) > 2000) break;
                        s.append("[%d].  <%s>\n".formatted(i+1,track.getInfo().uri));
                    }
                    event.reply( s.toString()).queue();

                    }
                    case "remove"->{
                        var index = options.get(0).getAsInt();
                        try {
                            guildPlayer.removeTrackAt(index-1);
                            event.reply("usunelem").queue();
                        }catch (IllegalArgumentException e)
                        {
                            event.reply(event.getMember().getAsMention() +" "+ e.getMessage());
                        }
                    }
                    case "move" ->{
                        var optionString = options.get(0).getAsString();
                        Predicate <String> predicate = Pattern.compile("\\b\\d+\\b-\\b\\d+\\b").asPredicate();
                        try {
                            if(predicate.test(optionString)) {
                                var source = Integer.parseInt(optionString,0,optionString.indexOf("-"),10) - 1;
                                var destination = Integer.parseInt(optionString,optionString.indexOf("-")+1,optionString.length(),10) - 1;
                                guildPlayer.moveTrackFromTo(source,destination);
                                event.reply("przesunelem").queue();
                            }
                            else
                            {
                                throw new IllegalArgumentException("Podaj wedlug wzorku zrodlo-cel; np. 1-6 , 3-2");
                            }
                        }catch (IllegalArgumentException e)
                        {
                            event.reply(event.getMember().getAsMention() +" "+ e.getMessage());
                        }
                    }
                }
            }
        }
        else {
            event.reply("Nie ma grajka to nie ma kolejki gostek").queue();
        }
    }
}
