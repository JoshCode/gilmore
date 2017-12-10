package nl.codefox.gilmore.command.game;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.database.GilmoreDatabase;
import nl.codefox.gilmore.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
        return Arrays.asList("!game addsub");
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
