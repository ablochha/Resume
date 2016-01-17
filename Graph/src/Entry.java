/*
 * by olga veksler
 */

// location aware entry for the Vector based heap

public class Entry<Key,Value> implements Position{
    private Key k;
    private Value v;
    private int location;
    
    public Entry(Key K, Value V, int loc){
        k = K;
        v = V;
        location = loc;
    }
    
    public Key   getKey()     {return k;}
    public Value getValue() {return v;}
    public void  changeValue(Value newV){ v = newV;}
    public void  changeKey(Key newK){k = newK;}
    public int   getLocation(){return location;}
    public void  setLocation(int l){location = l;}
    
    public int givePosition() {
        return(location);
    };      
 }