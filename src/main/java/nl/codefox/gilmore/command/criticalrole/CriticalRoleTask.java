package nl.codefox.gilmore.command.criticalrole;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import nl.codefox.gilmore.util.GoogleSheetsUtil;
import nl.codefox.gilmore.util.StringUtil;

public class CriticalRoleTask extends Thread
{

    public CriticalRoleTask()
    {
        this.start();
    }
    
    @Override
    public void run()
    {
        
        try
        {
            while(true)
            {
                Sheets service = GoogleSheetsUtil.getSheetsService();
                
                String sid = CriticalRoleConstants.SPREADSHEET_ID;
                String range = CriticalRoleConstants.SPREADSHEET_CHARACTER_RANGE;
                
                ValueRange response = service.spreadsheets().values().get(sid, range).execute();
                
                List<List<Object>> values = response.getValues();
                
                if(values != null && values.size() != 0)
                {
                    for (int i = 0; i < values.size(); i++) 
                    {
                        List<Object> row = values.get(i);
                        
                        String name = "";
                        String race = "";
                        String type = "";
                        String clas = "";
                        List<String> aliasesList = new ArrayList<String>();
                        
                        if(row.size() == 19)
                        {
                            name = String.valueOf(row.get(0));
                            race = String.valueOf(row.get(17));
                            type = String.valueOf(row.get(16));   
                            clas = String.valueOf(row.get(12));
                        
                            for(String string : String.valueOf(row.get(18)).toLowerCase().split(","))
                            {
                                aliasesList.add(string.trim());
                            }
                        }
                        else if(row.size() == 18)
                        {
                            name = String.valueOf(row.get(0));
                            race = String.valueOf(row.get(17));
                            type = String.valueOf(row.get(16));   
                            clas = String.valueOf(row.get(12));
                        }
                        else if(row.size() == 17)
                        {
                            name = String.valueOf(row.get(0));
                            race = String.valueOf(row.get(15));
                            type = String.valueOf(row.get(14));   
                            clas = String.valueOf(row.get(12));
                        
                            for(String string : String.valueOf(row.get(16)).toLowerCase().split(","))
                            {
                                aliasesList.add(string.trim());
                            }
                        }
                        else
                        {
                            continue;
                        }
                        
                        String title = String.format("the %s %s", race, clas);
                        String resource = String.format("http://gilmore.prscampbell.com/%s.png", name.toLowerCase());
                        String[] aliases = StringUtil.listToString(aliasesList, ",").split(",");
                        
                        if(type.equals("y"))
                        {
                            CriticalRoleCharacter.addCharacter(
                                new CriticalRoleCharacter(resource, (i + 2), name, title, aliases)
                            );
                        }
                    }
                }
                
                Thread.sleep(60000L);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
}
