package nl.codefox.gilmore.command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public abstract class GilmoreCommand {

    private final String MISSING_PERMISSION = "[%s] `Sorry, I cannot run that command for you, you don't have permission.`";
    private final String INVALID_USAGE = "[%s] `Sorry, I cannot run that command without the correct arguments. %s`";

    private final String description;
    private final String usage;
    private final int min;
    private final int max;
    private final Permission permission;
    private final List<String> aliases;

    public GilmoreCommand(String description, String usage, int args, Permission permission, String... aliases) {
        this.description = description;
        this.usage = usage;
        this.min = args;
        this.max = args;
        this.permission = permission;
        this.aliases = Arrays.asList(aliases);
    }

    public GilmoreCommand(String description, String usage, int min, int max, Permission permission, String... aliases) {
        this.description = description;
        this.usage = usage;
        this.min = min;
        this.max = max;
        this.permission = permission;
        this.aliases = Arrays.asList(aliases);
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public int getMinimumArguments() {
        return min;
    }

    public int getMaximumArguments() {
        return max;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public Permission getPermission() {
        return permission;
    }

    public void process(String[] args, MessageReceivedEvent event) {

        User user = event.getAuthor();

        boolean hasPermission = false;
        if (getPermission() == null) {
            hasPermission = true;
        } else {
            for (Role role : event.getGuild().getRolesForUser(user)) {
                if (role.getPermissions().contains(getPermission())) {
                    hasPermission = true;
                    break;
                }
            }
        }

        boolean validCommandUsage = false;
        if (args.length >= min && args.length <= max) {
            validCommandUsage = true;
        }

        if (!hasPermission) {
            permission(event, user);
            return;
        }

        if (!validCommandUsage) {
            usage(event, user);
            return;
        }

        run(args, event);

    }

    protected void permission(MessageReceivedEvent event, User user) {
        event.getChannel().sendMessage(String.format(MISSING_PERMISSION, user.getAsMention()));
    }

    protected void usage(MessageReceivedEvent event, User user) {
        event.getChannel().sendMessage(String.format(INVALID_USAGE, user.getAsMention(), getUsage()));
    }

    public abstract void run(String[] args, MessageReceivedEvent event);

}
