package kullervo16.jmxstats.features.counter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import kullervo16.jmxstats.impl.CounterDecoratorImpl;
import kullervo16.jmxstats.api.Counter;

/**
 * Counter decorator that allows you to register propertychangelisteners. After changing the value, the
 * listeners will be informed (in a synchronous way).
 * 
 * @author jeve
 */
public class EventEmitter extends CounterDecoratorImpl {

    private final List<PropertyChangeListener> listeners = new LinkedList<>();
    
    public EventEmitter(Counter next) {
        super(next);
    }
    
    public synchronized void addListener(PropertyChangeListener pcl) {
        if(!this.listeners.contains(pcl)) {
            this.listeners.add(pcl);
        }
    }
    
    public synchronized void removeListener(PropertyChangeListener pcl) {
        this.listeners.remove(pcl);
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
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "value", currentValue, this.getValue());
        for(PropertyChangeListener pcl : this.listeners) {
            pcl.propertyChange(pce);
        }
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
