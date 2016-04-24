
package kullervo16.jmxstats.factories;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import kullervo16.jmxstats.api.Counter;

/**
 * This class produces counter instances.
 * 
 * @author jef
 */
public class CounterFactory {
    
    private final Map<String,Counter> counterMap = new HashMap<>();
    private final Object lock = new Object();
    private final String prefix;

    public CounterFactory(String prefix) {
        this.prefix = prefix;
    }
    
    

    
    public Counter createCounter() {
        return new kullervo16.jmxstats.impl.Counter();
    }
    
    /**
     * This methods returns a counter for a given id. If the counter does not exist yet, it will be created. Otherwise,
     * the existing counter is returned.
     * 
     * @param id
     * @return   
     */
    public Counter getJmxCounter(String id)  {
        try {
            synchronized (this.lock) {
                if(this.counterMap.containsKey(id)) {
                    return this.counterMap.get(id);
                }
                this.counterMap.put(id, this.createCounter());
            }
            // release the lock, we can safely allow others in... we have the reference so they can begin counting while
            // we continue the registration process
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName mxbeanName = new ObjectName(prefix+id);
            mbs.registerMBean(this.counterMap.get(id), mxbeanName);
            return this.counterMap.get(id);
        } catch (MalformedObjectNameException ex) {
            throw new IllegalArgumentException("Cannot register "+id, ex);
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException ex) {
            throw new IllegalStateException("Cannot register "+id, ex);
        } 
    }
}
