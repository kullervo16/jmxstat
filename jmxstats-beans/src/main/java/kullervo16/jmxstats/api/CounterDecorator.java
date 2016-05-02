package kullervo16.jmxstats.api;

/**
 *
 * @author jeve
 */
public interface CounterDecorator extends Counter{

    /**
     * This method returns an instance of a specific counterdecorator class. It will traverse the chain
     * and return the first instance that matches the class. You can use this method to gain access to the
     * feature specific methods.
     * 
     * @param clazz
     * @return 
     */
    public CounterDecorator getSpecificFeature(Class<? extends CounterDecorator> clazz);
}
