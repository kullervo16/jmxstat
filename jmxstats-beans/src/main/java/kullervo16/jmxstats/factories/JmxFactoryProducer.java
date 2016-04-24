
package kullervo16.jmxstats.factories;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Class to produce the factories
 * @author jef
 */
@ApplicationScoped
public class JmxFactoryProducer {
    
    private CounterFactory counterFactory = new CounterFactory();

    @Produces
    public CounterFactory getCounterFactory() {
        return this.counterFactory;
    }
}
