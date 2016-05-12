package me.timothy.physics.rc;

/**
 * A very simple description of a resistor, which
 * has some resistance measured in ohms.
 * 
 * @author Timothy
 */
public class Resistor {
	private int resistanceOhms;
	
	/**
	 * Initializes the resistor with the specified resistance
	 * @param resistanceOhms resistance in &#8486;
	 */
	public Resistor(int resistanceOhms) {
		this.resistanceOhms = resistanceOhms;
	}
	
	/**
	 * Fetches the resistance of this circuit, measured in ohms
	 * @return the resistance in &#8486;
	 */
	public int getResistanceOhms() {
		return resistanceOhms;
	}
	
	@Override
	public String toString() {
		return resistanceOhms + "ohms";
	}
}