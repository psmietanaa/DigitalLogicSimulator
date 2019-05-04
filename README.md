# Digital Logic Simulator

**This Java program reads a logic circuit description, builds a data structure using gate and wire objects,
and then simulates the circuit. A low-pass filter has been included for a realistic behavior.**

It supports 4 kinds of gates and wires:
- **Input Gates**
    - Input gates have three attributes: 
        - an initial value, which must be 1 (for true) or 0 (for false)
        - a delay, which must be a positive floating point number
        - a change count, which must be a non-negative integer
    - Example: gate x input 1 1.0 0
- **Xor Gates**
    - Xor gates have only one attribute:
        - a delay, which must be a positive floating point number
    - Example: gate x xor 1.0
- **Threshold Gates**
    - Threshold gates have two attributes:
        - a threshold, which must be a non-negative integer
        - a delay, which must be a positive floating point number
    - Example: gate x threshold 2 1.0
- **Output Gates**
    - Output gates have no attributes.
    - Example: gate x output
- **Wires**
    - Wires have three attributes:
        - a source gate
        - a delay, which must be a positive floating point number
        - any number of destinations, where each destination is proceeded by the delay to that destination
    - Example: wire a 1.0 b
    - Example: wire b 1.0 a 1.0 a

**Additionally, the simulation performs the following sanity checks.**
- Input gates may not be destinations of any wires.
- Output gates may not be sources for any wires.
- Xor gates must be the destination of exactly 2 wires.
- If a threshold gate is the destination of n wires, it may not have a threshold greater than n.

**Instructions:**

The simulation runs **only** if there were no warnings issued during the building of the model.

If the simulation encounters any errors, it will print them to the screen.

**Example input:**
```
-- this is a comment
gate a input 1 1.0 2
gate b output -- this is a comment too
wire a 0.1 b
```
**Example simulation output:**
```
time 0.0 0->1 Gate a input 1 1.0 2
time 0.1 0->1 Wire a 0.1 b
time 1.0 1->0 Gate a input 1 1.0 1
time 1.1 1->0 Wire a 0.1 b
time 2.0 0->1 Gate a input 1 1.0 0
time 2.1 0->1 Wire a 0.1 b
```

**Contents:**
```
README.md               -- this file
classes                 -- the @ file for the javac command

Simulation.java         -- utility package (standalone)
Errors.java             -- utility package (standalone)

ScanSupport.java        -- utility package

Wire.java               -- wires that connect Gates
Gate.java               -- gates are connected by wires
InputCountGate.java     -- a subclass of Gate
ThresholdGate.java      -- a subclass of InputCountGate
XorGate.java            -- a subclass of InputCountGate
InputGate.java          -- a subclass of Gate
OutputGate.java         -- a subclass of Gate

LogicSimulator.java     -- the main program

tests                   -- an executable testing script

Makefile                -- instructions for building and testing it
```
In the above list of .java files, blank lines separate levels in the
dependency hierarchy. Each level depends on levels above it.

**Usage:**

To **build** the program, use the command "make" or "make LogicSimulator"

To **run** the tests, use the command "make tests"

To **view documentation** of the program, use the command "make javadoc"

To **clean** the directory, use the command "make clean"
