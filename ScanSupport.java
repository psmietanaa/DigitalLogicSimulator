// ScanSupport.java

import java.util.regex.Pattern;
import java.util.Scanner;

/** Support package for application dependent extensions to class Scanner.
 *  @author Piotr Smietana
 *  @version 2019-03-04
 *  @see Errors
 */
public class ScanSupport {

    // Match one legal name
    private static final Pattern name =
        Pattern.compile( "[A-Za-z]*[A-Za-z0-9]" );
    // Match any number of blanks
    private static final Pattern blanks =
        Pattern.compile( "[ \t]*" );
    // Match text up to newline
    private static final Pattern theRest =
        Pattern.compile( "[^\n]*" );

    /** Support interface for passing deferred computation of message text.
     */
    public interface MessageCarrier {
        String content();
    }

    /** Scan and return one name, if available.
     *  @param sc the scanner to read from
     *  @param msg the message to output if there is no name
     *  @return the name, or null if there is no name
     *  Typically called as ScanSupport.scanName( sc, () -> string + expr )
     */
    public static String scanName( Scanner sc, MessageCarrier msg ) {
        if (sc.hasNext( name )) return sc.next(name);
        Errors.warn( msg.content() );
        return null;
    }

    /** Scan and return one nonnegative int, if available.
     *  @param sc the scanner to read from
     *  @param msg the message prefix to output if there is no int
     *  @return the value, or 0 if there is no number
     *  Typically called as ...scanPositiveInt( sc, () -> string + expr )
     */
    public static int scanPositiveInt( Scanner sc, MessageCarrier msg ) {
        if (sc.hasNextInt()) {
            int value = sc.nextInt();
            if (value >= 0) return value;
            Errors.warn( msg.content() + " " + value + ": must be positive" );
            }
        Errors.warn( msg.content() + ": integer value expected" );
        return 0;
    }

    /** Scan and return one nonnegative float, if available.
     *  @param sc the scanner to read from
     *  @param msg the message prefix to output if there is no float
     *  @return the value, or NaN if there is no number
     *  Typically called as ...scanPositiveFloat( sc, () -> string + expr )
     */
    public static float scanPositiveFloat( Scanner sc, MessageCarrier msg ) {
        if (sc.hasNextFloat()) {
            float value = sc.nextFloat();
            if (value >= 0.0F) return value;
            Errors.warn( msg.content() + " " + value + ": must be positive" );
            }
        Errors.warn( msg.content() + ": float value expected" );
        return Float.NaN;
    }

    /** Skip the rest of this line, and complain if it's nonblank noncomment.
     *  @param sc the scanner to read from
     *  @param msg the object that computes the message to output if needed
     *  Typically called as ScanSupport.finishLine( sc, () -> string + expr )
     */
    public static void finishLine( Scanner sc, MessageCarrier msg ) {
        sc.skip( blanks );
        sc.skip( theRest );
        final String remainder = sc.match().group( 0 );
        if ("".equals( remainder )) return;
        if (remainder.startsWith ("--")) return;
        Errors.warn( msg.content() + ": " + remainder );
    }
}
