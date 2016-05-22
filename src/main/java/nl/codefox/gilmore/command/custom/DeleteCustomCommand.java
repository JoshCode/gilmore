package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;

public class DeleteCustomCommand extends GilmoreCommand {

    public DeleteCustomCommand() {
        super("Delete a custom command", "Usage: !custom delete [command]", 3, Permission.MANAGE_SERVER, "!custom delete");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) 
    {
        String label = (args[2].contains("!") ? args[2] : "!" + args[2]);
        
        if (CustomCommand.commandExists(label))
        {
            GilmoreDatabase.deleteCommand(label);
            CustomCommand.deleteCommand(label);
            event.getChannel().sendMessage(String.format("[%s] `The command '%s' has been deleted`", event.getAuthor().getAsMention(), label));
        }
        else
        {
            event.getChannel().sendMessage(String.format("[%s] `This command doesn't exist, please create the command '!%s', first`", event.getAuthor().getAsMention(), args[2]));
        }
    }
}
