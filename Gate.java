// Gate.java

import java.util.Iterator;
import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;

/** Subclasses of Gates are joined by Wires.
 *  @author Piotr Smietana
 *  @version 2019-04-03
 *  @see Wire
 *  @see InputCountGate
 *  @see InputGate
 *  @see OutputGate
 */
public abstract class Gate {

    // List of all of the gates in the universe
    private static final List <Gate> allGates = new LinkedList <> ();

    /** Allow outsiders to iterate over all the gates.
     *  @return an Iterator allowing access to gates
     */
    public static Iterator <Gate> iterator() {
        return allGates.iterator();
    }

    /** Look up a gate by name.
     *  @param n the name of the gate, possibly null (matches nothing)
     *  @return the Gate with that name, or null if no match
     */
    public static Gate findGate( String n ) {
        for (Gate g: allGates) {
            if (g.name.equals( n )) return g;
        }
        return null;
    }

    /** The name of this gate.
      */
    public final String name;

    /** Where the outputs from this gate go.
     */
    protected final LinkedList <Wire> outgoing = new LinkedList <> ();

    /** Where the inputs to this gate come from.
     */
    protected int inCount = 0;

    /** Constructor, used only from within subclasses.
     *  @param n the name of the new gate
     */
    protected Gate( String n ) {
        name = n;
    }

    /** Build a new Gate and add it to the list of gates.
     *  @param sc the scanner used to get the attributes of this gate
     */
    public static void make( Scanner sc ) {
        // First worry about the gate name
        final String name
            = ScanSupport.scanName( sc, () -> "Gate has missing name" );
        if (name == null) {
            ScanSupport.finishLine(
                sc, () -> "Gate: followed by"
            );
            return;
        }
        if (findGate( name ) != null) {
            Errors.warn( "Gate " + name + ": name reused" );
            ScanSupport.finishLine(
                sc, () -> "Gate " + name + ": followed by"
            );
            return;
        }

        // Second get the gate kind
        final String kind = ScanSupport.scanName(
            sc, () -> "Gate " + name + ": kind missing"
        );
        if (kind == null) {
            ScanSupport.finishLine(
            sc, () -> "Gate " + name + ": followed by"
            );
            return;
        }

        // Finally construct the right kind of gate
        if ("xor".equals( kind )) {
            allGates.add( new XorGate( sc, name ) );
        } else if ("threshold".equals( kind )) {
            allGates.add( new ThresholdGate( sc, name ) );
        } else if ("input".equals( kind )) {
            allGates.add( new InputGate( sc, name ) );
        } else if ("output".equals( kind )) {
            allGates.add( new OutputGate( sc, name ) );
        } else {
            Errors.warn( "Gate " + name + " " + kind + ": kind unknown" );
            ScanSupport.finishLine(
                sc, () -> "Gate " + name + " " + kind + ": followed by"
            );
        }
    }

    /** Every subclass of gate offers a sanity check.
     *  <p>
     *  It should call Errors.warn() for each failure it detects
     */
    public abstract void sanityCheck();

    /** Connect a wire as an input to this gate.
     *  @param w the wire
     */
    public void connectTo( Wire w ) {
        inCount = inCount + 1;
    }

    /** Connect a wire as an output from this gate.
     *  @param w the wire
     */
    public void connectFrom( Wire w ) {
        outgoing.add( w );
    }

    /** Gate toString() method.
     */
    public String toString() {
        return "Gate " + name;
    }

    // Simulation methods

    /** Tell the gate that one of its inputs has changed.
     *  <p>
     *  Each subclass must implement this method.
     *  @param value the new value of that input
     */
    public abstract void inputChange( int value );

    /** Actually change the output.
     *  @param value the new value of that input
     */
    protected void outputChange( int value ) {
        final int comp = 1 - value;
        System.out.println(
            "time " + Simulation.time + " " + comp + "->" + value + " " + this
        );
        for (Wire w: outgoing) w.inputChange( value );
    }
}
