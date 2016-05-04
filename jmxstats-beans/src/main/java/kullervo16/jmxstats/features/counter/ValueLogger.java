package kullervo16.jmxstats.features.counter;

import kullervo16.jmxstats.impl.CounterDecoratorImpl;
import kullervo16.jmxstats.api.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Counter decorator that allows you to register propertychangelisteners. After changing the value, the
 * listeners will be informed (in a synchronous way).
 * 
 * @author jeve
 */
public class ValueLogger extends CounterDecoratorImpl {

    private final Logger logger = LoggerFactory.getLogger(Counter.class);
        
    public ValueLogger(Counter next) {
        super(next);
    }
        

    /**
     * 
     * @param inc
     * @return 
     * @throws IllegalStateException when the current value is higher than the requested maximum
     */
    @Override       
    public synchronized Counter increment(long inc) {
        long currentValue = this.getValue();
        this.nextInChain.increment(inc);         
        StringBuilder logLine = new StringBuilder("Counter ");
        if(this.nextInChain.getId() != null) {
            logLine.append(this.nextInChain.getId()).append(" ");
        }
        logLine.append("changed value from ").append(currentValue).append(" to ").append(this.getValue());
        this.logger.info(logLine.toString());
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
