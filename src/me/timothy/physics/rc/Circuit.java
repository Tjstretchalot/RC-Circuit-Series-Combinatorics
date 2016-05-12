package me.timothy.physics.rc;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Specifically the type of circuit described in the class-level documentation, resistors
 * in series in series with capacitors in series. 
 * 
 * @author Timothy Moore
 */
public class Circuit {
	// This is not a primitive so it can be calculated
	// only when needed
	private Double timeConstant;
	
	/**
	 * The capacitors in the circuit
	 */
	public List<Capacitor> capacitors;
	
	/**
	 * The resistors in the circuit
	 */
	public List<Resistor> resistors;
	
	/**
	 * Creates a circuit with the specified capacitors and resistors in series
	 * 
	 * @param capacitors the capacitors
	 * @param resistors the resistors
	 * @param rcCircuitGenerator TODO
	 */
	public Circuit(List<Capacitor> capacitors, List<Resistor> resistors) {
		this.capacitors = capacitors;
		this.resistors = resistors;
	}
	
	/**
	 * The time constant, &tau;, of the circuit measured in seconds.
	 * @return &tau; (seconds)
	 */
	public double getTimeConstant() {
		if(timeConstant == null) {
			timeConstant = calculateTimeConstant();
		}
		
		return timeConstant.doubleValue();
	}
	
	/**
	 * The effective resistance of the circuit
	 * @return R (Ohms)
	 */
	public int getEffectiveResistanceOhms() {
		return resistors.stream().collect(Collectors.summingInt(r -> r.getResistanceOhms()));
	}
	
	/**
	 * The effective capacitance of the circuit
	 * @return C (&mu;F)
	 */
	public double getEffectiveCapacitanceMicroFarads() {
		return 1. / capacitors.stream().collect(Collectors.summingDouble(c -> 1. / c.getCapacitanceMicroFarads()));
	}
	
	private double calculateTimeConstant() {
		// Combine resistors: Re = R1 + R2 + ... + Rn
		int effectiveResistanceOhms = getEffectiveResistanceOhms();
		
		// Combine capacitors: 1 / Ce = 1 / C1 + 1 / C2 + ... + 1 / Cn
		double effectiveCapacitanceMicroFarads = getEffectiveCapacitanceMicroFarads();
		
		// tau = RC
		return effectiveResistanceOhms * effectiveCapacitanceMicroFarads * Math.pow(10, -6);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capacitors == null) ? 0 : capacitors.hashCode());
		result = prime * result + ((resistors == null) ? 0 : resistors.hashCode());
		result = prime * result + ((timeConstant == null) ? 0 : timeConstant.hashCode());
		return result;
	}

	/**
	 * Not dependent on the order of the capacitors list or resistors list
	 * 
	 * @param obj the other object
	 * @return if it logically equivalent to this object
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Circuit other = (Circuit) obj;
		if (capacitors == null) {
			if (other.capacitors != null)
				return false;
		} else if (!capacitors.containsAll(other.capacitors) || !other.capacitors.containsAll(capacitors))
			return false;
		if (resistors == null) {
			if (other.resistors != null)
				return false;
		} else if (!resistors.containsAll(other.resistors) || !other.resistors.containsAll(resistors))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("Time Constant: %s | Resistors: %s | Capacitors: %s | Eff Resistance: %s | Eff Capacitance: %s",
			String.format("%1$,.8f", getTimeConstant()), resistors.toString(), capacitors.toString(), 
			String.format("%dohms", getEffectiveResistanceOhms()),
			String.format("%1$,.5fuF", getEffectiveCapacitanceMicroFarads()));
	}
	
	
}