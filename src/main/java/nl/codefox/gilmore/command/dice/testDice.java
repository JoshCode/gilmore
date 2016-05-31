
package nl.codefox.gilmore.command.dice;

/**
 *
 * @author Joep Veldhoven (s4456556)
 * @author Tijmen van der Kemp (s4446887)
 */
public class testDice {
    public static void main (String [] args) {
        Dice dice = new Dice ("3[str]+2[adsf]*[asdf][afsd]2+3[con]");
        System.out.println(dice.roll());
        System.out.println(dice.getBreakdown());
    }
}
