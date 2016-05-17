
package kullervo16.jmxstats.factories;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import kullervo16.jmxstats.api.Flag;
import kullervo16.jmxstats.impl.FlagImpl;

/**
 * This class produces counter instances.
 * 
 * @author jef
 */
public class FlagFactory {
    
    private final Map<String,Flag> flagMap = new HashMap<>();
    private final Object lock = new Object();
    private final String prefix;

    public FlagFactory(String prefix) {
        this.prefix = prefix;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    
    public Flag createFlag() {
        return this.createFlag(null);
    }
    
    public Flag createFlag(String id) {
        return new FlagImpl(id);    
    }
        
        
    
    /**
     * This methods returns a Flag for a given id. If the flag does not exist yet, it will be created. Otherwise,
     * the existing flag is returned.
     * 
     * @param id     
     * @return   
     */
    public Flag getJmxFlag(String id)  {
       
        try {
            synchronized (this.lock) {
                if(this.flagMap.containsKey(id)) {
                    return this.flagMap.get(id);
                }
                this.flagMap.put(id, this.createFlag(id));
            }
            // release the lock, we can safely allow others in... we have the reference so they can begin counting while
            // we continue the registration process
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName mxbeanName = new ObjectName(prefix+id);
            try {
                mbs.registerMBean(this.flagMap.get(id), mxbeanName);
            }catch(InstanceAlreadyExistsException iaee) {
                // well, this is most probably a reload of our application.. the value was not in our map, but still in the MBeanServer... remove and reregister
                mbs.unregisterMBean(mxbeanName);
                mbs.registerMBean(this.flagMap.get(id), mxbeanName);
            }
            return this.flagMap.get(id);
        } catch (MalformedObjectNameException ex) {
            this.flagMap.remove(id);
            throw new IllegalArgumentException("Cannot register "+id, ex);
        } catch (InstanceNotFoundException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException ex) {
            this.flagMap.remove(id);
            throw new IllegalStateException("Cannot register "+id, ex);
        } 
    }

    public void unregisterBeans() {
        synchronized (this.lock) {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            
            for(String name : this.flagMap.keySet()) {
                try {
                    ObjectName mxbeanName = new ObjectName(prefix+name);                
                    mbs.unregisterMBean(mxbeanName);
                }catch(InstanceNotFoundException | MBeanRegistrationException | MalformedObjectNameException registEx) {
                    // well' what can we do... just ignore
                }
            }
            System.out.println("Unregistered "+this.flagMap.size()+" JMX flags");
            this.flagMap.clear();
        }
    }
    
    public void unregisterFlag(String id) {        
        synchronized (this.lock) {
            // do this when nobody is looping
            this.flagMap.remove(id);
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
