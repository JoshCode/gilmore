package nl.codefox.gilmore.command;

import nl.codefox.gilmore.command.game.*;
import nl.codefox.gilmore.database.GilmoreDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameCommand extends GilmoreCommand {

	private static ArrayList<Game> games = new ArrayList<>();

	public GameCommand() {
		this.addSubCommand(new GameListCommand());
		this.addSubCommand(new GameCreateCommand());
		this.addSubCommand(new GameRemoveCommand());
		this.addSubCommand(new GameHostCommand());
		this.addSubCommand(new GameSubscribeCommand());
		this.addSubCommand(new GameUnsubscribeCommand());
		this.addSubCommand(new GameSubscribedCommand());
		this.addSubCommand(new GameAddSubCommand());
		load();
	}

	@Override
	public String getUsage() {
		return "!game [list, create, remove, host, subscribe, unsubscribe, addsub]";
	}

	@Override
	public String getDescription() {
		return "Command to help play games with people of similar interests";
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("!game");
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
			if (game.getName().toLowerCase().equals(name.toLowerCase())) {
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
			if (game.getName().toLowerCase().equals(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
