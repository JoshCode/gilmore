package nl.codefox.gilmore;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import nl.codefox.gilmore.listeners.ReadyListener;
import nl.codefox.gilmore.listeners.commands.GameListener;
import nl.codefox.gilmore.listeners.commands.VersionListener;

import javax.security.auth.login.LoginException;

/**
 * @author JoshCode
 */
public class Launcher {

    public static JDA JDA;
    private static final String dbms = "mysql";
    private static final String serverName = "localhost";
    private static final String portNumber = "3307";

    public static void main(String[] args) {
        JDABuilder jdaBuilder = new JDABuilder();

        jdaBuilder.setBotToken(System.getenv("GILMORE_BOT_TOKEN"));
        GameListener gl = new GameListener();
        jdaBuilder.addListener(new ReadyListener());
        jdaBuilder.addListener(new VersionListener());
        jdaBuilder.addListener(gl);

        try {
            JDA = jdaBuilder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
