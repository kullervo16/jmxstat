
package kullervo16.jmxstats.api;

/**
 * The flag models a boolean. 
 * 
 * The initial value of the flag is false.
 * 
 * @author jef
 */
public interface Flag extends Stat {
    
    /**
     * Sets the flag to true, whatever the value was.
     * @return the instance (fluent api)
     */
    public Flag set();
    
    /**
     * Sets the flag to false, whatever the value was.
     * @return the instance (fluent api)
     */
    public Flag unset();
    
    /**
     * Resets to the opposite position.
     * @return 
     */
    public Flag toggle();
    
    /**
     * 
     * @return the current value of the flag. 
     */
    public boolean isSet();
        
        
}
