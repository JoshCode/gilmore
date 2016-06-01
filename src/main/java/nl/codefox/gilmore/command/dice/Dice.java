package nl.codefox.gilmore.command.dice;

import java.util.ArrayList;
import java.util.Random;
import nl.codefox.gilmore.command.dice.expressions.Addition;

public class Dice 
{
    
    private final Random random = new Random();
    private final ArrayList<Lexer.Token> tokens;
    private String breakdown;
    
    public Dice(String str) 
    {
        tokens = Lexer.lex(str);

    }

    public String getBreakdown() {
        return breakdown;
    }
    
    public int roll()
    {
        Addition expr = new Addition (tokens);
        breakdown = expr.description;
        return expr.value;
    }
}
