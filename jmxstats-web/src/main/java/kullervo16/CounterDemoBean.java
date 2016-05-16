
package kullervo16;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import kullervo16.jmxstats.api.Counter;
import kullervo16.jmxstats.api.CounterDecorator;
import kullervo16.jmxstats.factories.CounterFactory;
import kullervo16.jmxstats.features.counter.Gauge;
import kullervo16.jmxstats.features.counter.MaxValue;
import kullervo16.jmxstats.features.counter.MinValue;

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
        
        List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
        featureList.add(Gauge.class);
        featureList.add(MinValue.class);
        featureList.add(MaxValue.class);
        Counter gauge = counterFactory.getJmxCounter(TYPEDEMO_GAUGE, featureList);
        gauge.setDescription("Gauge to monitor a virtual queue (min = 0, max = 10");
        gauge.setUnit("elements");
        counterFactory.getMinValue(gauge).setMinValue(0);
        counterFactory.getMaxValue(gauge).setMaxValue(10);
    }
    
    public long getCounterValue() {
        return myCounter.increment().getValue();
        
    }
        
    public long getJmxValue() {        
        return counterFactory.getJmxCounter(TYPEDEMO_COUNTER).increment().getValue();        
    }
    private static final String TYPEDEMO_COUNTER = "type=demoCounter";
    private static final String TYPEDEMO_GAUGE = "type=demoGauge";
}
