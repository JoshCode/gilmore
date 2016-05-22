package nl.codefox.gilmore.config;

import java.io.File;

import nl.codefox.gilmore.util.Logging;

public class GilmoreConfigurationListener extends Thread 
{

    private File file;
    private Long lastModifiedTime;
    
    public GilmoreConfigurationListener(String file)
    {
        this.file = new File(file);
        this.lastModifiedTime = this.file.lastModified();
        start();
    }
    
    public void run()
    {
        try
        {
            while(true)
            {
                
                if(lastModifiedTime < file.lastModified())
                {
                    lastModifiedTime = file.lastModified();
                    Logging.info("Reloading configuration - looks like it changed.");
                    GilmoreConfiguration.getInstance().load();
                }
                
                Thread.sleep(1000L);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
