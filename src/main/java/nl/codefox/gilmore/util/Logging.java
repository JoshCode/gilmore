package nl.codefox.gilmore.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import nl.codefox.gilmore.config.GilmoreConfiguration;

public class Logging
{

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss Z");

    public static void log(LogLevel level, String message)
    {
        log(level, message, false);
    }
    
    public static void log(LogLevel level, String message, boolean console)
    {
        GilmoreConfiguration config = GilmoreConfiguration.getInstance();
        
        String line = String.format("[%s\t%s] %s\n", level.name(), getTimestamp(), message);
        
        if(console)
        {
            System.out.print(line);
        }
        else
        {   
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(config.getLogLocation(), true)))
            {
                if(level == LogLevel.DEBUG && !config.isDebug())
                {
                    return;
                }
                
                writer.write(line);
            }
            catch (Exception ex)
            {
                System.out.println("Cannot log to file.");
                ex.printStackTrace();
            }
        }
    }
    
    public static void debug(String message)
    {
        log(LogLevel.DEBUG, message);
    }
    
    public static void debug(String message, boolean console)
    {
        log(LogLevel.DEBUG, message, console);
    }
    
    public static void info(String message)
    {
        log(LogLevel.INFO, message);
    }
    
    public static void info(String message, boolean console)
    {
        log(LogLevel.INFO, message, console);
    }
    
    public static void error(String message)
    {
        log(LogLevel.ERROR, message);
    }
    
    public static void error(String message, boolean console)
    {
        log(LogLevel.ERROR, message, console);
    }
    
    public static void log(Exception ex)
    {
        log(LogLevel.ERROR, ex.getClass().getCanonicalName() + ": " + ex.getMessage());
        for(StackTraceElement trace : ex.getStackTrace())
        {
            log(LogLevel.ERROR, "\tat " + trace.toString());
        }
    }
    
    private static String getTimestamp()
    {
        return TIME_FORMAT.format(Calendar.getInstance().getTime());
    }
    
}
