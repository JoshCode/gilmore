package nl.codefox.gilmore.command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.custom.CreateCustomCommand;
import nl.codefox.gilmore.command.custom.DeleteCustomCommand;
import nl.codefox.gilmore.command.custom.EditCustomCommand;
import nl.codefox.gilmore.command.custom.ListCustomCommands;
import nl.codefox.gilmore.database.GilmoreDatabase;

import java.util.Map;
import java.util.Set;

public class CustomCommand extends GilmoreCommand
{
    private static Map<String, String> commands;

    public CustomCommand()
    {
        super("Allows you to make custom commands", "Usage: !custom [create|edit|delete|list]", 2, 100, Permission.MANAGE_SERVER, "!custom");
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
        commands = GilmoreDatabase.getCommands();
    }

    public static boolean commandExists(String command)
    {
        return commands.containsKey(command);
    }
    
    public static Set<String> getCommands()
    {
        return commands.keySet();
    }

    public static String getCommand(String command)
    {
        return commands.get(command);
    }

    public static void editCommand(String command, String description) 
    {
        commands.put(command, description);
    }

    public static void deleteCommand(String command) 
    {
        commands.remove(command);
    }
}
