package nl.codefox.gilmore.command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.util.Logging;

public class MuteCommand extends GilmoreCommand {

    public MuteCommand() {
        super("Stops a user from typing anything in chat", "Usage: !mute [username]", 2, Permission.VOICE_MUTE_OTHERS, "!mute");
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
    public void run(String[] args, MessageReceivedEvent event) {
        Role role = getMuteRole();

        try {
            User user = event.getJDA().getUsersByName(args[1]).get(0);

            if (event.getGuild().getRolesForUser(user).contains(role)) {
                event.getChannel().sendMessage(String.format("[%s] `'%s' has already been muted`", event.getAuthor().getAsMention(), args[1]));
                return;
            }

            event.getGuild().getManager().addRoleToUser(user, role).update();
            event.getChannel().sendMessage(String.format("[%s] `'%s' has been muted`", event.getAuthor().getAsMention(), args[1]));
        } catch (Exception ex) {
            event.getChannel().sendMessage(String.format("[%s] `Could not mute user '%s'`", event.getAuthor().getAsMention(), args[1]));
            Logging.log(ex);
        }

    }


}
