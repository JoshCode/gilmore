package nl.codefox.gilmore.command.game;

import nl.codefox.gilmore.command.GilmoreCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameAddSubCommand extends GilmoreCommand {

	private String selectedGame;

	public GameAddSubCommand() {
		this.addSubCommand(new GameAddSubSelectCommand(this));
		this.addSubCommand(new GameAddSubAddCommand(this));
	}

	@Override
	public String getUsage() {
		return "Usage: !game addsub [select, add]";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("!game addsub");
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public int getMaximumArguments() {
		return 100;
	}

	@Override
	public List<String> getRolePermission() {
		return Arrays.asList("Mod", "Administrator", "Server Owner");
	}

	public String getSelectedGame() {
		return selectedGame;
	}

	public void setSelectedGame(String selectedGame) {
		this.selectedGame = selectedGame;
	}
}
