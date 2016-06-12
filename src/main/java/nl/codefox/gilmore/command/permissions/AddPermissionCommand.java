package nl.codefox.gilmore.command.permissions;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.command.PermissionsCommand;
import nl.codefox.gilmore.util.MessageDeleter;
import nl.codefox.gilmore.util.StringUtil;

import java.util.Optional;

public class AddPermissionCommand extends GilmoreCommand {
    public AddPermissionCommand() {
        super("Adds permission to command and alias' using Discord roles", "!permission add [role] ![command] [subcommand]", 2, 10, Permission.MANAGE_SERVER, "!permission add", "!perm add");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        String roleName = args[0];
        String finalCommand = StringUtil.arrayToString(args, 1, " ");
        Optional<Role> r = channel.getGuild().getRoles().stream().filter(rr -> rr.getName().equalsIgnoreCase(roleName)).findFirst();

        if (r.isPresent()) {
            Role role = r.get();
            if (Gilmore.getCommandListener().commandExists(finalCommand)) {
                for (GilmoreCommand gilmoreCommand : Gilmore.getCommandListener().getCommands()) {
                    if (gilmoreCommand.getAliases().contains(finalCommand)) {
                        for (String alias : gilmoreCommand.getAliases()) {
                            PermissionsCommand.addCommandPermission(alias, role);
                        }
                    }
                }
                Message message = channel.sendMessage(String.format("[%s} The command %s now requires the role %s", author.getAsMention(), finalCommand, role.getName()));
                new MessageDeleter(message);
            } else {
                Message message = channel.sendMessage(String.format("[%s} The command %s does not exist", author.getAsMention(), finalCommand));
                new MessageDeleter(message);
            }
        } else {
            Message message = channel.sendMessage(String.format("[%s} The role %s does not exist, please check spelling and try again", author.getAsMention(), roleName));
            new MessageDeleter(message);
        }
    }
}
