// Wire.java

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

/** Wires connect Gates.
 *  @author Piotr Smietana
 *  @version 2019-04-29
 *  @see Gate
 */
public class Wire {

    // List of all of the wires in the universe
    private static final List <Wire> allWires = new LinkedList <> ();

    /** Allow outsiders to iterate over all the wires
     *  @return an Iterator allowing access to wires
     */
    public static Iterator <Wire> iterator() {
        return allWires.iterator();
    }

    // Attributes of each Wire
    private final Gate source;
    private final Gate destination;
    private final float delay;

    // Construct a new Wire
    private Wire( Gate src, Gate dst, Float del ) {
        source = src;
        destination = dst;
        delay = del;
    }

    /** Build a new Gate and add it to the list of gates.
     *  @param sc the scanner used to get the attributes of this wire
     *  @param src the source gate, guaranteed non null
     *  When called, the keyword "wire" and the name of the source gate
     *  have already been scanned, so we are ready to scan the delay and
     *  destination.
     */
    public static void make( Scanner sc, Gate src ) {
        // Wire's delay and destination, until constructor call
        final Float delay
             = ScanSupport.scanPositiveFloat( sc, () -> "wire " + src.name );
        final String dstName = ScanSupport.scanName(
            sc, () -> "Wire " + src.name + " " + delay + ": destination missing"
        );
        // All scanning is done now, may return at any time to avoid making wire
        if (dstName == null) return;
        Gate dst = Gate.findGate( dstName );
        if (dst == null) {
            Errors.warn(
            "Wire " + src.name + " " + delay + " " + dstName
            + ": undefined destination"
            );
            return;
        }
        // Make the wire
        Wire w = new Wire( src, dst, delay );
        // Now we can actually connect the source and destination gates
        src.connectFrom( w );
        dst.connectTo( w );
        allWires.add( w );
    }

    /** Wire toString() method.
     */
    public String toString() {
        return "Wire " + source.name + ' ' + delay + ' ' + destination.name;
    }

    // Simulation methods

    /** Tell the wire that its input has changed.
     *  @param value the new value of that input
     */
    public void inputChange( int value ) {
        Simulation.schedule(
            delay, () -> this.outputChange( value )
        );
    }

    /** Actually change the output.
     */
    private void outputChange( int value ) {
        final int comp = 1 - value;
        System.out.println(
            "time " + Simulation.time + " " + comp + "->" + value + " " + this
        );
        destination.inputChange( value );
    }
}
