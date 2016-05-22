package nl.codefox.gilmore.command;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.game.Game;
import nl.codefox.gilmore.command.game.GameCreateCommand;
import nl.codefox.gilmore.command.game.GameHostCommand;
import nl.codefox.gilmore.command.game.GameListCommand;
import nl.codefox.gilmore.command.game.GameRemoveCommand;
import nl.codefox.gilmore.command.game.GameSubscribeCommand;
import nl.codefox.gilmore.command.game.GameUnsubscribeCommand;

import java.util.ArrayList;

public class GameCommand extends GilmoreCommand 
{

    private static ArrayList<Game> games = new ArrayList<>();
    
    public GameCommand() 
    {
        super("", "Usage: !game [list|create|remove]", 2, 100, null, "!game");
        load();
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) 
    {
        
        String verb = args[1];
        
        switch(verb)
        {
            case "list":
                runList(args, event);   break;
            case "create":
                runCreate(args, event); break;
            case "remove":
                runRemove(args, event); break;
            case "host":
                runHost(args, event); break;
            case "subscribe":
                runSubscribe(args, event); break;
            case "unsubscribe":
                runUnsubscribe(args, event); break;
        }
        
    }

    private void runList(String[] args, MessageReceivedEvent event) 
    {
        new GameListCommand().process(args, event);
    }

    private void runCreate(String[] args, MessageReceivedEvent event) 
    {       
        new GameCreateCommand().process(args, event);
    }

    private void runRemove(String[] args, MessageReceivedEvent event) 
    {      
        new GameRemoveCommand().process(args, event);
    }

    private void runHost(String[] args, MessageReceivedEvent event) 
    {
        new GameHostCommand().process(args, event);
    }

    private void runSubscribe(String[] args, MessageReceivedEvent event) 
    {
        new GameSubscribeCommand().process(args, event);
    }

    private void runUnsubscribe(String[] args, MessageReceivedEvent event) 
    {
        new GameUnsubscribeCommand().process(args, event);
    }
    
    public static void addGames(ArrayList<Game> games)
    {
        getGames().addAll(games);
    }
    
    public static void addGame(Game game)
    {
        getGames().add(game);
    }
    
    public static void removeGame(Game game)
    {
        getGames().remove(game);
    }
    
    public static ArrayList<Game> getGames()
    {
        return GameCommand.games;
    }
    
    public static void save()
    {
        // TODO
    }
    
    public static void load()
    {
        
    }
    
    public static Game getGame(String name)
    {
        for(Game game : getGames())
        {
            if(game.getName().equals(name))
            {
                return game;
            }
        }
        return null;
    }

    public static boolean gameExists(String name) 
    {
        if (getGames().isEmpty()) { return false; }

        for (Game game : getGames()) 
        {
            if (game.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

}
