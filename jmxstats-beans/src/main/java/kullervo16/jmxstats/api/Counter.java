
package kullervo16.jmxstats.api;

/**
 * The counter allows you to count events. 
 * 
 * All counter implementations are thread-safe.
 * The initial value of the counter is 0.
 * 
 * @author jef
 */
public interface Counter extends Stat{
    
    /**
     * Increments the counter with 1.
     * @return the instance (fluent api)
     */
    public Counter increment();
    
    /**
     * Increment the counter with the provided increment.
     * @param inc 
     * @return the instance (fluent api)
     */
    public Counter increment(long inc);
    
    /**
     * Resets the counter back to its initial position (0).
     * @return 
     */
    public Counter reset();
    
    /**
     * 
     * @return the current value of the counter. 
     */
    public long getValue();
    
    /**
     * Atomically returns the value and resets the counter
     * @return the current value of the counter. 
     */
    public long readValueAndReset();
    
    
    /**
     * This method specifies the counters unit
     * @param unit 
     */
    public void setUnit(String unit);
    
    
    /**
     * 
     * @return the description of the counter (useful when browsing the JMX tree) 
     */
    public String getUnit();
    
}
