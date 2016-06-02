package nl.codefox.gilmore.command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.util.Logging;

public class UnmuteCommand extends GilmoreCommand {
    
    public UnmuteCommand() {
        super("Allows a muted to typing into chat", "Usage: !unmute [username]", 1, Permission.VOICE_MUTE_OTHERS, "!unmute");
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        Role role = MuteCommand.getMuteRole();

        try {
            User user = event.getJDA().getUsersByName(args[0]).get(0);

            if (!event.getGuild().getRolesForUser(user).contains(role)) {
                channel.sendMessage(String.format("[%s] `'%s' isn't currently muted`", author.getAsMention(), args[0]));
                return;
            }

            event.getGuild().getManager().removeRoleFromUser(user, role).update();
            channel.sendMessage(String.format("[%s] `'%s' has been unmuted`", author.getAsMention(), args[0]));
        } catch (Exception ex) {
            channel.sendMessage(String.format("[%s] `Could not unmute user '%s'`", author.getAsMention(), args[0]));
            Logging.log(ex);
        }

    }


}
