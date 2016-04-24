
package kullervo16.jmxstats.impl;

import java.io.Serializable;
import kullervo16.jmxstats.api.CounterMXBean;

/**
 * This class models a simple counter.
 * 
 * @author jef
 */
public class Counter implements CounterMXBean, Serializable{
    
    private int value;
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
    public Counter increment(int inc) {
        this.value += inc;
        return this;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

}
