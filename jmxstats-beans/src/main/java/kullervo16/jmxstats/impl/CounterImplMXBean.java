

package kullervo16.jmxstats.impl;

import kullervo16.jmxstats.api.Counter;

/**
 * JMX marker interface. Business is taken over from the actual interface.
 * @author jef
 */
public interface CounterImplMXBean extends Counter {

    /**
     * 
     * @return the description of the counter (useful when browsing the JMX tree) 
     */
    public String getDescription();
    
    /**
     * 
     * @return the description of the counter (useful when browsing the JMX tree) 
     */
    public String getUnit();
}
