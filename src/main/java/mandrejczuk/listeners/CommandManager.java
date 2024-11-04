package mandrejczuk.listeners;

import mandrejczuk.commands.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private final List<ICommand> commands = new LinkedList<>();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
       for(Guild guild: event.getJDA().getGuilds())
       {
           for(ICommand command: commands)
           {
               guild.upsertCommand(command.getName(),command.getDescription()).addOptions(command.getOptions()).queue();
           }
       }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for(ICommand command :commands)
        {
            if(command.getName().equals(event.getName()))
            {
                command.execute(event);
                return;
            }
        }
    }

    public void add(ICommand command)
    {
        commands.add(command);
    }


}
