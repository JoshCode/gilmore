package nl.codefox.gilmore.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Resources {

    private static Map<String, String> SQL = new HashMap<String, String>();

    static
    {
        loadSQL();
    }
    
    private static void loadSQL()
    {
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
                    while ((sqlLine = br.readLine()) != null) 
                    {
                        sql += sqlLine + "\n";
                    }
                    
                    SQL.put(line, sql);
                }
            }
            br.close();
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
