package nl.codefox.gilmore.command;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.util.Logging;

import java.util.Arrays;
import java.util.List;

public class UnmuteCommand extends GilmoreCommand {

    @Override
    public String getUsage() {
        return "Usage: !unmute [username]";
    }

    @Override
    public String getDescription() {
        return "Unmutes a users allowing them to type in channels!";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!unmute");
    }

    @Override
    public int getRequiredArguments() {
        return 1;
    }

    @Override
    public List<String> getRolePermission() {
        return Arrays.asList("Administrator", "Server Owner");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        Role role = MuteCommand.getMuteRole();

        try {
            User user = event.getJDA().getUsersByName(args[0]).get(0);

            if (!event.getGuild().getRolesForUser(user).contains(role)) {
                Message message = channel.sendMessage(String.format("[%s] `'%s' isn't currently muted`", author.getAsMention(), args[0]));
                return;
            }

            event.getGuild().getManager().removeRoleFromUser(user, role).update();
            Message message = channel.sendMessage(String.format("[%s] `'%s' has been unmuted`", author.getAsMention(), args[0]));
        } catch (Exception ex) {
            Message message = channel.sendMessage(String.format("[%s] `Could not unmute user '%s'`", author.getAsMention(), args[0]));
            Logging.log(ex);
        }
    }
}