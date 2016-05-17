/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kullervo16.jmxstats;

import java.util.LinkedList;
import java.util.List;
import kullervo16.jmxstats.api.Counter;
import kullervo16.jmxstats.api.CounterDecorator;
import kullervo16.jmxstats.factories.CounterFactory;
import kullervo16.jmxstats.factories.JmxFactoryProducer;
import kullervo16.jmxstats.features.counter.EventEmitter;
import kullervo16.jmxstats.features.counter.Gauge;
import kullervo16.jmxstats.features.counter.MaxValue;
import kullervo16.jmxstats.impl.CounterImpl;
import kullervo16.jmxstats.impl.CounterDecoratorImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author jeve
 */
public class CounterDecoratorTest {
    
    public CounterDecoratorTest() {
    }

    @Test
    public void testDefaultDecorationLogic() {
        CounterImpl counter = new CounterImpl("decoration");
        CounterDecoratorImpl decorator = new CounterDecoratorImpl(counter) {
            // just testing the defaults
        };
        assertEquals(counter.getValue(), decorator.getValue());
        counter.increment();
        assertEquals(counter.getValue(), decorator.getValue());
        decorator.increment();
        assertEquals(counter.getValue(), decorator.getValue());
        counter.increment(100);
        assertEquals(counter.getValue(), decorator.getValue());
        decorator.increment(50);
        assertEquals(counter.getValue(), decorator.getValue());
        decorator.reset();
        assertEquals(counter.getValue(), decorator.getValue());
        decorator.setDescription("description");
        assertEquals("description", counter.getDescription());
        decorator.setUnit("UNIT");
        assertEquals("UNIT", counter.getUnit());
        decorator.increment(42);
        assertEquals(42, decorator.readValueAndReset());
        assertEquals(0, decorator.getValue());
        
    }
    
    @Test
    public void testCreateWithDecorator() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(MaxValue.class);
         Counter decoratedCounter =  factory.createCounter(null, featureList);
         decoratedCounter.setDescription("boe");
         assertEquals(0, decoratedCounter.getValue());                 
    }
    
    @Test
    public void testGetSpecificFeature() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(MaxValue.class);
         featureList.add(EventEmitter.class);
         CounterDecorator decoratedCounter =  factory.createCounter(null, featureList);
         
         assertEquals(MaxValue.class, decoratedCounter.getSpecificFeature(MaxValue.class).getClass());
         assertEquals(EventEmitter.class, decoratedCounter.getSpecificFeature(EventEmitter.class).getClass());
         assertNull(decoratedCounter.getSpecificFeature(Gauge.class));
         
    }
}
