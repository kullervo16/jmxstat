
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
        counterFactory.getJmxCounter(TYPEDEMO_COUNTER).setDescription("Counter to count the number of times a page has been fetched");
        counterFactory.getJmxCounter(TYPEDEMO_COUNTER).setUnit("page loads");
    }
    
    public long getCounterValue() {
        return myCounter.increment().getValue();
        
    }
        
    public long getJmxValue() {        
        return counterFactory.getJmxCounter(TYPEDEMO_COUNTER).increment().getValue();        
    }
    private static final String TYPEDEMO_COUNTER = "type=demoCounter";
}
