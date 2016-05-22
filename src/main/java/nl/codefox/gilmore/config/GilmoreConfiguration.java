package nl.codefox.gilmore.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Properties;

import nl.codefox.gilmore.util.Logging;

public class GilmoreConfiguration 
{

    private final String CONFIGURATION_PATH = "C:\\Users\\patrick\\Documents\\Software\\gilmore.conf";
    private static GilmoreConfiguration instance;
    
    private String databaseManagementSystem = "mysql";
    private String databaseHostname = "localhost";
    private Integer databasePort = 3306;
    private String databasePassword = "password";
    private String databaseUsername = "username";
    private String databaseName = "GILMORE";
    private Boolean debug = false;
    private String logLocation = "~/logs/gilmore.log";
    private String botToken = "token";

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
    
    @GilmoreConfigurationItem(key = "db_managmentsystem", type = String.class)
    public void setDatabaseManagmentSystem(String databaseManagementSystem)
    {
        this.databaseManagementSystem = databaseManagementSystem;
    }
    
    public String getDatabaseHostname()
    {
        return databaseHostname;
    }
    
    @GilmoreConfigurationItem(key = "db_hostname", type = String.class)
    public void setDatabaseHostname(String databaseHostname)
    {
        this.databaseHostname = databaseHostname;
    }
    
    public Integer getDatabasePort()
    {
        return databasePort;
    }
    
    @GilmoreConfigurationItem(key = "db_port", type = Integer.class)
    public void setDatabasePort(Integer databasePort)
    {
        this.databasePort = databasePort;
    }
    
    public String getDatabaseUsername()
    {
        return databaseUsername;
    }
    
    @GilmoreConfigurationItem(key = "db_username", type = String.class)
    public void getDatabaseUsername(String databaseUsername)
    {
        this.databaseUsername = databaseUsername;
    }
    
    public String getDatabasePassword()
    {
        return databasePassword;
    }
    
    @GilmoreConfigurationItem(key = "db_password", type = String.class)
    public void setDatabasePassword(String databasePassword)
    {
        this.databasePassword = databasePassword;
    }
    
    public String getDatabaseName()
    {
        return databaseName;
    }
    
    @GilmoreConfigurationItem(key = "db_name", type = String.class)
    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }
    
    public Boolean isDebug()
    {
        return debug;
    }
    
    @GilmoreConfigurationItem(key = "debug", type = Boolean.class)
    public void setDebug(Boolean debug)
    {
        this.debug = debug;
    }
    
    public String getLogLocation()
    {
        return logLocation;
    }
    
    @GilmoreConfigurationItem(key = "log_location", type = String.class)
    public void setLogLocation(String logLocation)
    {
        this.logLocation = logLocation;
    }
    
    public String getBotToken()
    {
        return botToken;
    }
    
    @GilmoreConfigurationItem(key = "bot_token", type = String.class)
    public void setBotToken(String botToken)
    {
        this.botToken = botToken;
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
                        if(item.type() == Integer.class)
                        {
                            Integer value = Integer.parseInt((String) properties.get(item.key()));
                            method.invoke(this, value);
                            Logging.debug(String.format("[GilmoreConfiguration] %s(%s)", method.getName(), value), true);
                            continue;
                        }
                        else if(item.type() == Boolean.class)
                        {
                            Boolean value = Boolean.parseBoolean((String) properties.get(item.key()));
                            method.invoke(this, value);
                            Logging.debug(String.format("[GilmoreConfiguration] %s(%s)", method.getName(), value), true);
                            continue;
                        }

                        String value = (String) properties.get(item.key());
                        method.invoke(this, value);
                        Logging.debug(String.format("[GilmoreConfiguration] %s(%s)", method.getName(), value), true);
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
