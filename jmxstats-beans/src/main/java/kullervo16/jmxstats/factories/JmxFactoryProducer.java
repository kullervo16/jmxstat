
package kullervo16.jmxstats.factories;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Class to produce the factories
 * @author jef
 */
@ApplicationScoped
public class JmxFactoryProducer {
    
    private final static String prefix = "kullervo16.jmx:";
    
    private final CounterFactory counterFactory = new CounterFactory(prefix);

    @Produces
    public CounterFactory getCounterFactory() {
        return this.counterFactory;
    }
}
