package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.ArrayUtil;

public class GameUnsubscribeCommand extends GilmoreCommand
{

    public GameUnsubscribeCommand() 
    {
        super("", "Usage: !game unsubscribe [name]", 3, 100, null, "!game unsubscribe");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event)
    {
        
        String name = ArrayUtil.arrayToString(args, 2, " ");
        
        if(!GameCommand.gameExists(name))
        {
            event.getChannel().sendMessage(String.format("[%s] `This game doesn't exist. Try !game list to get a list of available games", event.getAuthor().getAsMention(), name));
        }
        else
        {
            Game game = GameCommand.getGame(name);
            
            if(!game.getInterestedUsers().contains(event.getAuthor().getId()))
            {
                event.getChannel().sendMessage(String.format("[%s] `You are not subscribed to the game '%s'`", event.getAuthor().getAsMention(), name));
                return;
            }
            
            game.removeUser(event.getAuthor().getId());
            GilmoreDatabase.removeSubscriber(name, event.getAuthor().getId());
            event.getChannel().sendMessage(String.format("[%s] `You are now unsubscribed from the game '%s'`", event.getAuthor().getAsMention(), name));
        }
        
    }

}
