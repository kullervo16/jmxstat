
package kullervo16.jmxstats.factories;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This class will listen to a web lifecycle and make sure the JMX beans are
 * unregistered when the webapplication is undeployed (to make sure the MBeans do not linger)
 * @author jef
 */
@WebListener
public class WebLifecycleHandler implements ServletContextListener{

    @Inject
    JmxFactoryProducer factoryProducer;
        

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        factoryProducer.getCounterFactory().unregisterBeans();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // not interested
    }
}
