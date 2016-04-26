
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
    private String description;
    private String id;
    private String unit;

    public Counter(String id) {
        this.id = id;
    }

    public Counter() {
        
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
    public Counter reset() {
        this.value = 0L;
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
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "Counter{" + "value=" + value + ", description=" + description + ", id=" + id + ", unit=" + unit + '}';
    }    
    
}
