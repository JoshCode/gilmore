package nl.codefox.gilmore.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Resources {

    private static Map<String, String> SQL = new HashMap<String, String>();
    private static Font font;
    private static final String FONT_URL = "http://gilmore.prscampbell.com/cocogoose.otf";

    static
    {
        loadSQL();
        loadFont();
    }
    
    private static void loadSQL()
    {
        Logging.debug("[Resources] Getting SQL");
        try
        {
            ClassLoader loader = Resources.class.getClassLoader();
            InputStream in = loader.getResourceAsStream("sql/");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) 
            {
                try(BufferedReader fr = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("sql/" + line))))
                {
                    String sql = "";
                    String sqlLine;
                    while ((sqlLine = fr.readLine()) != null) 
                    {
                        sql += sqlLine + "\n";
                    }
                    
                    SQL.put(line, sql);
                    Logging.debug("[Resources] SQL '" + line + "' loaded in");
                }
            }
            br.close();
            Logging.debug("[Resources] " + SQL.size() + " SQL statements have been loaded in.");
        }
        catch (Exception ex)
        {
            Logging.log(ex);
        }
    }
    
    private static void loadFont()
    {      
        Logging.debug("[Resources] Getting font from '" + FONT_URL + "'");
        try
        {
            URL fontUrl = new URL(FONT_URL);
            font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            Logging.debug("[Resources] Font loaded!");
        }
        catch (Exception ex)
        {
            Logging.log(ex);
        }
    }

    public static String getSQL(String key)
    {
        return SQL.get(key);
    }

}
