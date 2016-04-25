
package kullervo16;

import java.io.Serializable;
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
public class CounterDemoBean implements Serializable{

    @Inject
    private CounterFactory counterFactory;
    
    private Counter myCounter;
    
    @PostConstruct
    public void setup() {
        myCounter = counterFactory.createCounter();
    }
    
    public long getCounterValue() {
        return myCounter.increment().getValue();
        
    }
        
    public long getJmxValue() {        
        return counterFactory.getJmxCounter("type=demoCounter","Counter to count the number of times a page has been fetched").increment().getValue();        
    }
}
