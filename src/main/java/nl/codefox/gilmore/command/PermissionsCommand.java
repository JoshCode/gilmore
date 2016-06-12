package nl.codefox.gilmore.command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.permissions.AddPermissionCommand;
import nl.codefox.gilmore.command.permissions.DeletePermissionCommand;
import nl.codefox.gilmore.command.permissions.ListPermissionCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PermissionsCommand extends GilmoreCommand {
    private static Map<String, List<String>> commandPermissions;

    public PermissionsCommand() {
        super("Allows you to make custom commands", "Usage: !permissions [add|delete|list]", 1, 20, Permission.MANAGE_SERVER, "!permissions", "!perm");
        load();
    }

    public static boolean containsCommandPermission(String command) {
        return commandPermissions.containsKey(command);
    }

    public static List<String> getCommandPermissions(String command) {
        return commandPermissions.get(command);
    }

    public static void addCommandPermission(String command, Role role) {
        List<String> roleList = commandPermissions.get(command);
        if (roleList == null) {
            roleList = new ArrayList<>();
            commandPermissions.put(command, roleList);
        }

        roleList.add(role.getId());
        GilmoreDatabase.addCommandPermissions(command, role);
    }

    public static boolean removeCommandPermission(String command, Role role) {
        if (commandPermissions.containsKey(command)) {
            commandPermissions.get(command).remove(role.getId());
            GilmoreDatabase.removeCommandPermissions(command, role);
            return true;
        }
        return false;
    }

    @Override
    public void process(String oldCommand, String[] oldArgs, TextChannel channel, User author, MessageReceivedEvent event) {
        String command = oldCommand + " " + oldArgs[0];
        String[] args = Arrays.copyOfRange(oldArgs, 1, oldArgs.length);

        switch (oldArgs[0]) {
            case "add":
                new AddPermissionCommand().runCommand(command, args, channel, author, event);
                break;
            case "delete":
                new DeletePermissionCommand().runCommand(command, args, channel, author, event);
                break;
            case "list":
                new ListPermissionCommand().runCommand(command, args, channel, author, event);
                break;
            default:
                invalidUsage(oldCommand, oldArgs, channel, author, event);
                break;
        }
    }

    private void load() {
        commandPermissions = GilmoreDatabase.getCommandPermissions();
    }
}
