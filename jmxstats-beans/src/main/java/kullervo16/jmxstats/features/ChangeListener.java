package kullervo16.jmxstats.features;

import kullervo16.jmxstats.impl.CounterDecoratorImpl;
import kullervo16.jmxstats.api.Counter;

/**
 * Counter decorator that allows you to register propertychangelisteners. After changing the value, the
 * listeners will be informed (in a synchronous way).
 * 
 * @author jeve
 */
public class ChangeListener extends CounterDecoratorImpl {

    private long maxValue;
    
    public ChangeListener(Counter next) {
        super(next);
    }
    
    /**
     * 
     * @param max the new maximum
     * @throws IllegalStateException when the current value is higher than the requested maximum
     */
    public void setMaxValue(long max) {
        if(this.nextInChain.getValue() > max) {
            throw new IllegalStateException("Current value ("+this.nextInChain.getValue()+") is higher than the requested maximum ("+max+")");
        }
        this.maxValue = max;
    }

    /**
     * 
     * @param inc
     * @return 
     * @throws IllegalStateException when the current value is higher than the requested maximum
     */
    @Override       
    public synchronized Counter increment(long inc) {
        if(this.nextInChain.getValue() + inc > this.maxValue) {
           throw new IllegalStateException("Requested increment ("+inc+") cannot be added to "+this.nextInChain.getValue()+" as it would surpass the maximum value ("+this.maxValue+")");
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
