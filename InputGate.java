// InputGate.java

import java.util.Scanner;

/** Input gates will provide input to the simulation.
 *  @author Piotr Smietana
 *  @version 2019-04-29
 *  @see Gate
 */
public class InputGate extends Gate {

    private float delay = Float.NaN;
    private int initial = 0;
    private int changeCount = 0;

    /** Constructor, used only from within subclasses.
     *  @param sc the scanner
     *  @param name the name of the new gate
     */
    public InputGate( Scanner sc, String name ){
        super( name );
        initial = ScanSupport.scanPositiveInt( sc, () -> this.toString() );
        delay = ScanSupport.scanPositiveFloat( sc, () -> this.toString() );
        changeCount = ScanSupport.scanPositiveInt( sc, () -> this.toString() );
        if (initial > 1) Errors.warn( this + ": initial value > 1" );
        ScanSupport.finishLine( sc, () -> this + ": followed by" );
    }

    /** Every subclass of gate offers a sanity check.
     */
    public void sanityCheck() {
        if (inCount != 0) {
            Errors.warn( this.toString() + ": has unexpected input wires" );
        }
        // Launch the simulation!
        if (initial == 1) {
            this.outputChange( 1 );
        }
        if (changeCount > 0) {
            Simulation.schedule(
                    delay, () -> this.nextChange( 1 - initial )
            );
        }
    }

    /** InputGate toString() method.
     */
    public String toString() {
        return super.toString() + " input "
            + initial + " " + delay + " " + changeCount;
    }

    // Simulation methods

    /** Tell the gate that one of its inputs has changed.
     *  <p>
     *  Each subclass must implement this method.
     *  @param value the new value of that input
     */
    public void inputChange( int value ) {
        Errors.warn(this.toString() + ": impossible input change");
    }

    // Output change process for sequence of inputs
    private void nextChange( int value ) {
        changeCount = changeCount - 1;
        // First change the output
        this.outputChange( value );
        // Second schedule the next change, if any
        if (changeCount > 0) {
            Simulation.schedule(
                    delay, () -> this.nextChange( 1 - value )
            );
        }
    }
}
