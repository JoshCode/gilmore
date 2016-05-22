package nl.codefox.gilmore.command;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.command.custom.CreateCustomCommand;
import nl.codefox.gilmore.command.custom.DeleteCustomCommand;
import nl.codefox.gilmore.command.custom.EditCustomCommand;
import nl.codefox.gilmore.command.custom.ListCustomCommands;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

import java.util.Map;

public class CustomCommand extends GilmoreCommand
{
    private static Map<String, String> commandList;

    public CustomCommand()
    {
        super("Handle custom command", "Usage: !customCommand [create|edit|delete|list]", 2, 100, null, "!customCommand");
        load();
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event)
    {
        String verb = args[1];

        switch (verb)
        {
            case "create":
                new CreateCustomCommand().process(args, event);
                break;
            case "edit":
                new EditCustomCommand().process(args, event);
                break;
            case "delete":
                new DeleteCustomCommand().process(args, event);
                break;
            case "list":
                new ListCustomCommands().process(args, event);
                break;
            default:
                usage(event, event.getAuthor());
                break;
        }
    }

    private void load()
    {
        commandList = GilmoreDatabase.getCommands();
    }

    public static boolean commandExists(String command)
    {
        return commandList.containsKey(command);
    }

    public static String getCommand(String command)
    {
        return commandList.get(command);
    }

    public static void editCommand(String command, String description) {
        commandList.put(command, description);
    }

    public static void deleteCommand(String command) {
        commandList.remove(command);
    }

    public static void listCustomCommands(String[] args, MessageReceivedEvent event) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("[%s] ```Here is a list of all available custom commands;\n", event.getAuthor().getAsMention()));

        for(String command : commandList.keySet())
        {
            builder.append("> " + command + "\n");
        }

        builder.append("```");
        event.getAuthor().getPrivateChannel().sendMessage(builder.toString());
    }
}
