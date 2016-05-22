package nl.codefox.gilmore.command.criticalrole;

public enum CriticalRoleCharacter 
{

    VAX     ("/img/vax.png",     2,  "vax"),
    VEX     ("/img/vex.png",     3,  "vex"),
    GROG    ("/img/grog.png",    4,  "grog"),
    PERCY   ("/img/percy.png",   5,  "percy"),
    KEYLETH ("/img/keyleth.png", 6,  "keyleth"),
    SCANLAN ("/img/scanlan.png", 7,  "scanlan"),
    KASHAW  ("/img/kashaw.png",  8,  "kashaw"),
    ZAHRA   ("/img/zahra.png",   9,  "zahra"),
    GERN    ("/img/gern.png",    10, "gern"),
    PIKE    ("/img/pike.png",    14, "pike"),
    GARTHOK ("/img/garthok.png", 15, "garthok"),
    TIBERIUS("/img/tiberius.png",16, "tiberius"),
    LILLITH ("/img/lillith.png", 17, "lillith"),
    THORBIR ("/img/thorbir.png", 18, "thorbir"),
    LYRA    ("/img/lyra.png",    19, "lyra");
    
    private String resource;
    private int row;
    private String name;
    
    CriticalRoleCharacter(String resource, int row, String name)
    {
        this.resource = resource;
        this.row = row;
        this.name = name;
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
    
    public static CriticalRoleCharacter byName(String name)
    {
        for(CriticalRoleCharacter crc : values())
        {
            if(crc.getName().equals(name))
            {
                return crc;
            }
        }
        return null;
    }
    
    public static boolean contains(String name)
    {
        for(CriticalRoleCharacter crc : values())
        {
            if(crc.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }
    
}
