
package kullervo16.jmxstats.factories;

import javax.inject.Named;
import kullervo16.jmxstats.api.Counter;
import kullervo16.jmxstats.impl.BasicCounterImpl;

/**
 * This class produces counter instances.
 * 
 * @author jef
 */
@Named
public class CounterFactory {

    
    public Counter createCounter() {
        return new BasicCounterImpl();
    }
}
