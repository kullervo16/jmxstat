/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kullervo16.jmxstats.features.counter;

import kullervo16.jmxstats.features.counter.MaxValue;
import java.util.LinkedList;
import java.util.List;
import kullervo16.jmxstats.api.CounterDecorator;
import kullervo16.jmxstats.factories.CounterFactory;
import kullervo16.jmxstats.factories.JmxFactoryProducer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author jeve
 */
public class ValueLoggerTest {
    
    public ValueLoggerTest() {
    }

    
    @Test
    public void testCreate() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(ValueLogger.class);
         CounterDecorator decoratedCounter =  factory.createDecoratedCounter(null, featureList);
         decoratedCounter.setDescription("boe");
         assertEquals(0, decoratedCounter.getValue());                 
    }
    
    @Test
    public void testLog() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(ValueLogger.class);
         CounterDecorator decoratedCounter =  factory.createDecoratedCounter(null, featureList);
         decoratedCounter.increment();
         decoratedCounter =  factory.createDecoratedCounter("with id", featureList);
         decoratedCounter.increment();
    }
}
