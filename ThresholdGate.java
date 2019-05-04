// ThresholdGate.java

import java.util.Scanner;

/** Threshold logic gates
 *  @author Piotr Smietana
 *  @version 2019-04-03
 *  @see Gate
 */
public class ThresholdGate extends InputCountGate {

    private int threshold = 0;

    /** Constructor, used only from within subclasses.
     *  @param sc the scanner
     *  @param name the name of the new gate
     */
    public ThresholdGate( Scanner sc, String name ){
        super( name );
        threshold = ScanSupport.scanPositiveInt( sc, () -> this.toString() );
        delay = ScanSupport.scanPositiveFloat( sc, () -> this.toString() );
        ScanSupport.finishLine( sc, () -> this + ": followed by" );
    }

    /** Every subclass of gate offers a sanity check.
     */
    public void sanityCheck() {
        if (threshold > inCount) {
            Errors.warn( this.toString() + ": has threshold > input wires" );
        }
    }

    /** ThresholdGate toString() method.
     */
    public String toString() {
        return super.toString() + " threshold " + threshold + " " + delay;
    }

    /** Compute the gate's logical value.
     *  <p>
     *  Each subclass must implement this method.
     *  @param count the number of ones on the input
     *  @return logic value
     */
    public int logicRule( int count ) {
        if (count >= threshold) return 1;
        return 0;
    }
}
