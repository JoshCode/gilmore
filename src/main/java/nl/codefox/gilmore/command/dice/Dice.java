package nl.codefox.gilmore.command.dice;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dice 
{
    
    private final Pattern DICE_PATTERN = Pattern.compile("(?<A>\\d*)d(?<B>\\d+)(?>(?<MULT>[x/])(?<C>\\d+))?(?>(?<ADD>[+-])(?<D>\\d+))?");
    private final Random random = new Random();
    
    private int rolls = 0;
    private int faces = 0;
    private int multiplier = 0;
    private int additive = 0;
    
    private String breakdown;

    public Dice(String str) 
    {
        parse(str);
    }

    public int getRolls() 
    {
        return rolls;
    }

    public int getFaces()
    {
        return faces;
    }

    public int getMultiplier() 
    {
        return multiplier;
    }

    public int getAdditive() 
    {
        return additive;
    }
    
    public String getBreakdown()
    {
        return breakdown;
    }
    
    public int roll()
    {
        int val = 0;
        breakdown = "((";
        for(int i = 0; i < getRolls(); i++)
        {
            int x = random.nextInt(getFaces()) + 1;
            val += x;
            
            breakdown += String.valueOf(x);
            if(i != getRolls() - 1)
            {
                breakdown += " + ";
            }
        }
        breakdown += ") * " + getMultiplier() + " + " + getAdditive() + ")";
        
        val *= getMultiplier();
        val += getAdditive();
        
        return val;
    }

    private boolean isEmpty(String str) 
    {
        return str == null || str.trim().isEmpty();
    }

    private Integer getInt(Matcher matcher, String group, int defaultValue) 
    {
        String groupValue = matcher.group(group);
        return isEmpty(groupValue) ? defaultValue : Integer.valueOf(groupValue);
    }

    private Integer getSign(Matcher matcher, String group, String positiveValue) 
    {
        String groupValue = matcher.group(group);
        return isEmpty(groupValue) || groupValue.equals(positiveValue) ? 1 : -1;
    }
    
    private void parse(String str) 
    {
        Matcher matcher = DICE_PATTERN.matcher(str);
        if(matcher.matches()) 
        {
            this.rolls = getInt(matcher, "A", 1);
            this.faces = getInt(matcher, "B", -1);
            this.multiplier = getInt(matcher, "C", 1) * getSign(matcher, "MULT", "x");
            this.additive = getInt(matcher, "D", 0) * getSign(matcher, "ADD", "+");
        }
    }
    
}
