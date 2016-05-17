
package kullervo16.jmxstats.impl;

import java.io.Serializable;
import kullervo16.jmxstats.api.Flag;

/**
 * This class models a flag.
 * 
 * @author jef
 */
public class FlagImpl implements FlagImplMXBean, Serializable{
    
    private volatile boolean value;
    private String description;
    private final String id;

    public FlagImpl(String id) {
        this.id = id;
    }
                          

    @Override
    public String getDescription() {
        return this.description;
    }
    
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

   

    @Override
    public String toString() {
        return "Flag{" + "value=" + value + ", description=" + description + ", id=" + id +'}';
    }    

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Flag set() {
        this.value = true;
        return this;
    }

    @Override
    public Flag unset() {
        this.value  = false;
        return this;
    }

    @Override
    public Flag toggle() {
        this.value = !this.value;
        return this;
    }

    @Override
    public boolean isSet() {
        return this.value;
    }
     
}
