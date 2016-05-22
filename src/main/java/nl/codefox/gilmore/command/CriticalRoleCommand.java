package nl.codefox.gilmore.command;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.mysql.fabric.xmlrpc.base.Value;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import nl.codefox.gilmore.util.GoogleSheetsUtil;

public class CriticalRoleCommand extends GilmoreCommand 
{

    private final int STR    = 0;
    private final int DEX    = 1;
    private final int CON    = 2;
    private final int INT    = 3;
    private final int WIS    = 4;
    private final int CHA    = 5;
    private final int AC     = 6;
    private final int HP     = 7;
    private final int TMPHP  = 8;
    private final int MAXHP  = 9;
    
    private final int firstColumn = 630;
    private final int secondColumn = 915;
    private final int thirdColumn = 1201;
    
    private final int firstRow = 187;
    private final int secondRow = 277;
    private final int thirdRow = 369;
    
    private final int fontSize = 39;

    private final int vaxRow = 2;
    private final int vexRow = 3;
    private final int grogRow = 4;
    private final int percyRow = 5;
    private final int keylethRow = 6;
    private final int scanlanRow = 7;
    private final int pikeRow = 14;
    
    public CriticalRoleCommand() 
    {
        super("Shows information about the main characters from Critical Role", "Usage: !criticalrole [character]", 2, null, "!criticalrole");
    }

    @Override
    public void run(String[] args, MessageReceivedEvent event) 
    {
        event.getChannel().sendMessage(String.format("[%s] `Preparing that for you now - one moment...`", event.getAuthor().getAsMention()))
        ;
        switch(args[1])
        {
            case "grog":
                draw(event, "/img/grog.png", getData(grogRow)); break;
            case "pike":
                draw(event, "/img/pike.png", getData(pikeRow)); break;
            case "vex":
                draw(event, "/img/vex.png", getData(vexRow)); break;
            case "vax":
                draw(event, "/img/vax.png", getData(vaxRow)); break;
            case "keyleth":
                draw(event, "/img/keyleth.png", getData(keylethRow)); break;
            case "scanlan":
                draw(event, "/img/scanlan.png", getData(scanlanRow)); break;
            case "percy":
                draw(event, "/img/percy.png", getData(percyRow)); break;
        }
    }
    
    private int[] getData(int row)
    {
        
        try 
        {
            
            Sheets service = GoogleSheetsUtil.getSheetsService();
            
            String sid = "1Pm8eLIYggq1vDMrR3EUjDOPlHqwC1owPbZj6rEgvm9M";
            String range = String.format("Data!B%d:L%d", row, row);
            
            ValueRange response = service.spreadsheets().values().get(sid, range).execute();
            
            List<List<Object>> values = response.getValues();
            
            if(values != null && values.size() != 0)
            {
                for (List<Object> r : values) 
                {
                    int[] data = new int[10];
                    data[HP] = Integer.parseInt(String.valueOf(r.get(0)));
                    data[MAXHP] = Integer.parseInt(String.valueOf(r.get(1)));
                    data[TMPHP] = Integer.parseInt(String.valueOf(r.get(2)));
                    data[AC] = Integer.parseInt(String.valueOf(r.get(3)));
                    data[STR] = Integer.parseInt(String.valueOf(r.get(5)));
                    data[DEX] = Integer.parseInt(String.valueOf(r.get(6)));
                    data[CON] = Integer.parseInt(String.valueOf(r.get(7)));
                    data[INT] = Integer.parseInt(String.valueOf(r.get(8)));
                    data[WIS] = Integer.parseInt(String.valueOf(r.get(9)));
                    data[CHA] = Integer.parseInt(String.valueOf(r.get(10)));
                    return data;
                }
            }
            
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        return new int[10];
    }
    
    private void draw(MessageReceivedEvent event, String resource, int[] data)
    {
        try
        {
            BufferedImage image = new BufferedImage(1331, 450, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();

            graphics.drawImage(ImageIO.read(new File(getClass().getResource(resource).getFile())), 0, 0, null);

            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            graphics.setColor(Color.BLACK);

            // STR
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, fontSize)); 
            graphics.drawString(String.valueOf(data[STR]), firstColumn, firstRow);

            // DEX
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, fontSize)); 
            graphics.drawString(String.valueOf(data[DEX]), firstColumn, secondRow);

            // CON
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, fontSize)); 
            graphics.drawString(String.valueOf(data[CON]), firstColumn, thirdRow);

            // INT
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, fontSize)); 
            graphics.drawString(String.valueOf(data[INT]), secondColumn, firstRow);

            // WIS
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, fontSize)); 
            graphics.drawString(String.valueOf(data[WIS]), secondColumn, secondRow);

            // CHA
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, fontSize)); 
            graphics.drawString(String.valueOf(data[CHA]), secondColumn, thirdRow);

            // AC
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, fontSize)); 
            graphics.drawString(String.valueOf(data[AC]), thirdColumn, firstRow);

            // HP
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, fontSize)); 
            graphics.drawString(String.valueOf(data[HP]), thirdColumn, secondRow);

            // Temp HP
            graphics.setFont(new Font("COCOGOOSE", Font.PLAIN, fontSize)); 
            graphics.drawString(String.valueOf(data[TMPHP]), thirdColumn, thirdRow);
            
            float current = data[HP];
            float max = data[MAXHP];
            
            float bar1 = (327 / 100);
            float bar2 = ((current / max) * 100);
            
            int bar = (int) Math.round(bar1 * bar2);
            
            graphics.setColor(Color.decode("0xE14040"));
            graphics.fillRect(962, 47, bar, 32);
            
            File temp = new File(System.getProperty("user.home"), "temp.png");
            ImageIO.write(image, "png", temp);
            
            event.getChannel().sendFile(temp, null);
            temp.delete();
            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
