

package kullervo16.jmxstats.api;

/**
 * Base class for common stuff in the different statistics.
 * 
 * @author jef
 */
public interface Stat {

    /**
     *
     * @return the description of the counter (useful when browsing the JMX tree)
     */
    String getDescription();

    /**
     *
     * @return the counter ID (if any)
     */
    String getId();

    /**
     * This method sets the description of what is counted
     * @param description
     */
    void setDescription(String description);

}
