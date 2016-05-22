package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

public class EditCustomCommand extends GilmoreCommand {

    public EditCustomCommand() {
        super("Edit a custom command", "Usage: !custom edit ![command] [new description]", 4, 100, Permission.MANAGE_SERVER, "!custom edit");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) 
    {
        String name = StringUtil.arrayToString(args, 3, " ");

        if (CustomCommand.commandExists(args[2]))
        {
            GilmoreDatabase.editCommand(args[2], name);
            CustomCommand.editCommand(args[2], name);
            event.getChannel().sendMessage(String.format("[%s] `The command %s has been edited`", event.getAuthor().getAsMention(), args[2]));
        }
        else
        {
            event.getChannel().sendMessage(String.format("[%s] `This command doesn't exists, please create the command %s, first`", event.getAuthor().getAsMention(), args[3]));
        }
    }
}
