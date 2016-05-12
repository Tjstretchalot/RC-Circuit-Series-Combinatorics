package me.timothy.physics.rc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

/**
 * <h1>RC Circuit Design Project</h1>
 * 
 * <h2>Overview</h2>
 * <p>For this project, the task is to design a circuit to within some percentage 
 * of a specific time constant using a combination of specified resistors and capacitors
 * that the physics lab has available.</p>
 * 
 * <h2>Definitions</h2>
 * <ul>
 *   <li><b>Electrical Circuit</b> - A path in which electrons flow. Conceptually, this can be thought of as
 *       a water fountain, where some parts, usually batteries, act as a pump and other elements represent areas
 *       where the water is able to fall.</li>
 *   <li><b>Battery</b> - A battery usually puts energy into a circuit in order to maintain a certain voltage across
 *       its ends. Conceptually, this acts as a pump</li>
 *   <li><b>Resistor</b> - A resistor converts electrical energy in the form of current into heat. Conceptually,
 *       a resistor is a drop in height.</li>
 *   <li><b>Capacitor</b> - A capacitor is capable of storing energy. Conceptually, a charging capacitor will
 *       act like a small reservoir that the pump will fill up when it is running, and a discharging capacitor
 *       is when that reservoir is allowed to fall further down.</li>
 *   <li><b>RC Time constant</b> - The time required to charge the capacitor through a resistor by ~63.2% the 
 *       difference between initial value and final value, or discharge the capacitor to ~36.8% of its current
 *       value.</li>
 * </ul>
 * 
 * <h2>Assumptions</h2>
 * <p>In this circuit generator, it is assumed that capacitors and resistors are only placed in series and are
 * not seperated by the other one - that is to say, the circuit might look like "R R C C B" but not like "R C R C B".
 * Furthermore, it is assumed the resistors and capacitors are ideal and the wire has negligible resistance compared 
 * to resistors.</p>
 * 
 * <h2>Quantitative Analysis</h2>
 * <h3>Resistors in Series</h3>
 * <p>Resistors in series can be treated as one resistor by noting that the charge through the resistors <i>must</i>
 * remain constant by Kirchoff's Current Law. Furthermore, the voltage across the resistors will be the sum of the
 * voltages across each resistor. Finally, the voltage across a resistor is equal to the current across the resistor
 * multiplied by its resistance (Ohm's Law). Bringing this all together:</p>
 * <pre>Vtotal = V1 + V2 + ... + Vi</pre>
 * <pre>I * Reffective = I * R1 + I * R2 + .... + I * Ri</pre>
 * <pre>Reffective = R1 + R2 + ... + Ri</pre>
 * 
 * <h3>Capacitors in Series</h3>
 * <p>Capacitors in series can be treated as one capacitor by noting that the charge through the capacitors <i>must</i>
 * remain constant by Kirchoff's Current Law. Furthermore, the voltage across the capacitors will be the sum of the 
 * voltages across each capacitor. Finally, the voltage across a capacitor is equal to the charge on one end of the
 * capacitor divided by its capacitance (by the definition of capacitance). Bringing this all together:</p>
 * <pre>Vtotal = V1 + V2 + ... + Vi</pre>
 * <pre>Q / Ceffective = Q / C1 + Q / C2 + .... + Q / Ci</pre>
 * <pre>1 / Ceffective = 1 / C1 + 1 / C2 + .... + 1 / Ci</pre>
 * 
 * <h3>Time Constant</h3>
 * <p>The derivation for the time constant is too in-depth for this documentation, however it can be found 
 * at <a href="https://en.wikipedia.org/wiki/RC_time_constant">the wikipedia page</a>. The final equation is
 * simply:</p>
 * <pre>&tau; = RC</pre>
 * <p>Where &tau; is the time constant in seconds, R is the resistance of the circuit, and C is the capacitance
 * of the circuit. If the circuit cannot be simplified to one resistor and one capacitor, this equation does not
 * work and &tau; must be found using Kirchoff's Loop Laws.</p>
 * 
 * @author Timothy Moore
 * @see <a href="https://en.wikipedia.org/wiki/Kirchhoff%27s_circuit_laws">Kirchoff's Circuit Laws</a>
 * @see <a href="https://en.wikipedia.org/wiki/Ohm%27s_law">Ohm's Law</a>
 * @see <a href="https://en.wikipedia.org/wiki/Capacitance">Capacitance</a>
 */
public class RCCircuitGenerator {
	
	//@SuppressWarnings("unused") // In case time constant near is null
	/*public static void main(String[] args) {
		/*
		 * These are the parameters that can be modified to generate different circuits
		 *
		final int numResistors = 3;
		final int numCapacitors = 2;
		final int[] allowedResistorOhms = {5, 10, 25, 50, 100};
		final int[] allowedCapacitatorMicroFarads = {100, 250, 400};
		final Double timeConstantNear = 0.01; // Use null to ignore.
		final Double timeConstantNearness = 0.2; // % / 100 of timeConstantNear. Use 0 for exact. Ignored (and can be null) if timeConstantNear is null
		
		RCCircuitGenerator generator = new RCCircuitGenerator();
		generator.setResistors(allowedResistorOhms);
		generator.setCapacitors(allowedCapacitatorMicroFarads);
		
		List<Circuit> circuits = generator.getAllPossibleCircuits(numResistors, numCapacitors);
		
		if(timeConstantNear != null) {
			final double minTimeConstant = timeConstantNear.doubleValue() - timeConstantNear.doubleValue() * timeConstantNearness.doubleValue();
			final double maxTimeConstant = timeConstantNear.doubleValue() + timeConstantNear.doubleValue() * timeConstantNearness.doubleValue();
			
			circuits = circuits.stream().filter(c -> (c.getTimeConstant() >= minTimeConstant && c.getTimeConstant() <= maxTimeConstant)).collect(Collectors.toList());
		}
		
		System.out.printf("Found %d circuits with %d resistors%s %d capacitors%s\n", circuits.size(), numResistors,
				(timeConstantNear == null ? " and" : ","), numCapacitors, 
				(timeConstantNear == null ? "" : String.format(", and a time constant within %f%% of %fs", timeConstantNearness.doubleValue()*100, timeConstantNear)));
		System.out.printf("  Allowed Resistors (Ohms): %s\n", Arrays.toString(allowedResistorOhms));
		System.out.printf("  Allowed Capacitors (uF):  %s\n", Arrays.toString(allowedCapacitatorMicroFarads));
		for(int i = 0; i < circuits.size(); i++) {
			System.out.printf("  Circuit #%d:\n    %s\n", i+1, circuits.get(i).toString());
		}
	}*/
	
	private List<Resistor> resistors;
	private List<Capacitor> capacitors;
	
	
	/**
	 * Initializes the circuit generator with no resistors or capacitors.
	 */
	public RCCircuitGenerator() {
		resistors = new ArrayList<>();
		capacitors = new ArrayList<>();
	}
	
	/**
	 * Sets the allowed resistors to be the specified resistors in ohms.
	 * 
	 * @param resistancesOhms the resistances in ohms
	 */
	public void setResistors(int... resistancesOhms) {
		resistors.clear();
		for(int rOhm : resistancesOhms) {
			resistors.add(new Resistor(rOhm));
		}
	}
	
	/**
	 * Sets the allowed resistors to the specified resistors
	 * 
	 * @param resistors the new resistors
	 */
	public void setResistors(List<Resistor> resistors) {
		this.resistors = resistors;
	}
	/**
	 * Sets the allowed capacitors to be the specified capacitors in ohms
	 * 
	 * @param capacitancesMicroFarads the capacitors in micro farads
	 */
	public void setCapacitors(int... capacitancesMicroFarads) {
		capacitors.clear();
		for(int cMicroFarads : capacitancesMicroFarads) {
			capacitors.add(new Capacitor(cMicroFarads));
		}
	}
	
	/**
	 * Sets the allowed capacitors to the specified capacitors
	 * 
	 * @param capacitors the new capacitors
	 */
	public void setCapacitors(List<Capacitor> capacitors) {
		this.capacitors = capacitors;
	}
	
	/**
	 * Fetches every possible combination of circuits with at most the specified 
	 * number of resistors and capacitors. Circuits with different ordering of
	 * resistors or capacitors are not considered unique.
	 * 
	 * @param maxNumResistors maximum number of resistors
	 * @param maxNumCapacitors maximum number of capacitors
	 * @return all possible combinations of circuits.
	 */
	public List<Circuit> getAllPossibleCircuits(int maxNumResistors, int maxNumCapacitors) {
		List<Circuit> result = new ArrayList<>();
		
		ICombinatoricsVector<Resistor> resCombVector = Factory.createVector(resistors);
		ICombinatoricsVector<Capacitor> capCombVector = Factory.createVector(capacitors);
		
		List<ICombinatoricsVector<Resistor>> resistorCombinations = new ArrayList<>();
		for(int numResistors = 1; numResistors <= maxNumResistors; numResistors++) {
			Generator<Resistor> resistorGen = Factory.createMultiCombinationGenerator(resCombVector, numResistors);
			resistorCombinations.addAll(resistorGen.generateAllObjects());
		}
		List<ICombinatoricsVector<Capacitor>> capacitorCombinations = new ArrayList<>();
		for(int numCapacitors = 1; numCapacitors <= maxNumCapacitors; numCapacitors++) {
			Generator<Capacitor> capacitorGen = Factory.createMultiCombinationGenerator(capCombVector, numCapacitors);
			capacitorCombinations.addAll(capacitorGen.generateAllObjects());
		}
		
		for(ICombinatoricsVector<Resistor> resistorsVect : resistorCombinations) {
			for(ICombinatoricsVector<Capacitor> capacitorsVect : capacitorCombinations) {
				result.add(new Circuit(capacitorsVect.getVector(), resistorsVect.getVector()));
			}
		}
		
		return result;
	}
	
	/**
	 * Returns a subset of the circuits provided where the circuits time constant
	 * is within <code>tolerance</code> of <code>timeConstant</code>
	 * 
	 * @param circuits the circuits to use
	 * @param timeConstant the time constant
	 * @param tolerance allowed tolerance (or 0)
	 * @return the filtered circuits
	 */
	public static List<Circuit> filterCircuitsByTimeConstant(List<Circuit> circuits, double timeConstant, double tolerance) {
		return circuits.stream().filter(c -> Math.abs(c.getTimeConstant() - timeConstant) <= tolerance).collect(Collectors.toList());
	}
}
