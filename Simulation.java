// Simulation.java

import java.util.PriorityQueue;
import java.util.LinkedList;

/** Simulation framework
 *  @author Douglas W. Jones
 *  @version 2019-04-29
 */
public class Simulation {

    /** The current simulation time, updated before each Action is triggered.
     */
    public static float time = 0.0f;

    // This interface is so we can schedule events with lambda expressions
    public interface Action {
        void trigger();
    }

    // Events are scheduled on the event set
    // Event records are also used in semaphore queues
    private static class Event {
        final float time;
        final Action act;

        Event( float t, Action a ) {
            time = t;
            act = a;
        }
    }

    // The central organizing data structure of the simulation
    private static final PriorityQueue <Event> eventSet
            = new PriorityQueue <> (
                    ( Event e1, Event e2 ) -> Float.compare( e1.time, e2.time )
    );

    /** Schedule a new event
     *  @param delay the time interval before the Action should be triggered
     *  @param a the Action that should be triggered after that delay
     *  A typical call will look like this
     *  Simulation.schedule( someDelay, ()->codeToRun( params ) );
     *  That is, the Action will be constructed by a lambda expression
     */
    public static void schedule( float delay, Action a ) {
        eventSet.add( new Event( time + delay, a ) );
    }

    /** Provide for synchronization between logical processes.
     *  <p>
     *  A logical process is a sequence of events where each event
     *  in the sequence causes the next, either by scheduling it or
     *  by waiting on a semaphore, which will schedule it later.
     */
    public static class Semaphore {

        // Every Semaphore has a non-negative count and a queue of events
        private int count = 0;
        private final LinkedList <Event> queue
                    = new LinkedList <> ();

        // Initially, both q.isEmpty() == true and count == 0;
        // When count > 0 it must be the case that q.isEmpty() == true, and
        // When q.isEmpty() == false it must be the case that count == 0.

        /** Create a new semaphore with an empty queue and the given count.
         *  @param c the non-negative initial value of the semaphore's count.
         */
        public Semaphore( int c ) {
            if (c < 0) {
                throw new java.lang.Error(
                        "Semaphore must not be created with a negative count."
                );
            }
            count = c;
        }

        /** Claim a resource and schedule a new event when the claim succeeds.
         *  @param delay the time interval before the Action can be triggered
         *  @param a the Action that should be triggered after that delay
         *  A typical call will look like this
         *  s.wait( someDelay, ()->codeToRun( params ) );
         *  That is, the Action will be constructed by a lambda expression.
         *  it will either be scheduled immediately, if the count permits,
         *  or it will be scheduled later, by a call to s.signal().
         */
        public void wait( float delay, Action a ) {
            if (count > 0) {
                count = count - 1;
                schedule( delay, a );
            } else {
                queue.add( new Event( delay, a ) );
            }
        }

        /** Release a resource and schedule any event that was waiting for it.
         */
        public void signal() {
            if (queue.isEmpty()) {
                count = count + 1;
            } else {
                Event e = queue.remove();
                schedule( e.time, e.act );
            }
        }
    }

    /** Run a simulation.
     *  Call this after scheduling at least one event.
     *  The simulation will run until either there are no more events or
     *  some event terminates the program.
     *  From this point on, a typical simulation program will be event driven
     *  with the ordering of computations determined by chronological ordering
     *  of scheduled events.
     */
    public static void run() {
        while (!eventSet.isEmpty()) {
            Event e = eventSet.remove();
            time = e.time;
            e.act.trigger();
        }
    }
}
