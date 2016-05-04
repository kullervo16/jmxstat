
package kullervo16.jmxstats.impl;

import java.io.Serializable;

/**
 * This class models a simple counter.
 * 
 * @author jef
 */
public class CounterImpl implements CounterImplMXBean, Serializable{
    
    private volatile long value;
    private String description;
    private final String id;
    private String unit;

    public CounterImpl(String id) {
        this.id = id;
    }
                    
    @Override
    public CounterImpl increment() {
        this.value++;
        return this;
    }

    @Override
    public CounterImpl increment(long inc) {
        this.value += inc;
        return this;
    }

    @Override
    public CounterImpl reset() {
        this.value = 0L;
        return this;
    }
    
    

    @Override
    public long getValue() {
        return this.value;
    }

    @Override
    public long getValueAndReset() {
        long res = this.value;
        reset();
        return res;
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

    @Override
    public String getId() {
        return this.id;
    }
   
}
