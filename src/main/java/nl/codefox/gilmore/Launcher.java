package nl.codefox.gilmore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import nl.codefox.gilmore.listeners.GameNotifier;
import nl.codefox.gilmore.listeners.ReadyListener;
import nl.codefox.gilmore.listeners.commands.GameListener;
import nl.codefox.gilmore.listeners.commands.VersionListener;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

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

        loadSavedDB(jdaBuilder);

        jdaBuilder.addListener(new ReadyListener());
        jdaBuilder.addListener(new VersionListener());

        try {
            JDA = jdaBuilder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void loadSavedDB(JDABuilder jdaBuilder) {
        File gamesSave = new File("db/games.json");
        if (gamesSave.exists()) {
            Reader fr = null;
            try {
                fr = new FileReader(gamesSave);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            ArrayList<GameNotifier> games = gson.fromJson(fr, new TypeToken<ArrayList<GameNotifier>>(){}.getType());
            GameListener gl = new GameListener(games);
            jdaBuilder.addListener(gl);
        }
    }
}
