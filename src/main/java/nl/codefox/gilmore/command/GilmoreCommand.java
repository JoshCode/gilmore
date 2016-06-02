package nl.codefox.gilmore.command;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

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
        this.min  = min;
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

    public abstract void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event);

    public void invalidPermissions(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        channel.sendMessage(String.format(MISSING_PERMISSION, author.getAsMention()));
    }

    public void invalidUsage(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        channel.sendMessage(String.format(INVALID_USAGE, author.getAsMention(), getUsage()));
    }

    public boolean isValidUsage(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        return args.length >= getMinimumArguments() && args.length <= getMaximumArguments();
    }

    public boolean hasPermission(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        if (getPermission() == null)
            return true;
        else
            for(Role role : event.getGuild().getRolesForUser(author))
                if (role.getPermissions().contains(getPermission()))
                    return true;

        return false;
    }

    public void runCommand(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        if (!hasPermission(command, args, channel, author, event)) {
            invalidPermissions(command, args, channel, author, event);
            return;
        }

        if (!isValidUsage(command, args, channel, author, event)) {
            invalidUsage(command, args, channel, author, event);
            return;
        }

        process(command, args, channel, author, event);
    }
}
