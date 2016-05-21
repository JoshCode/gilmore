package nl.codefox.gilmore.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Properties;

public class GilmoreConfiguration 
{

	private final String CONFIGURATION_PATH = "";
	private static GilmoreConfiguration instance;
	
	private String databaseManagementSystem = "mysql";
	private String databaseHostname = "localhost";
	private Integer databasePort = 3306;

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
	private void setDatabaseManagmentSystem(String databaseManagementSystem)
	{
		this.databaseManagementSystem = databaseManagementSystem;
	}
	
	public String getDatabaseHostname()
	{
		return databaseHostname;
	}
	
	@GilmoreConfigurationItem(key = "db_hostname")
	private void setDatabaseHostname(String databaseHostname)
	{
		this.databaseHostname = databaseHostname;
	}
	
	public Integer getDatabasePort()
	{
		return databasePort;
	}
	
	@GilmoreConfigurationItem(key = "db_port")
	private void setDatabasePort(Integer databasePort)
	{
		this.databasePort = databasePort;
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
	
	private @interface GilmoreConfigurationItem
	{
		public String key();
	}
	
}
