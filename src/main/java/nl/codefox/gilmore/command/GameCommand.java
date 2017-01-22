package nl.codefox.gilmore.command;

import nl.codefox.gilmore.command.game.Game;
import nl.codefox.gilmore.command.game.GameCreateCommand;
import nl.codefox.gilmore.command.game.GameHostCommand;
import nl.codefox.gilmore.command.game.GameListCommand;
import nl.codefox.gilmore.command.game.GameRemoveCommand;
import nl.codefox.gilmore.command.game.GameSubscribeCommand;
import nl.codefox.gilmore.command.game.GameUnsubscribeCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameCommand extends GilmoreCommand {

    private static Map<String, Game> games = new HashMap<String, Game>();

    public GameCommand() {
        this.addSubCommand(new GameListCommand());
        this.addSubCommand(new GameCreateCommand());
        this.addSubCommand(new GameRemoveCommand());
        this.addSubCommand(new GameHostCommand());
        this.addSubCommand(new GameSubscribeCommand());
        this.addSubCommand(new GameUnsubscribeCommand());
        load();
    }

    @Override
    public String getUsage() {
        return "Usage: !game [list, create, remove, host, subscribe, unsubscribe]";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("!game");
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return 100;
    }

    public static void addGames(List<Game> games) {
        games.forEach(game -> addGame(game));
    }

    public static void addGame(Game game) {
        getGames().put(game.getName().toLowerCase(), game);
    }

    public static void removeGame(Game game) {
        getGames().remove(game.getName().toLowerCase());
    }

    public static Map<String, Game> getGames() {
        return GameCommand.games;
    }

    public static void load() {
        addGames(GilmoreDatabase.getGames());
    }

    public static Game getGame(String name) {
        return getGames().getOrDefault(name.toLowerCase(), null);
    }

    public static boolean gameExists(String name) {
        return getGames().containsKey(name.toLowerCase());
    }
}
