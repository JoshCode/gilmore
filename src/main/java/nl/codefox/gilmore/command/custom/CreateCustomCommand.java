package nl.codefox.gilmore.command.custom;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

public class CreateCustomCommand extends GilmoreCommand {

    public CreateCustomCommand() {
        super("Create a custom command", "Usage: !custom create [command] [description]", 4, 100, Permission.MANAGE_SERVER, "!custom create");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) {
        String desc = StringUtil.arrayToString(args, 3, " ");
        String label = (args[2].contains("!") ? args[2] : "!" + args[2]);

        if (CustomCommand.commandExists(label)) {
            event.getChannel().sendMessage(String.format("[%s] `This command already exists, to edit use !custom edit %s [description]`", event.getAuthor().getAsMention(), label));
        } else {
            GilmoreDatabase.addCommand(label, desc);
            CustomCommand.editCommand(label, desc);
            event.getChannel().sendMessage(String.format("[%s] `The command '%s' has been created`", event.getAuthor().getAsMention(), label));
        }
    }
}
