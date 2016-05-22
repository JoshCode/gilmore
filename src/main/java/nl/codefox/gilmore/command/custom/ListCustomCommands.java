package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;

public class ListCustomCommands extends GilmoreCommand {

    public ListCustomCommands() {
        super("Lists custom command", "Usage: !custom list", 2, null, "!custom list");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) 
    {
        StringBuilder builder = new StringBuilder(String.format("[%s] `Here is a list of all available custom commands`\n", event.getAuthor().getAsMention()));
        
        builder.append("```");
        
        if (!CustomCommand.getCommands().isEmpty()) 
        {
            for (String str : CustomCommand.getCommands()) 
            {
                builder.append(String.format("> %s\n", str));
            }
        } 
        else 
        {
            builder.append("There are currently no custom commands!");
        }
        
        builder.append("```");
        
        event.getChannel().sendMessage(builder.toString());
    }
}
