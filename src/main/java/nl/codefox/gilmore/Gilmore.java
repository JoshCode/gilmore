package nl.codefox.gilmore;

import flexjson.JSONDeserializer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import nl.codefox.gilmore.command.HistoryCommand;
import nl.codefox.gilmore.config.GilmoreConfiguration;
import nl.codefox.gilmore.listener.ChannelListener;
import nl.codefox.gilmore.listener.ConnectionListener;
import nl.codefox.gilmore.util.Logging;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Gilmore {

	private static JDA JDA_INSTANCE;
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

			JDABuilder builder = new JDABuilder(AccountType.BOT);

			builder.setToken(config.getBotToken());

			commandListener = new ChannelListener()
					.registerCommand(new HistoryCommand());

			connectionListener = new ConnectionListener();

			builder.addListener(commandListener);
			builder.addListener(connectionListener);

			JDA_INSTANCE = builder.buildBlocking();

			JDA_INSTANCE.getPresence().setGame(Game.of("with your hearts"));

		} catch (LoginException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RateLimitedException e) {
			e.printStackTrace();
		}

	}

	public static JDA getJDA() {
		return JDA_INSTANCE;
	}

	public static ChannelListener getCommandListener() {
		return commandListener;
	}
}
