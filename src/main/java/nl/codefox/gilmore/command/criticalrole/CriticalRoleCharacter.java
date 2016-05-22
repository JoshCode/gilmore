package nl.codefox.gilmore.command.criticalrole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CriticalRoleCharacter 
{

    private static List<CriticalRoleCharacter> characters = new ArrayList<CriticalRoleCharacter>();
    
    private List<String> aliases = new ArrayList<String>();
    private String resource = "";
    private String name = "";
    private String title = "";
    private int row = 0;
    
    public CriticalRoleCharacter(String resource, int row, String name, String title, String... aliases)
    {
        this.resource = resource;
        this.row = row;
        this.name = name;
        this.title = title;
        this.aliases.addAll(Arrays.asList(aliases));
        this.aliases.add(name.toLowerCase());
    }
    
    public List<String> getAliases()
    {
        return this.aliases;
    }
    
    public String getResource()
    {
        return this.resource;
    }
    
    public int getRow()
    {
        return this.row;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getTitle()
    {
        return this.title;
    }
    
    public static void clear()
    {
        characters.clear();
    }
    
    public static void addCharacter(CriticalRoleCharacter character)
    {
        characters.add(character);
    }
    
    public static CriticalRoleCharacter getCharacter(String name)
    {
        for(CriticalRoleCharacter character : characters)
        {
            if(character.getAliases().contains(name))
            {
                return character;
            }
        }
        return null;
    }
    
}
