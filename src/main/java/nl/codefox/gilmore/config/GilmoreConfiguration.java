package nl.codefox.gilmore.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class GilmoreConfiguration {

	private static final String CONFIGURATION_PATH = "conf/gilmore.conf";
	private static GilmoreConfiguration instance;

	private String databaseManagementSystem = "mysql";
	private String databaseHostname = "localhost";
	private Integer databasePort = 3306;
	private String databasePassword = "password";
	private String databaseUsername = "username";
	private String databaseName = "GILMORE";
	private Boolean debug = false;
	private String logPath = "logs/gilmore.log";
	private File logLocation;
	private String botToken = "token";

	private GilmoreConfiguration() {
		String home = System.getProperty("user.home");
		logLocation = new File(home, logPath);
	}

	public static GilmoreConfiguration getInstance() {
		if (instance == null) {
			instance = new GilmoreConfiguration();
			new GilmoreConfigurationListener(CONFIGURATION_PATH);
			instance.load();
		}
		return instance;
	}

	public String getDatabaseManagementSystem() {
		return databaseManagementSystem;
	}

	@GilmoreConfigurationItem(key = "db_managmentsystem", type = String.class)
	public void setDatabaseManagmentSystem(String databaseManagementSystem) {
		this.databaseManagementSystem = databaseManagementSystem;
	}

	public String getDatabaseHostname() {
		return databaseHostname;
	}

	@GilmoreConfigurationItem(key = "db_hostname", type = String.class)
	public void setDatabaseHostname(String databaseHostname) {
		this.databaseHostname = databaseHostname;
	}

	public Integer getDatabasePort() {
		return databasePort;
	}

	@GilmoreConfigurationItem(key = "db_port", type = Integer.class)
	public void setDatabasePort(Integer databasePort) {
		this.databasePort = databasePort;
	}

	public String getDatabaseUsername() {
		return databaseUsername;
	}

	@GilmoreConfigurationItem(key = "db_username", type = String.class)
	public void getDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	@GilmoreConfigurationItem(key = "db_password", type = String.class)
	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	@GilmoreConfigurationItem(key = "db_name", type = String.class)
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public Boolean isDebug() {
		return debug;
	}

	@GilmoreConfigurationItem(key = "debug", type = Boolean.class)
	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public File getLogLocation() {
		return logLocation;
	}

	@GilmoreConfigurationItem(key = "log_location", type = String.class)
	public void setLogLocation(File logLocation) {
		this.logLocation = logLocation;
	}

	public String getBotToken() {
		return botToken;
	}

	@GilmoreConfigurationItem(key = "bot_token", type = String.class)
	public void setBotToken(String botToken) {
		this.botToken = botToken;
	}

	public void load() {

		try {
			String home = System.getProperty("user.home");
			File configurationFile = new File(home, CONFIGURATION_PATH);
			Properties properties = new Properties();
			properties.load(new FileReader(configurationFile));

			for (Method method : GilmoreConfiguration.class.getMethods()) {
				if (method.isAnnotationPresent(GilmoreConfigurationItem.class)) {
					GilmoreConfigurationItem item = method.getAnnotation(GilmoreConfigurationItem.class);

					if (properties.containsKey(item.key())) {
						if (item.type() == Integer.class) {
							Integer value = Integer.parseInt((String) properties.get(item.key()));
							method.invoke(this, value);
							System.out.println(String.format("[GilmoreConfiguration] %s(%s)", method.getName(), value));
							continue;
						} else if (item.type() == Boolean.class) {
							Boolean value = Boolean.parseBoolean((String) properties.get(item.key()));
							method.invoke(this, value);
							System.out.println(String.format("[GilmoreConfiguration] %s(%s)", method.getName(), value));
							continue;
						}

						String value = (String) properties.get(item.key());
						method.invoke(this, value);
						System.out.println(String.format("[GilmoreConfiguration] %s(%s)", method.getName(), value));
					}
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println(String.format("Configuration file '%s' not found, using default configuration values.", CONFIGURATION_PATH));
			ex.printStackTrace();
		} catch (Exception ex) {
			System.out.println(String.format("Exception when loading in configuration, using default configuration values.", CONFIGURATION_PATH));
			ex.printStackTrace();
		}


	}

}
