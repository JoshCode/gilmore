package nl.codefox.gilmore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.codefox.gilmore.command.game.Game;
import nl.codefox.gilmore.config.GilmoreConfiguration;
import nl.codefox.gilmore.util.Logging;
import nl.codefox.gilmore.util.Resources;

public class GilmoreDatabase
{

    private static Connection connection;

    private static Connection getConnection()
    {
        try
        {
            GilmoreConfiguration config = GilmoreConfiguration.getInstance();

            if(connection == null || connection.isClosed())
            {

                Logging.debug("Attempting connection to database...");
                Logging.debug("\tdatabaseManagementSystem = " + config.getDatabaseManagmentSystem());
                Logging.debug("\tdatabaseHostname = " + config.getDatabaseHostname());
                Logging.debug("\tdatabasePort = " + config.getDatabasePort());
                Logging.debug("\tdatabaseName = " + config.getDatabaseName());
                Logging.debug("\tdatabaseUsername = " + config.getDatabaseUsername());
                Logging.debug("\tdatabasePassword = " + config.getDatabasePassword());

                connection = DriverManager.getConnection(
                        String.format("jdbc:%s://%s:%d/%s?useSSL=false", config.getDatabaseManagmentSystem(), config.getDatabaseHostname(),
                                                                         config.getDatabasePort(), config.getDatabaseName()),
                        config.getDatabaseUsername(), config.getDatabasePassword()
                );

                Logging.debug("Successful connection to database!");
            }

            Logging.debug("Returning connection to database");
            return connection;
        }
        catch (Exception ex)
        {
            Logging.error("Could not make connection to the database");
            Logging.log(ex);
            return null;
        }
    }

    public static List<Game> getGames()
    {
        List<Game> games = new ArrayList<Game>();

        try
        {
            PreparedStatement stmt = getConnection().prepareStatement(Resources.getSQL("SELECT_GAMES"));
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                Game game = new Game(rs.getString(1));

                PreparedStatement stmt2 = getConnection().prepareStatement(Resources.getSQL("SELECT_SUBSCRIBERS"));
                stmt2.setString(1, game.getName());
                ResultSet rs2 = stmt2.executeQuery();

                while(rs2.next())
                {
                    game.addUser(rs2.getString(1));
                }

                games.add(game);
            }
        }
        catch (Exception ex)
        {
            Logging.error("Could not get games from the database");
            Logging.log(ex);
        }

        return games;
    }

    public static void addGame(String name)
    {
        try
        {

            PreparedStatement stmt = getConnection().prepareStatement(Resources.getSQL("INSERT_GAME"));
            stmt.setString(1, name);

            stmt.executeUpdate();
            Logging.info("Added game '" + name + "' to the database");

        }
        catch (Exception ex)
        {
            Logging.error("Could not add game to the database");
            Logging.log(ex);
        }
    }

    public static void addSubscriber(String game, String user)
    {
        try
        {

            PreparedStatement stmt = getConnection().prepareStatement(Resources.getSQL("INSERT_SUBSCRIBER"));
            stmt.setString(1, game);
            stmt.setString(2, user);

            stmt.executeUpdate();
            Logging.debug("Added subscriber '" + user + "' for game '" + game + "' to the database");

        }
        catch (Exception ex)
        {
            Logging.error("Could not add subscriber to the database");
            Logging.log(ex);
        }
    }

    public static void removeGame(String name)
    {

        try
        {
            PreparedStatement subs = getConnection().prepareStatement(Resources.getSQL("DELETE_SUBSCRIBERS"));
            subs.setString(1, name);
            subs.executeUpdate();

            PreparedStatement game = getConnection().prepareStatement(Resources.getSQL("DELETE_GAME"));
            game.setString(1, name);
            game.executeUpdate();
        }
        catch (Exception ex)
        {
            Logging.error("Could not delete games from the database");
            Logging.log(ex);
        }
    }

    public static void removeSubscriber(String name, String user)
    {

        try
        {
            PreparedStatement subs = getConnection().prepareStatement(Resources.getSQL("DELETE_SUBSCRIBERS"));
            subs.setString(1, name);
            subs.setString(2, user);
            subs.executeUpdate();
        }
        catch (Exception ex)
        {
            Logging.error("Could not delete subscriber from the database");
            Logging.log(ex);
        }
    }

    public static void addCommand(String command, String description)
    {
        try
        {
            PreparedStatement customCommand = getConnection().prepareStatement(Resources.getSQL("INSERT_COMMAND"));
            customCommand.setString(1, command);
            customCommand.setString(2, description);
            customCommand.executeUpdate();
        }
        catch (Exception ex)
        {
            Logging.error("Could not add command to the database");
            Logging.log(ex);
        }
    }

    public static void editCommand(String command, String description)
    {
        try
        {
            PreparedStatement customCommand = getConnection().prepareStatement(Resources.getSQL("UPDATE_COMMAND"));
            customCommand.setString(1, description);
            customCommand.setString(2, command);
            customCommand.executeUpdate();
        }
        catch (Exception ex)
        {
            Logging.error("Could not edit command to the database");
            Logging.log(ex);
        }
    }

    public static Map<String, String> getCommands()
    {
        Map<String, String> commands = new HashMap<>();

        try
        {
            PreparedStatement stmt = getConnection().prepareStatement(Resources.getSQL("SELECT_COMMAND"));
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                commands.put(rs.getString(1),rs.getString(2));
            }
        }
        catch (Exception ex)
        {
            Logging.error("Could not get games from the database");
            Logging.log(ex);
        }
        return commands;
    }

    public static void deleteCommand(String command) 
    {
        try
        {
            PreparedStatement subs = getConnection().prepareStatement(Resources.getSQL("DELETE_SUBSCRIBERS"));
            subs.setString(1, command);
            subs.executeUpdate();
        }
        catch (Exception ex)
        {
            Logging.error("Could not delete the custom command from the database");
            Logging.log(ex);
        }
    }
}
