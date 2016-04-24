

package kullervo16.jmxstats.api;

/**
 * JMX marker interface. Business is taken over from the actual interface.
 * @author jef
 */
public interface CounterMXBean extends Counter {

    public String getDescription();
}
