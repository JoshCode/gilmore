package nl.codefox.gilmore.command;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.custom.CreateCustomCommand;
import nl.codefox.gilmore.command.custom.DeleteCustomCommand;
import nl.codefox.gilmore.command.custom.EditCustomCommand;
import nl.codefox.gilmore.command.custom.ListCustomCommands;
import nl.codefox.gilmore.database.GilmoreDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class CustomCommand extends GilmoreCommand {
    private static Map<String, String> commands;

    public CustomCommand() {
        super("Allows you to make custom commands", "Usage: !custom [create|edit|delete|list]", 1, 100, new ArrayList<>(
                Arrays.asList("Administrator", "Server Owner")), "!custom");
        load();
    }

    public static boolean commandExists(String command) {
        return commands.containsKey(command);
    }

    public static Set<String> getCommands() {
        return commands.keySet();
    }

    public static String getCommand(String command) {
        return commands.get(command);
    }

    public static void editCommand(String command, String description) {
        commands.put(command, description);
    }

    public static void deleteCommand(String command) {
        commands.remove(command);
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String verb = args[0];

        switch (verb) {
            case "create":
                new CreateCustomCommand().runCommand(command, args, channel, author, event);
                break;
            case "edit":
                new EditCustomCommand().runCommand(command, args, channel, author, event);
                break;
            case "delete":
                new DeleteCustomCommand().runCommand(command, args, channel, author, event);
                break;
            case "list":
                new ListCustomCommands().runCommand(command, args, channel, author, event);
                break;
            default:
                invalidUsage(command, args, channel, author, event);
                break;
        }
    }

    private void load() {
        commands = GilmoreDatabase.getCommands();
    }
}
