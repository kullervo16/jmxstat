
package kullervo16;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import kullervo16.jmxstats.api.Counter;
import kullervo16.jmxstats.factories.CounterFactory;

/**
 * Managed bean to demonstrate counters
 * 
 * @author jef
 */
@ManagedBean(name = "counter")
@SessionScoped
public class CounterDemoBean {

    @Inject
    private CounterFactory counterFactory;
    
    private Counter myCounter;
    
    @PostConstruct
    public void setup() {
        myCounter = counterFactory.createCounter();
    }
    
    public int getCounterValue() {
        return myCounter.increment().getValue();
        
    }
        
}
