
package nl.codefox.gilmore.command.dice;

/**
 *
 * @author Tijmen van der Kemp
 */
public class testDice {
    public static void main (String [] args) {
        String testString = "1d20 + 1!";
        System.out.println(testString);
        Dice dice = new Dice (testString);
        int roll = dice.roll();
        System.out.println(dice.getBreakdown() + " = " + roll);
    }
}
