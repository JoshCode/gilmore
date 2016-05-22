package nl.codefox.gilmore.command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.util.Logging;

public class UnmuteCommand extends GilmoreCommand
{
    
    public UnmuteCommand() 
    {
        super("Allows a muted to typing into chat", "Usage: !unmute [username]", 2, Permission.VOICE_MUTE_OTHERS, "!unmute");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) 
    {
        Role role = MuteCommand.getMuteRole();
        
        try
        {
            User user = event.getJDA().getUsersByName(args[1]).get(0);
         
            if(!event.getGuild().getRolesForUser(user).contains(role))
            {
                event.getChannel().sendMessage(String.format("[%s] `'%s' isn't currently muted`", event.getAuthor().getAsMention(), args[1]));
                return;
            }
            
            event.getGuild().getManager().removeRoleFromUser(user, role).update();
            event.getChannel().sendMessage(String.format("[%s] `'%s' has been unmuted`", event.getAuthor().getAsMention(), args[1]));
        }
        catch (Exception ex)
        {
            event.getChannel().sendMessage(String.format("[%s] `Could not unmute user '%s'`", event.getAuthor().getAsMention(), args[1]));
            Logging.log(ex);
        }
        
    }

    
    
}
