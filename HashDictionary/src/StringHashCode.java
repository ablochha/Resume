/*
 * Author: Andrew Bloch-Hansen
 */

public class StringHashCode implements HashCode {
	
	//This function uses horners equation to get the polynomial accumulation p(a)
	private int getPolynomialAccumulationValue(String str) {
	
		//variables P and a
		int polynomialVariableA = 33;
		int polynomialUnicodeValueP;
		
		//check if it is an empty word or not
		if (str.length() >= 1)
		{
				
			polynomialUnicodeValueP = str.codePointAt(str.length()-1);
			int i = str.length() - 2;
			
			//apply horners rule to each letter in the word
			while (i >= 0) 
			{
			
				polynomialUnicodeValueP = polynomialUnicodeValueP*polynomialVariableA + (int)str.codePointAt(i);
				i--;
			
			} //end while
			
			return polynomialUnicodeValueP;
			
		} //end if
		
		//return if -1 if it was an empty string
		return -1;
	
	} //end getPolyAccum

	//This function sends the dictionary the new number corresponding to the word
	public int giveCode(Object key) {
		
		String str = (String) key;
		
		int hashCode = getPolynomialAccumulationValue(str);
		
		return hashCode;
		
	} //end giveCode	

} //end StringHashCode
