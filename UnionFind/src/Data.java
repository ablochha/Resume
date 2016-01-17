/*
 * Andrew Bloch-Hansen
 * 250473076
 */

public class Data {

	private int _frequency;
	private int _label;
	
	public Data(int frequency, int label) {
		
		_frequency = frequency;
		_label = label;
		
	} //end Data
	
	public int getFrequency() {
		
		return _frequency;
		
	} //end getFrequency
	
	public void setFrequency(int frequency) {
		
		_frequency = frequency;
		
	} //end setFrequency
	
	public void incrementFrequency() {
		
		_frequency++;
		
	} //end incrementFrequency
	
	public int getLabel() {
		
		return _label;
		
	} //end getLabel
	
	public void setLabel(int label) {
		
		_label = label;
		
	} //end setLabel
	
} //end Data