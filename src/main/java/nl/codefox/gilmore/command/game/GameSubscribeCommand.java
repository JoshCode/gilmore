package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

public class GameSubscribeCommand extends GilmoreCommand
{

    public GameSubscribeCommand() 
    {
        super("", "Usage: !game subscribe [name]", 3, 100, null, "!game subscribe");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event)
    {
        
        String name = StringUtil.arrayToString(args, 2, " ");
        
        if(!GameCommand.gameExists(name))
        {
            event.getChannel().sendMessage(String.format("[%s] `This game doesn't exist, but it could. Try !game create '%s'`", event.getAuthor().getAsMention(), name));
        }
        else
        {
            Game game = GameCommand.getGame(name);
            
            if(game.getInterestedUsers().contains(event.getAuthor().getId()))
            {
                event.getChannel().sendMessage(String.format("[%s] `You are already subscribed to the game '%s'`", event.getAuthor().getAsMention(), name));
                return;
            }
            
            game.addUser(event.getAuthor().getId());
            GilmoreDatabase.addSubscriber(name, event.getAuthor().getId());
            event.getChannel().sendMessage(String.format("[%s] `You are now subscribed to the game '%s'`", event.getAuthor().getAsMention(), name));
            
        }
        
    }

}
