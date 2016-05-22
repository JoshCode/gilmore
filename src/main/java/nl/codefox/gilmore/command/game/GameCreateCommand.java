package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

public class GameCreateCommand extends GilmoreCommand
{

    public GameCreateCommand() 
    {
        super("", "Usage: !game create [name]", 3, 100, null, "!game create");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event)
    {
        
        String name = StringUtil.arrayToString(args, 2, " ");
        
        if(GameCommand.gameExists(name))
        {
            event.getChannel().sendMessage(String.format("[%s] `This game already exists, try !game subscribe " + name + "`", event.getAuthor().getAsMention(), name));
        }
        else
        {
            
            GilmoreDatabase.addGame(name);
            GilmoreDatabase.addSubscriber(name, event.getAuthor().getId());
            GameCommand.addGame(new Game(name, event.getAuthor().getId()));
            event.getChannel().sendMessage(String.format("[%s] `The game '%s' has been created and you are subscribed to it.`", event.getAuthor().getAsMention(), name));
            
        }
        
    }

}
