// XorGate.java

import java.util.Scanner;

/** Exclusive Or gates
 *  @author Piotr Smietana
 *  @version 2019-04-03
 *  @see Gate
 */
public class XorGate extends InputCountGate {

    /** Constructor, used only from within subclasses.
     *  @param sc the scanner
     *  @param name the name of the new gate
     */
    public XorGate( Scanner sc, String name ){
        super( name );
        delay = ScanSupport.scanPositiveFloat( sc, () -> this.toString() );
        ScanSupport.finishLine( sc, () -> this + ": followed by" );
    }

    /** Every subclass of gate offers a sanity check.
     */
    public void sanityCheck() {
        if (inCount != 2) {
            Errors.warn( this.toString() + ": input wire count must be two" );
        }
    }

    /** XorGate toString() method.
     */
    public String toString() {
        return super.toString() + " xor " + delay;
    }

    /** Compute the gate's logical value.
     *  <p>
     *  Each subclass must implement this method.
     *  @param count the number of ones on the input
     *  @return logic value
     */
    public int logicRule( int count ) {
        return count % 2;
    }
}
