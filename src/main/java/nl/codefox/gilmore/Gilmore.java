package nl.codefox.gilmore;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

import nl.codefox.gilmore.command.AboutCommand;
import nl.codefox.gilmore.command.CriticalRoleCommand;
import nl.codefox.gilmore.command.CustomCommand;
import nl.codefox.gilmore.command.DiceCommand;
import nl.codefox.gilmore.command.GameCommand;
import nl.codefox.gilmore.command.HelpCommand;
import nl.codefox.gilmore.command.MuteCommand;
import nl.codefox.gilmore.command.UnmuteCommand;
import nl.codefox.gilmore.command.PermissionsCommand;
import nl.codefox.gilmore.config.GilmoreConfiguration;
import nl.codefox.gilmore.listener.ChannelListener;
import nl.codefox.gilmore.listener.ConnectionListener;
import nl.codefox.gilmore.util.Logging;

import java.io.File;
import java.io.IOException;

import javax.security.auth.login.LoginException;

public class Gilmore {

    private static JDA JDA;
    private static ChannelListener commandListener;
    private static ConnectionListener connectionListener;

    public static void main(String[] args) {

        try {
            Logging.info("Gilmore starting up!");
            GilmoreConfiguration config = GilmoreConfiguration.getInstance();

            File logLocation = config.getLogLocation();
            new File(logLocation.getParent()).mkdirs();
            try {
                logLocation.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JDABuilder builder = new JDABuilder();

            builder.setBotToken(config.getBotToken());

            commandListener = new ChannelListener()
                    .registerCommand(new AboutCommand())
                    .registerCommand(new GameCommand())
                    .registerCommand(new DiceCommand())
                    .registerCommand(new HelpCommand())
                    .registerCommand(new MuteCommand())
                    .registerCommand(new UnmuteCommand())
                    .registerCommand(new HelpCommand())
                    .registerCommand(new CustomCommand())
                    .registerCommand(new CriticalRoleCommand())
                    .registerCommand(new PermissionsCommand());

            connectionListener = new ConnectionListener();

            builder.addListener(commandListener);
            builder.addListener(connectionListener);

            JDA = builder.buildBlocking();

            JDA.getAccountManager().setGame("with your hearts");

        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static JDA getJDA() {
        return JDA;
    }

    public static ChannelListener getCommandListener() {
        return commandListener;
    }
}
