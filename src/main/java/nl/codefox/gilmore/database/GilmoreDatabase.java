package nl.codefox.gilmore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import nl.codefox.gilmore.config.GilmoreConfiguration;

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
				
				connection = DriverManager.getConnection(
						String.format("jdbc:%s://%s:%s/%s?useSSL=false", config.getDatabaseManagmentSystem(), config.getDatabaseHostname(),
															config.getDatabasePort(), config.getDatabaseName()),
						config.getDatabaseUsername(), config.getDatabasePassword()
				);
			}
			
			return connection;
		}
		catch (Exception ex)
		{
			System.out.println("Could not make connection to database.");
			ex.printStackTrace();
			return null;
		}
	}
	
}
