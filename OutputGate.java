// OutputGate.java

import java.util.Scanner;

/** Output gates will provide output from the simulation.
 *  @author Piotr Smietana
 *  @version 2019-04-29
 *  @see Gate
 */
public class OutputGate extends Gate {

    /** Constructor, used only from within subclasses.
     *  @param sc the scanner
     *  @param name the name of the new gate
     */
    public OutputGate( Scanner sc, String name ){
        super( name );
        ScanSupport.finishLine( sc, () -> this + ": followed by" );
    }

    /** Every subclass of gate offers a sanity check.
     */
    public void sanityCheck() {
        if (this.outgoing.peek() != null) {
            Errors.warn( this.toString() + ": has outgoing wires" );
        }
    }

    /** OutputGate toString() method.
     */
    public String toString() {
        return super.toString() + " output";
    }

    // Simulation methods

    /** Tell the gate that one of its inputs has changed.
     *  <p>
     *  Each subclass must implement this method.
     *  @param value the new value of that input
     */
    public void inputChange( int value ) {
        // Nothing to be done
        // End of circuit
    }
}
