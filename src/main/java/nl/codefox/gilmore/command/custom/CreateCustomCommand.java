package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.ArrayUtil;

public class CreateCustomCommand extends GilmoreCommand {

    public CreateCustomCommand() {
        super("Create a custom command", "Usage: !customCommand create ![command] [description]", 4, 100, null, "!customCommand create");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) {
        String name = ArrayUtil.arrayToString(args, 3, " ");

        if (CustomCommand.commandExists(args[2]))
        {
            event.getChannel().sendMessage(String.format("[%s] `This command already exists, to edit use !customCommand edit %s`", event.getAuthor().getAsMention(), args[2]));
        }
        else
        {
            GilmoreDatabase.addCommand(args[2], name);
            event.getChannel().sendMessage(String.format("[%s] `The command %s has been created`", event.getAuthor().getAsMention(), args[3]));
        }
    }
}
