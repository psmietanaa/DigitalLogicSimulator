// Errors.java

/** Error reporting package.
 *  <p>
 *  Provide a standard prefix and behavior for error reporting.
 *  @author Piotr Smietana
 *  @version 2019-03-03
 */
public class Errors {

    // Prefix string for all error messages
    private static String prefix = "???: ";

    // Number of warnings
    private static int warnCount = 0;

    /** Set prefix on error reports, should be done before any error reports.
     *  @param p the prefix on any error messages
     */
    public static void setPrefix( String p ) {
        prefix = p;
    }

    /** Report nonfatal errors, output a message and return.
     *  @param m the message to output
     */
    public static void warn( String m ) {
        System.err.println( prefix + ": " + m );
        warnCount = warnCount + 1;
    }

    /** Report the number of warnings that have been issued.
     *  @return the count
     */
    public static int warnings() {
        return warnCount;
    }

    /** Report fatal errors, output a message and crash.
     *  @param m the message to output
     */
    public static void fatal( String m ) {
        warn( m );
        System.exit( 1 );
    }
}
