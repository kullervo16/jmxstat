package kullervo16.jmxstats.features.counter;

import kullervo16.jmxstats.impl.CounterDecoratorImpl;
import kullervo16.jmxstats.api.Counter;

/**
 * Counter decorator that restricts the counter to a minimum value. It throws IllegalStateException when you try to
 * force the counter above its allowed maximum value (or a minimum above the current value). Mostly useful with a Gauge.
 * 
 * @author jeve
 */
public class MinValue extends CounterDecoratorImpl {

    private long minValue;
    
    public MinValue(Counter next) {
        super(next);
    }
    
    /**
     * 
     * @param min the new minimum
     * @throws IllegalStateException when the current value is lower than the requested minimum
     */
    public void setMinValue(long min) {
        if(this.nextInChain.getValue() < min) {
            throw new IllegalStateException("Current value ("+this.nextInChain.getValue()+") is lower than the requested minimum ("+min+")");
        }
        this.minValue = min;
    }

    /**
     * 
     * @param inc
     * @return 
     * @throws IllegalStateException when the current value is higher than the requested maximum
     */
    @Override       
    public synchronized Counter increment(long inc) {
        if(this.nextInChain.getValue() + inc < this.minValue) {
           throw new IllegalStateException("Requested increment ("+inc+") cannot be applied to "+this.nextInChain.getValue()+" as it would surpass the minimum value ("+this.minValue+")");
        }
        this.nextInChain.increment(inc); 
        return this;
    }

    /**
     * 
     * @return 
     * @throws IllegalStateException when the current value is higher than the requested maximum
     */
    @Override
    public Counter increment() {        
        return this.increment(1L);
    }
    
    

}
