
package kullervo16.jmxstats.impl;

import kullervo16.jmxstats.api.Counter;

/**
 * The counterDecorator is to be extended by classes adding features to a counter. 
 * 
 * 
 * @author jef
 */
public abstract class CounterDecoratorImpl implements kullervo16.jmxstats.api.CounterDecorator{
    
    protected final Counter nextInChain;
    
    public CounterDecoratorImpl(Counter next) {
        this.nextInChain = next;
    }
    
    /**
     * Passes on the call to the next in line. Override to add functionality.
     * @return the instance (fluent api)
     */
    @Override
    public Counter increment() {
        this.nextInChain.increment();
        return this;
    }
    
    /**
     * Passes on the call to the next in line. Override to add functionality.
     * @param inc 
     * @return the instance (fluent api)
     */
    @Override
    public Counter increment(long inc) {
        this.nextInChain.increment(inc);
        return this;
    }
    
    /**
     * Passes on the call to the next in line. Override to add functionality.
     * @return 
     */
    @Override
    public Counter reset() {
        this.nextInChain.reset();
        return this;
    }
    
    /**
     * Passes on the call to the next in line. Override to add functionality.
     * @return 
     */    
    @Override
    public long getValue() {
        return this.nextInChain.getValue();
    }
    
    /**
     * Passes on the call to the next in line. Override to add functionality.
     * @param description 
     */
    @Override
    public void setDescription(String description) {
        this.nextInChain.setDescription(description);
    }
    
    /**
     * Passes on the call to the next in line. Override to add functionality.
     * @param unit 
     */
    @Override
    public void setUnit(String unit) {
        this.nextInChain.setUnit(unit);
    }
    
    /**
     * This method returns an instance of a specific counterdecorator class. It will traverse the chain
     * and return the first instance that matches the class. You can use this method to gain access to the
     * feature specific methods.
     * 
     * @param clazz
     * @return 
     */
    @Override
    public CounterDecoratorImpl getSpecificFeature(Class<? extends kullervo16.jmxstats.api.CounterDecorator> clazz) {
        if(clazz.isInstance(this)) {
            return this;
        }
        if(this.nextInChain instanceof CounterDecoratorImpl) {
            return ((CounterDecoratorImpl)this.nextInChain).getSpecificFeature(clazz);
        }
        return null;
    }

    @Override
    public long getValueAndReset() {
        return this.nextInChain.getValueAndReset();
    }

    @Override
    public String getDescription() {
        return this.nextInChain.getDescription();
    }

    @Override
    public String getUnit() {
        return this.nextInChain.getUnit();
    }

    @Override
    public String getId() {
        return this.nextInChain.getId();
    }
    
    
        
}
