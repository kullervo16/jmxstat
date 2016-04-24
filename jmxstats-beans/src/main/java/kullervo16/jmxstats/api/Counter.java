
package kullervo16.jmxstats.api;

/**
 * The counter allows you to count events. 
 * 
 * All counter implementations are thread-safe.
 * The initial value of the counter is 0.
 * 
 * @author jef
 */
public interface Counter {
    
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
    public Counter increment(int inc);
    
    /**
     * 
     * @return the current value of the counter. 
     */
    public int getValue();
}
