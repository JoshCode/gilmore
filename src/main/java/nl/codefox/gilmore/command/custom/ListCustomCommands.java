package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;

public class ListCustomCommands extends GilmoreCommand {

    public ListCustomCommands() {
        super("Lists custom command", "Usage: !custom list", 2, Permission.MANAGE_SERVER, "!custom list");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) {

        CustomCommand.listCustomCommands(args, event);
    }
}
