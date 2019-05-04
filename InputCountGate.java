// InputCountGate.java

/** Parent class of gates where the output depends on the number of one inputs.
 *  @author Piotr Smietana
 *  @version 2019-04-29
 *  @see Gate
 */
abstract class InputCountGate extends Gate {

    private int inputCount = 0;
    private int oldOutput = 0;
    private int newOutput = 0;
    float delay = Float.NaN;

    /** Constructor, used only from within subclasses.
     *  @param name the name of the new gate
     */
    protected InputCountGate( String name ) {
        super( name );
    }

    // Simulation methods

    /** Tell the gate that one of its inputs has changed.
     *  <p>
     *  Each subclass must implement this method.
     *  @param value the new value of that input
     */
    public void inputChange( int value ) {
        if (value == 1) {
            inputCount = inputCount + 1;
        } else {
            inputCount = inputCount - 1;
        }
        // The following code suppresses changes from 1 to 1 or 0 to 0
        int myOutput = logicRule( inputCount );
        if (myOutput != oldOutput) {
            Simulation.schedule(
            delay, () -> this.outputChange( myOutput ));
            oldOutput = myOutput;
        }
    }

    /** Override the default output change event.
     *  <p>
     *  Needed to suppress short pulses.
     *  @param value the new value of that input
     */
    protected void outputChange( int value ) {
        // First, suppress changes away from the value
        if (value != oldOutput) return;
        // Second, suppress changes from 1 to 1 or 0 to 0
        if (value == newOutput) return;
        // Finally, we know that this output change should occur
        super.outputChange(value);
        newOutput = value;
    }

    /** Compute the gate's logical value.
     *  <p>
     *  Each subclass must implement this method.
     *  @param count the number of ones on the input
     *  @return logic value
     */
    public abstract int logicRule( int count );
}
