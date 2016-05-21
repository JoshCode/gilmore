package nl.codefox.gilmore.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Properties;

public class GilmoreConfiguration 
{

	private final String CONFIGURATION_PATH = "C:\\Users\\patrick\\Documents\\Software\\gilmore.conf";
	private static GilmoreConfiguration instance;
	
	private String databaseManagementSystem = "mysql";
	private String databaseHostname = "localhost";
	private String databasePort = "3306";
	private String databasePassword = "password";
	private String databaseUsername = "username";
	private String databaseName = "GILMORE";

	private GilmoreConfiguration() { }
	
	public static GilmoreConfiguration getInstance()
	{
		if(instance == null)
		{
			instance = new GilmoreConfiguration();
			instance.load();
		}
		
		return instance;
	}
	
	public String getDatabaseManagmentSystem()
	{
		return databaseManagementSystem;
	}
	
	@GilmoreConfigurationItem(key = "db_managmentsystem")
	public void setDatabaseManagmentSystem(String databaseManagementSystem)
	{
		this.databaseManagementSystem = databaseManagementSystem;
	}
	
	public String getDatabaseHostname()
	{
		return databaseHostname;
	}
	
	@GilmoreConfigurationItem(key = "db_hostname")
	public void setDatabaseHostname(String databaseHostname)
	{
		this.databaseHostname = databaseHostname;
	}
	
	public String getDatabasePort()
	{
		return databasePort;
	}
	
	@GilmoreConfigurationItem(key = "db_port")
	public void setDatabasePort(String databasePort)
	{
		this.databasePort = databasePort;
	}
	
	public String getDatabaseUsername()
	{
		return databaseUsername;
	}
	
	@GilmoreConfigurationItem(key = "db_username")
	public void getDatabaseUsername(String databaseUsername)
	{
		this.databaseUsername = databaseUsername;
	}
	
	public String getDatabasePassword()
	{
		return databasePassword;
	}
	
	@GilmoreConfigurationItem(key = "db_password")
	public void setDatabasePassword(String databasePassword)
	{
		this.databasePassword = databasePassword;
	}
	
	public String getDatabaseName()
	{
		return databaseName;
	}
	
	@GilmoreConfigurationItem(key = "db_name")
	public void setDatabaseName(String databaseName)
	{
		this.databaseName = databaseName;
	}
	
	public void load()
	{
		
		try
		{
			Properties properties = new Properties();
			properties.load(new FileReader(CONFIGURATION_PATH));
			
			for(Method method : GilmoreConfiguration.class.getMethods())
			{
				if(method.isAnnotationPresent(GilmoreConfigurationItem.class))
				{
					GilmoreConfigurationItem item = method.getAnnotation(GilmoreConfigurationItem.class);
					
					if(properties.containsKey(item.key()))
					{
						method.invoke(this, properties.get(item.key()));
					}
				}
			}
		}
		catch (FileNotFoundException ex)
		{
			System.out.println(String.format("Configuration file '%s' not found, using default configuration values.", CONFIGURATION_PATH));
			ex.printStackTrace();
		}
		catch (Exception ex)
		{
			System.out.println(String.format("Exception when loading in configuration, using default configuration values.", CONFIGURATION_PATH));
			ex.printStackTrace();
		}
	}
	
}
