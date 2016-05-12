package me.timothy.physics.rc;

/**
 * A very simply description of a capacitor, which has
 * some capacitance in micro-farads.
 * 
 * @author Timothy
 */
public class Capacitor {
	private int capacitanceMicroFarads;
	
	/**
	 * Creates the capacitor initialized to the specified
	 * capacitance
	 * 
	 * @param capacitanceMicroFarads capacitance in &mu;F
	 */
	public Capacitor(int capacitanceMicroFarads) {
		this.capacitanceMicroFarads = capacitanceMicroFarads;
	}
	
	/**
	 * Fetch the capacitance in microfarads
	 * @return capacitance in &mu;F
	 */
	public int getCapacitanceMicroFarads() {
		return capacitanceMicroFarads;
	}
	
	@Override
	public String toString() {
		return capacitanceMicroFarads + "uF";
	}
}