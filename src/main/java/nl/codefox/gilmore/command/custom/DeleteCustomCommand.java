package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;

public class DeleteCustomCommand extends GilmoreCommand {

    public DeleteCustomCommand() {
        super("Delete a custom command", "Usage: !custom delete ![command]", 3, Permission.MANAGE_SERVER, "!custom delete");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) {

        if (CustomCommand.commandExists(args[2]))
        {
            GilmoreDatabase.deleteCommand(args[2]);
            CustomCommand.deleteCommand(args[2]);
            event.getChannel().sendMessage(String.format("[%s] `The command %s has been delete`", event.getAuthor().getAsMention(), args[2]));
        }
        else
        {
            event.getChannel().sendMessage(String.format("[%s] `This command doesn't exists, please create the command %s, first`", event.getAuthor().getAsMention(), args[3]));
        }
    }
}
