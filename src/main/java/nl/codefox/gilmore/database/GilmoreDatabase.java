package nl.codefox.gilmore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import nl.codefox.gilmore.config.GilmoreConfiguration;
import nl.codefox.gilmore.util.Logging;

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
    
    public static void main(String[] args)
    {
        getConnection();
    }
    
}
