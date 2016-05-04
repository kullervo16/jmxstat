
package kullervo16.jmxstats.factories;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import kullervo16.jmxstats.api.Counter;
import kullervo16.jmxstats.api.CounterDecorator;

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
    
    public String getPrefix() {
        return this.prefix;
    }
    
    
    public Counter createCounter() {
        return this.createCounter(null);
    }
    
    public Counter createCounter(String id) {
        return new kullervo16.jmxstats.impl.CounterImpl(id);    
    }
    
    public CounterDecorator createDecoratedCounter(List<Class<? extends CounterDecorator>> featureList) {
        return this.createDecoratedCounter(null, featureList);
    }
    
    public CounterDecorator createDecoratedCounter(String id, List<Class<? extends CounterDecorator>> featureList)  {
        Counter counter = this.createCounter(id);
        if(featureList == null || featureList.isEmpty()) {
            throw new IllegalArgumentException("Passing a null/empty feature list is useless. Please use the appropriate API");
        }
        Counter next = counter;
        CounterDecorator result = null;
        Class ctrParam[] = new Class[1];
        ctrParam[0] = Counter.class;
        for(Class clazz : featureList) {            
            Object args[] = new Object[1];
            args[0] = next;
            try {
                Constructor constr = clazz.getConstructor(ctrParam);
           
                CounterDecorator newDecorator = (CounterDecorator) constr.newInstance(args);
                result = newDecorator;
                next = newDecorator;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new IllegalStateException("Cannot instantiate feature of class "+clazz, ex);
            }            
        }
        return result;
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
                this.counterMap.put(id, this.createCounter(id));
            }
            // release the lock, we can safely allow others in... we have the reference so they can begin counting while
            // we continue the registration process
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName mxbeanName = new ObjectName(prefix+id);
            try {
                mbs.registerMBean(this.counterMap.get(id), mxbeanName);
            }catch(InstanceAlreadyExistsException iaee) {
                // well, this is most probably a reload of our application.. the value was not in our map, but still in the MBeanServer... remove and reregister
                mbs.unregisterMBean(mxbeanName);
                mbs.registerMBean(this.counterMap.get(id), mxbeanName);
            }
            return this.counterMap.get(id);
        } catch (MalformedObjectNameException ex) {
            this.counterMap.remove(id);
            throw new IllegalArgumentException("Cannot register "+id, ex);
        } catch (InstanceNotFoundException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException ex) {
            this.counterMap.remove(id);
            throw new IllegalStateException("Cannot register "+id, ex);
        } 
    }

    public void unregisterBeans() {
        synchronized (this.lock) {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            
            for(String name : this.counterMap.keySet()) {
                try {
                    ObjectName mxbeanName = new ObjectName(prefix+name);                
                    mbs.unregisterMBean(mxbeanName);
                }catch(InstanceNotFoundException | MBeanRegistrationException | MalformedObjectNameException registEx) {
                    // well' what can we do... just ignore
                }
            }
            System.out.println("Unregistered "+this.counterMap.size()+" JMX counters");
            this.counterMap.clear();
        }
    }
    
    public void unregisterCounter(String id) {        
        synchronized (this.lock) {
            // do this when nobody is looping
            this.counterMap.remove(id);
        }
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName mxbeanName = new ObjectName(prefix+id);                
            mbs.unregisterMBean(mxbeanName);
        }catch(InstanceNotFoundException | MBeanRegistrationException | MalformedObjectNameException registEx) {
            // well' what can we do... just ignore
        }
    }
}
