
package nl.codefox.gilmore.command.dice.expressions;

import java.util.ArrayList;
import nl.codefox.gilmore.command.dice.Lexer;

public class Atomic extends Expression {

    public Atomic(ArrayList<Lexer.Token> tokens) {
        super(tokens);
        for (int i = 0; i < tokens.size(); i ++) {
            if (tokens.get(i).type == Lexer.TokenType.ATOMIC) {
                value = Integer.parseInt(tokens.get(i).data);
            }
            description += tokens.get(i).data;
        }
    }

}
