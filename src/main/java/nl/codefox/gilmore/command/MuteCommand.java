package nl.codefox.gilmore.command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.util.Logging;

public class MuteCommand extends GilmoreCommand {

    public MuteCommand() {
        super("Stops a user from typing anything in chat", "Usage: !mute [username]", 1, Permission.VOICE_MUTE_OTHERS, "!mute");
    }

    public static Role getMuteRole() {
        for (Role role : Gilmore.getJDA().getGuilds().get(0).getRoles()) {
            if (role.getName().equals("Muted")) {
                return role;
            }
        }
        return null;
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {
        Role role = getMuteRole();

        try {
            User user = event.getJDA().getUsersByName(args[0]).get(0);

            if (event.getGuild().getRolesForUser(user).contains(role)) {
                channel.sendMessage(String.format("[%s] `'%s' has already been muted`", author.getAsMention(), args[0]));
                return;
            }

            event.getGuild().getManager().addRoleToUser(user, role).update();
            channel.sendMessage(String.format("[%s] `'%s' has been muted`", author.getAsMention(), args[0]));
        } catch (Exception ex) {
            channel.sendMessage(String.format("[%s] `Could not mute user '%s'`", author.getAsMention(), args[0]));
            Logging.log(ex);
        }

    }


}
