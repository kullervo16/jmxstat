
package kullervo16;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
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
        
    public int getJmxValue() {        
        return counterFactory.getJmxCounter("type=demoCounter").increment().getValue();        
    }
}
