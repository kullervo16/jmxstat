
package kullervo16.jmxstats.impl;

import java.io.Serializable;
import kullervo16.jmxstats.api.Counter;

/**
 * This class models a simple counter.
 * 
 * @author jef
 */
public class BasicCounterImpl implements Counter, Serializable{
    
    private int value;

    @Override
    public BasicCounterImpl increment() {
        this.value++;
        return this;
    }

    @Override
    public BasicCounterImpl increment(int inc) {
        this.value += inc;
        return this;
    }

    @Override
    public int getValue() {
        return this.value;
    }

}
