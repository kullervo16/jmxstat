package kullervo16.jmxstats.features.counter;

import kullervo16.jmxstats.api.Counter;
import kullervo16.jmxstats.impl.CounterDecoratorImpl;

/**
 * A gauge is a counter that can be decremented as well
 * @author jeve
 */
public class Gauge extends CounterDecoratorImpl {

    public Gauge(Counter next) {
        super(next);
    }

    /**
     * Decrement the value of the counter by 1.
     * @return 
     */
    public Gauge decrement() {
        this.nextInChain.increment(-1);
        return this;
    }
    
    public Gauge decrement(long decr) {
        this.nextInChain.increment(-1 * decr);
        return this;
    }
}
