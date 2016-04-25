
package kullervo16.jmxstats.impl;

import java.io.Serializable;
import kullervo16.jmxstats.api.CounterMXBean;

/**
 * This class models a simple counter.
 * 
 * @author jef
 */
public class Counter implements CounterMXBean, Serializable{
    
    private long value;
    private final String description;

    public Counter(String description) {
        this.description = description;
    }

    public Counter() {
        this.description = "";
    }
            


    @Override
    public Counter increment() {
        this.value++;
        return this;
    }

    @Override
    public Counter increment(long inc) {
        this.value += inc;
        return this;
    }

    @Override
    public long getValue() {
        return this.value;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return "Counter{" + "value=" + value + ", description=" + description + '}';
    }

    
}
