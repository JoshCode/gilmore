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

                Logging.debug("[GilmoreDatabase] Attempting connection to database...");
                Logging.debug("[GilmoreDatabase] \tdatabaseManagementSystem = " + config.getDatabaseManagmentSystem());
                Logging.debug("[GilmoreDatabase] \tdatabaseHostname = " + config.getDatabaseHostname());
                Logging.debug("[GilmoreDatabase] \tdatabasePort = " + config.getDatabasePort());
                Logging.debug("[GilmoreDatabase] \tdatabaseName = " + config.getDatabaseName());
                Logging.debug("[GilmoreDatabase] \tdatabaseUsername = " + config.getDatabaseUsername());
                Logging.debug("[GilmoreDatabase] \tdatabasePassword = " + config.getDatabasePassword());

                connection = DriverManager.getConnection(
                        String.format("jdbc:%s://%s:%d/%s?useSSL=false", config.getDatabaseManagmentSystem(), config.getDatabaseHostname(),
                                                                         config.getDatabasePort(), config.getDatabaseName()),
                        config.getDatabaseUsername(), config.getDatabasePassword()
                );

                Logging.debug("[GilmoreDatabase] Successful connection to database!");
            }

            Logging.debug("[GilmoreDatabase] Returning connection to database");
            return connection;
        }
        catch (Exception ex)
        {
            Logging.error("[GilmoreDatabase] [GilmoreDatabase] Could not make connection to the database");
            Logging.log(ex);
            return null;
        }
    }

    public static List<Game> getGames()
    {
        Logging.debug("[GilmoreDatabase] Getting games from database");
        List<Game> games = new ArrayList<Game>();

        try
        {
            PreparedStatement stmt = getConnection().prepareStatement(Resources.getSQL("SELECT_GAMES"));
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                Game game = new Game(rs.getString(1));

                Logging.debug("[GilmoreDatabase] Getting subscribers from database");
                PreparedStatement stmt2 = getConnection().prepareStatement(Resources.getSQL("SELECT_SUBSCRIBERS"));
                stmt2.setString(1, game.getName());
                ResultSet rs2 = stmt2.executeQuery();

                while(rs2.next())
                {
                    game.addUser(rs2.getString(1));
                }

                Logging.debug("[GilmoreDatabase] Finished getting subscribers from database");

                games.add(game);
            }
            
            Logging.debug("[GilmoreDatabase] Finished getting games from database");
        }
        catch (Exception ex)
        {
            Logging.error("[GilmoreDatabase] Could not get games from the database");
            Logging.log(ex);
        }

        return games;
    }

    public static void addGame(String name)
    {
        Logging.debug("[GilmoreDatabase] Adding game to database");
        try
        {
            PreparedStatement stmt = getConnection().prepareStatement(Resources.getSQL("INSERT_GAME"));
            stmt.setString(1, name);

            stmt.executeUpdate();
            Logging.debug("[GilmoreDatabase] Finished adding game to database");

        }
        catch (Exception ex)
        {
            Logging.error("[GilmoreDatabase] Could not add game to the database");
            Logging.log(ex);
        }
    }

    public static void addSubscriber(String game, String user)
    {
        Logging.debug("[GilmoreDatabase] Adding subscriber to database");
        try
        {
            PreparedStatement stmt = getConnection().prepareStatement(Resources.getSQL("INSERT_SUBSCRIBER"));
            stmt.setString(1, game);
            stmt.setString(2, user);

            stmt.executeUpdate();
            Logging.debug("[GilmoreDatabase] Finished adding susbcriber to database");

        }
        catch (Exception ex)
        {
            Logging.error("[GilmoreDatabase] Could not add subscriber to the database");
            Logging.log(ex);
        }
    }

    public static void removeGame(String name)
    {
        try
        {
            Logging.debug("[GilmoreDatabase] Removing subscribers from database");
            PreparedStatement subs = getConnection().prepareStatement(Resources.getSQL("DELETE_SUBSCRIBERS"));
            subs.setString(1, name);
            subs.executeUpdate();
            Logging.debug("[GilmoreDatabase] Finished removing subscribers from database");

            Logging.debug("[GilmoreDatabase] Removing game from database");
            PreparedStatement game = getConnection().prepareStatement(Resources.getSQL("DELETE_GAME"));
            game.setString(1, name);
            game.executeUpdate();
            Logging.debug("[GilmoreDatabase] Finished removing game from database");
        }
        catch (Exception ex)
        {
            Logging.error("[GilmoreDatabase] Could not delete games from the database");
            Logging.log(ex);
        }
    }

    public static void removeSubscriber(String name, String user)
    {

        Logging.debug("[GilmoreDatabase] Removing subscriber from database");
        try
        {
            PreparedStatement subs = getConnection().prepareStatement(Resources.getSQL("DELETE_SUBSCRIBERS"));
            subs.setString(1, name);
            subs.setString(2, user);
            subs.executeUpdate();
            Logging.debug("[GilmoreDatabase] Finished removing subscriber from database");
        }
        catch (Exception ex)
        {
            Logging.error("[GilmoreDatabase] Could not delete subscriber from the database");
            Logging.log(ex);
        }
    }

    public static void addCommand(String command, String description)
    {
        Logging.debug("[GilmoreDatabase] Adding command on database");
        try
        {
            PreparedStatement customCommand = getConnection().prepareStatement(Resources.getSQL("INSERT_COMMAND"));
            customCommand.setString(1, command);
            customCommand.setString(2, description);
            customCommand.executeUpdate();
            Logging.debug("[GilmoreDatabase] Finished adding command on database");
        }
        catch (Exception ex)
        {
            Logging.error("[GilmoreDatabase] Could not add command to the database");
            Logging.log(ex);
        }
    }

    public static void editCommand(String command, String description)
    {
        Logging.debug("[GilmoreDatabase] Editing command on database");
        try
        {
            PreparedStatement customCommand = getConnection().prepareStatement(Resources.getSQL("UPDATE_COMMAND"));
            customCommand.setString(1, description);
            customCommand.setString(2, command);
            customCommand.executeUpdate();
            Logging.debug("[GilmoreDatabase] Finished editing command on database");
        }
        catch (Exception ex)
        {
            Logging.error("[GilmoreDatabase] Could not edit command to the database");
            Logging.log(ex);
        }
    }

    public static Map<String, String> getCommands()
    {
        Map<String, String> commands = new HashMap<>();
        Logging.debug("[GilmoreDatabase] Getting commands from database");
        
        try
        {
            PreparedStatement stmt = getConnection().prepareStatement(Resources.getSQL("SELECT_COMMANDS"));
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                commands.put(rs.getString(1),rs.getString(2));
            }
            Logging.debug("[GilmoreDatabase] Done getting commands from database");
        }
        catch (Exception ex)
        {
            Logging.error("[GilmoreDatabase] Could not get games from the database");
            Logging.log(ex);
        }
        return commands;
    }

    public static void deleteCommand(String command) 
    {
        Logging.debug("[GilmoreDatabase] Deleting command from database");
        try
        {
            PreparedStatement subs = getConnection().prepareStatement(Resources.getSQL("DELETE_SUBSCRIBERS"));
            subs.setString(1, command);
            subs.executeUpdate();

            Logging.debug("[GilmoreDatabase] Finished deleting command from database");
        }
        catch (Exception ex)
        {
            Logging.error("[GilmoreDatabase] Could not delete the custom command from the database");
            Logging.log(ex);
        }
    }
}
