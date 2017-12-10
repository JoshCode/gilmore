package nl.codefox.gilmore.command.dice.expressions;

import nl.codefox.gilmore.command.dice.Lexer;

import java.util.ArrayList;

public class Addition extends Expression {

	public Addition(ArrayList<Lexer.Token> tokens) {
		super(tokens);

		boolean twoAdditions = false;

		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).type == Lexer.TokenType.ADDITION) {
				twoAdditions = true;
				Addition a = new Addition(new ArrayList<>(tokens.subList(0, i)));
				Addition b = new Addition(new ArrayList<>(tokens.subList(i + 1, tokens.size())));
				value = a.value + b.value;
				description = a.description + " + " + b.description;
			}
		}

		if (!twoAdditions) {
			Multiplication multiplication = new Multiplication(tokens);
			value = multiplication.value;
			description = multiplication.description;
		}
	}
}
