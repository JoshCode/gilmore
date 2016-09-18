package nl.codefox.gilmore.command;

import nl.codefox.gilmore.command.custom.CreateCustomCommand;
import nl.codefox.gilmore.command.custom.DeleteCustomCommand;
import nl.codefox.gilmore.command.custom.EditCustomCommand;
import nl.codefox.gilmore.command.custom.ListCustomCommands;
import nl.codefox.gilmore.command.custom.RawCustomCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomCommand extends GilmoreCommand {
    private static Map<String, String> commands;

    public CustomCommand() {
        this.addSubCommand(new CreateCustomCommand());
        this.addSubCommand(new EditCustomCommand());
        this.addSubCommand(new DeleteCustomCommand());
        this.addSubCommand(new ListCustomCommands());
        this.addSubCommand(new RawCustomCommand());
        load();
    }

    @Override
    public String getDescription() {
        return "Allows you to make custom commands";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!custom");
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return 100;
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

    private void load() {
        commands = GilmoreDatabase.getCommands();
    }
}
