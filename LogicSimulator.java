// LogicSimulator.java

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

/** Main program.
 *  @author Piotr Smietana
 *  @version 2019-04-29
 *  @see Gate
 *  @see Wire
 */
public class LogicSimulator {

    // Build the logic circuit by scanning a source file
    private static void buildLogic( Scanner sc ) {
        while (sc.hasNext()) {
            String command = sc.next();
            if ("--".equals( command )) {
                if (sc.hasNextLine()) sc.nextLine();
            } else if ("gate".equals( command )) {
                Gate.make( sc );
            } else if ("wire".equals( command )) {
                // Because of multiple destinations, must get the source here
                final String srcName
                    = ScanSupport.scanName( sc, () -> "Wire has no source" );
                if (srcName != null) {
                    final Gate source = Gate.findGate( srcName );
                    if (source != null) {
                        // Now build at least one wire and possibly more
                        Wire.make( sc, source );
                        while (sc.hasNextFloat()) {
                            Wire.make( sc, source );
                        }
                        ScanSupport.finishLine(
                            sc, () -> "Wire " + srcName + ": followed by"
                        );
                    } else {
                        Errors.warn( "Wire " + srcName + ": undefined source" );
                        ScanSupport.finishLine(
                            sc, () -> "Wire " + srcName + ": followed by"
                        );
                    }
                } else {
                    ScanSupport.finishLine( sc, () -> "Wire: followed by" );
                }
            } else {
                Errors.warn( "Invalid command " + command );
                ScanSupport.finishLine( sc, () -> command + ": followed by" );
            }
        }
    }

    // Perform sanity checks on all gates
    private static void sanityChecks() {
        for (Iterator <Gate> i = Gate.iterator(); i.hasNext();) {
            i.next().sanityCheck();
        }
    }

    // Print out the entire logic circuit
    private static void printLogic() {
        for (Iterator <Gate> i = Gate.iterator(); i.hasNext();) {
            System.out.println( i.next() );
        }
        for (Iterator <Wire> i = Wire.iterator(); i.hasNext();) {
            System.out.println( i.next() );
        }
    }

    /** Main method.
     *  @param args the command line arguments
     */
    public static void main( String[] args ) {
        Errors.setPrefix( "Logicsim" );
        if (args.length < 1) {
            Errors.fatal( "Missing argument" );
        }
        if (args.length > 1) {
            Errors.warn( "Extra arguments" );
        }
        try {
            buildLogic( new Scanner( new FileInputStream( args[0] ) ) );
            sanityChecks();
            if (Errors.warnings() > 0) {
                printLogic();
                System.exit( 1 );
            } else {
                Simulation.run();
            }
        } catch( FileNotFoundException e ) {
            Errors.fatal( "Can't open the file" );
        }
    }
}
