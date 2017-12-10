package nl.codefox.gilmore.util;

import nl.codefox.gilmore.config.GilmoreConfiguration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logging {

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss Z");

	public static void log(LogLevel level, String message) {
		GilmoreConfiguration config = GilmoreConfiguration.getInstance();

		String line = String.format("[%s\t%s] %s\n", level.name(), getTimestamp(), message);

		System.out.print(line);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(config.getLogLocation(), true))) {
			if (level == LogLevel.DEBUG && !config.isDebug()) {
				return;
			}

			writer.write(line);
		} catch (Exception ex) {
			System.out.println("Cannot log to file.");
			ex.printStackTrace();
		}
	}

	public static void debug(String message) {
		log(LogLevel.DEBUG, message);
	}

	public static void info(String message) {
		log(LogLevel.INFO, message);
	}

	public static void error(String message) {
		log(LogLevel.ERROR, message);
	}

	public static void log(Exception ex) {
		log(LogLevel.ERROR, ex.getClass().getCanonicalName() + ": " + ex.getMessage());
		for (StackTraceElement trace : ex.getStackTrace()) {
			log(LogLevel.ERROR, "\tat " + trace.toString());
		}
	}

	private static String getTimestamp() {
		return TIME_FORMAT.format(Calendar.getInstance().getTime());
	}

}
