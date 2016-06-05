package nl.codefox.gilmore.command;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import nl.codefox.gilmore.command.game.Game;
import nl.codefox.gilmore.command.game.GameCreateCommand;
import nl.codefox.gilmore.command.game.GameHostCommand;
import nl.codefox.gilmore.command.game.GameListCommand;
import nl.codefox.gilmore.command.game.GameRemoveCommand;
import nl.codefox.gilmore.command.game.GameSubscribeCommand;
import nl.codefox.gilmore.command.game.GameUnsubscribeCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;

import java.util.ArrayList;
import java.util.List;

public class GameCommand extends GilmoreCommand {

    private static ArrayList<Game> games = new ArrayList<>();

    public GameCommand() {
        super("", "Usage: !game [list|create|remove|host|subscribe|unsubscribe]", 1, 100, null, "!game");
        load();
    }

    public static void addGames(List<Game> games) {
        getGames().addAll(games);
    }

    public static void addGame(Game game) {
        getGames().add(game);
    }

    public static void removeGame(Game game) {
        getGames().remove(game);
    }

    public static ArrayList<Game> getGames() {
        return GameCommand.games;
    }

    public static void load() {
        addGames(GilmoreDatabase.getGames());
    }

    public static Game getGame(String name) {
        for (Game game : getGames()) {
            if (game.getName().equals(name)) {
                return game;
            }
        }
        return null;
    }

    public static boolean gameExists(String name) {
        if (getGames().isEmpty()) {
            return false;
        }

        for (Game game : getGames()) {
            if (game.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(String command, String[] args, TextChannel channel, User author, MessageReceivedEvent event) {

        String verb = args[0];

        switch (verb) {
            case "list":
                new GameListCommand().runCommand(command, args, channel, author, event);
                break;
            case "create":
                new GameCreateCommand().runCommand(command, args, channel, author, event);
                break;
            case "remove":
                new GameRemoveCommand().runCommand(command, args, channel, author, event);
                break;
            case "host":
                new GameHostCommand().runCommand(command, args, channel, author, event);
                break;
            case "subscribe":
                new GameSubscribeCommand().runCommand(command, args, channel, author, event);
                break;
            case "unsubscribe":
                new GameUnsubscribeCommand().runCommand(command, args, channel, author, event);
                break;
            default:
                invalidUsage(command, args, channel, author, event);
                break;
        }

    }

}
