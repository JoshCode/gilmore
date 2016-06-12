package nl.codefox.gilmore.command.permissions;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.command.PermissionsCommand;
import nl.codefox.gilmore.util.MessageDeleter;
import nl.codefox.gilmore.util.StringUtil;

public class ListPermissionCommand extends GilmoreCommand {
    public ListPermissionCommand() {
        super("List the roles that the user must have to call the command", "!permissions list ![command] [subcommand]", 1, 20, Permission.MANAGE_CHANNEL, "!permission list", "!perm list");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String finalCommand = StringUtil.arrayToString(args, 0, " ");

        StringBuilder builder = new StringBuilder(String.format("[%s] `Here is a list of all roles with permission for the command %s`\n", author.getAsMention(), finalCommand));
        builder.append("```");
        if (PermissionsCommand.containsCommandPermission(finalCommand)) {
            for (String role : PermissionsCommand.getCommandPermissions(finalCommand)) {
                channel.getGuild().getRoles().stream().filter(role1 -> role1.getId().equalsIgnoreCase(role)).forEach(role1 ->   builder.append(String.format("> %s\n", role1.getName())));
            }
        }else{
            builder.append("There are currently no role permissions!");
        }
        builder.append("```");

        Message message = channel.sendMessage(builder.toString());
        new MessageDeleter(message);
    }
}
